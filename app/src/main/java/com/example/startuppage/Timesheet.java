package com.example.startuppage;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import pub.devrel.easypermissions.EasyPermissions;

/*
TODO https://stackoverflow.com/questions/13341560/how-to-create-a-custom-dialog-box-in-android
 */
public class Timesheet extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "TimeSheet";
    Button clockInButton;
    Button clockOutButton;
    Button submitButton;
    Toolbar toolbar;
    ArrayList<Record> recordArray;
    ListView timeSheetView;
    RecordAdapter adapter;
    String currentFirebaseUser;
    TextView timeView;
    List<String> sch = new ArrayList<String>();
    //LocationService.
    boolean inOutFlag = false;
    DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    FirebaseFirestore db = FirebaseFirestore.getInstance(); //.document("users/NLwbSw1hdMgrYiUH66BzBnWuBdT2/records");
    protected static SQLiteDatabase localDb;
    //location
    private FusedLocationProviderClient fusedLocationClient;
    String currentAddress;
    String workAddress;//= "75 University Ave W, Waterloo, ON N2L 3C5, Canada";

    public void addEmployeeSchedule() {
        //add default schedule
        DocumentReference df = db.collection("users").document(currentFirebaseUser);
        Map<String, Object> newSchedule = new HashMap<>();
        newSchedule.put("schedule", Arrays.asList("12/9/20-08:00:00 to 12/9/20-16:00:00", "12/10/20-08:00:00 to 12/9/20-16:00:00", "12/10/20-08:00:00 to 12/10/20-16:00:00"));
        df.set(newSchedule, SetOptions.merge());
        Toast t = Toast.makeText(getApplicationContext(), "Created default schedule in Firebase", Toast.LENGTH_LONG);
        t.show();
    }

    public void getEmployeeSchedule() {
        Log.i(ACTIVITY_NAME, "attempting to get firestore schedule");
        DocumentReference df = db.collection("users").document(currentFirebaseUser);
        df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();
                if (doc != null && doc.get("schedule") != null) {
                    Log.i(ACTIVITY_NAME, "getEmployee schedule: query != null");
                    sch = (List<String>) doc.get("schedule");
                    Log.i(ACTIVITY_NAME, "Firestore: work schedule: " + sch);
                    for (int i = 0; i < sch.size(); i++) {
                        System.out.println(sch.get(i));
                    }
                    //workAddress = doc.getString("address");
                } else {
                    addEmployeeSchedule();
                    getEmployeeSchedule();
                    //Log.i(ACTIVITY_NAME, "Error: getting user work address. Generating dummy data.");
                }
            }
        });
    }

    public void checkUserID() {

        currentFirebaseUser = getIntent().getStringExtra("USERID"); // getuserID from previous intent
        if (currentFirebaseUser == null) {
            Log.i(ACTIVITY_NAME, "Error getting USERID... setting defualt user.");
            //fail safe incase USERID is not passed with intent
            currentFirebaseUser = "57vcmUkQZLhnEO8sRn42ojlvfmn2";
        }
        return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_timesheet, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(ACTIVITY_NAME, "user selected something.");
        switch (item.getItemId()) {
            case R.id.action_help:
                Log.i(ACTIVITY_NAME, "Launching help DialogFragment");
                FragmentManager fm = getSupportFragmentManager();
                String helpMessage = getResources().getString(R.string.timesheet_help);
                timesheet_help dialogFragment = timesheet_help.newInstance(helpMessage);//new timesheet_help("test");
                dialogFragment.show(fm, "Dialog");
                return true;
            case R.id.action_sick:
                Log.i(ACTIVITY_NAME, "sick day view.");
                //FragmentTransaction ft = getFragmentManager().beginTransaction();
                FragmentManager sickDayFm = getSupportFragmentManager();
                //timesheet_sick_frag sickFrag = timesheet_sick_frag.newInstance();
                //sickFrag.show(sickDayFm,"Dialog");
                DialogFragment sickFrag = new timesheet_sick_frag();
                sickFrag.show(sickDayFm, "SICK");
                return true;
            case R.id.action_view:
                Log.i(ACTIVITY_NAME, "Launching schedule view.");
                FragmentManager fm_view = getSupportFragmentManager();
                String schedule = sch.toString();
                timesheet_schedule viewFrag = timesheet_schedule.newInstance(schedule);//new timesheet_help("test");
                viewFrag.show(fm_view, "Dialog");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timesheet);
        Log.i(ACTIVITY_NAME, "Started");
        toolbar = findViewById(R.id.timeSheetToolbar);
        toolbar.inflateMenu(R.menu.toolbar_timesheet);
        toolbar.setTitle("Timesheet");
        setSupportActionBar(toolbar);
        checkLocation();
        checkUserID();
        getEmployeeSchedule();
        TimesheetDatabaseHelper dbHelper = new TimesheetDatabaseHelper(this);
        localDb = dbHelper.getWritableDatabase();
        loadFromDb();
        //currentFirebaseUser = getIntent().getStringExtra("USERID"); // getuserID from previous intent
        getWorkAddress();
        Log.i(ACTIVITY_NAME, "User id: " + currentFirebaseUser);

        clockInButton = findViewById(R.id.clockInButton);
        clockOutButton = findViewById(R.id.clockOutButton);
        timeSheetView = findViewById(R.id.timeSheetView);
        timeView = findViewById(R.id.inTimeView);
        submitButton = findViewById(R.id.submitButton);
        //getUser();
        recordArray = new ArrayList<>();
        recordArray = loadFromDb();

        adapter = new RecordAdapter(this);
        timeSheetView.setAdapter(adapter);

        clockInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLocation();
                if (inOutFlag == false) {
                    checkLocation();
                    Log.i(ACTIVITY_NAME, "work address: " + workAddress);
                    Log.i(ACTIVITY_NAME, "current address " + currentAddress);
                    if (currentAddress == null) {
                        Log.e(ACTIVITY_NAME, "Error: currentLocation = null");
                        return;
                    }
                    if (currentAddress.toLowerCase().contains(workAddress.toLowerCase())) {
                        Toast t = Toast.makeText(getApplicationContext(), "Clocked In", Toast.LENGTH_SHORT);
                        t.show();
                        Date dateObject = new Date();
                        timeView.setText("Clocked in at: " + timeFormat.format(dateObject));
                        inOutFlag = true;
                    } else {
                        Toast t = Toast.makeText(getApplicationContext(), "Error: User is not at work", Toast.LENGTH_SHORT);
                        t.show();
                    }
                } else {
                    Toast t = Toast.makeText(getApplicationContext(), "Error: Must Clock Out First", Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });
        clockOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timeView.getText().toString().compareTo("Clocked out") != 0) {
                    checkLocation();
                    Log.i(ACTIVITY_NAME, "work address: " + workAddress);
                    Log.i(ACTIVITY_NAME, "current address " + currentAddress);
                    if (currentAddress.toLowerCase().contains(workAddress.toLowerCase())) {
                        Toast t = Toast.makeText(getApplicationContext(), "Clocked Out", Toast.LENGTH_SHORT);
                        t.show();
                        String clockInText = timeView.getText().toString();
                        Record r = new Record(clockInText.substring(clockInText.length() - 8), false);
                        if (r.isSick == false) {
                            try {

                                r.calculateHours(r.clockInTime, r.clockOutTime);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        recordArray.add(r);
                        writeToDb(r);
                        adapter.notifyDataSetChanged();
                        inOutFlag = false;
                        timeView.setText(R.string.clockedOutText);
                    } else {
                        Toast t = Toast.makeText(getApplicationContext(), "Error: User not at work", Toast.LENGTH_SHORT);
                        t.show();
                    }
                } else {
                    Toast t = Toast.makeText(getApplicationContext(), "Error: Must Clock In First", Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(ACTIVITY_NAME, "Submit button pressed.");
                confirmSubmit();
            }
        });
    }

    public void submitToFireStore(Record record) {
        writeToFireStore(record.clockInTime, record.clockOutTime, record.hours, record.date, record.sickDay);
        return;
    }

    public void writeToFireStore(String startTime, String endTime, String hours, String date, String sick) {
        Map<String, Object> record = new HashMap<>();
        record.put("Date", date);
        record.put("StartTime", startTime);
        record.put("EndTime", endTime);
        record.put("Hours", hours);
        record.put("Sick", sick);
        db.collection("users").document(currentFirebaseUser).collection("records")
                .add(record)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(ACTIVITY_NAME, "DocumentSnapshot written with ID: ");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(ACTIVITY_NAME, "Error adding document", e);
            }
        });
        return;
    }

    public void addSickDay(String date) {
        Record r = new Record(date, true);
        r.setSickDay(date);
        recordArray.add(r);
        writeToDb(r);
        adapter.notifyDataSetChanged();
    }

    private void confirmSubmit() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm submission?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Log.i(ACTIVITY_NAME, "CurrentAddress: " + currentAddress + " workAddress: " + workAddress);
                        for (int i = 0; i < recordArray.size(); i++) {
                            submitToFireStore(recordArray.get(i));
                        }
                        recordArray.clear(); //remove everything from the list
                        adapter.notifyDataSetChanged();
                        localDb.execSQL("DELETE FROM " + TimesheetDatabaseHelper.TABLE_NAME);// delete all records in localdb
                        Toast.makeText(Timesheet.this, "Submitting...", Toast.LENGTH_SHORT).show();

                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
        return;
    }

    private class RecordAdapter extends ArrayAdapter<String> {
        public RecordAdapter(@NonNull Context context) {
            super(context, 0);
        }

        public int getCount() {

            return recordArray.size();
        }

        public String getItem(int position) {
            return recordArray.get(position).toString();
        }

        public String getClockOutTime(int position) {
            return recordArray.get(position).clockOutTime;
        }

        public String getSickDayDate(int pos) {
            return recordArray.get(pos).sickDay;
        }

        public String getClockInTime(int position) {
            return recordArray.get(position).clockInTime;
        }

        public String getDate(int position) {
            return recordArray.get(position).date;
        }

        public boolean getSickDay(int position) {
            return recordArray.get(position).isSick;
        }

        public String getHours(int position) throws ParseException {
            if (recordArray.get(position).isSick == false) {
                return recordArray.get(position).calculateHours(getClockInTime(position), getClockOutTime(position));
            } else {
                return null;
            }
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = Timesheet.this.getLayoutInflater();
            boolean sick = recordArray.get(position).isSick;//false;//recordArray.;
            View result = null;// = inflater.inflate(R.layout.timesheet_record, null);
            if (sick == false) {
                result = inflater.inflate(R.layout.timesheet_record, null);
                TextView dateView = result.findViewById(R.id.dateView);
                TextView clockInTimeView = result.findViewById(R.id.clockInView);
                TextView clockOutTimeView = result.findViewById(R.id.clockOutView);
                TextView hoursView = result.findViewById(R.id.hoursView);
                Date currentTime = Calendar.getInstance().getTime();
                Date date = new Date();
                dateView.setText(getDate(position));
                clockOutTimeView.setText(getClockOutTime(position));
                clockInTimeView.setText(getClockInTime(position));
                dateView.setText(getDate(position));
                try {
                    hoursView.setText(getHours(position));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            } else {
                // do the sick day list item
                result = inflater.inflate(R.layout.timesheet_record_sick, null);
                TextView sickDate = result.findViewById(R.id.sickDayRecord);
                sickDate.setText("Sick date off: " + getSickDayDate(position));
            }
            return result;
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private void writeToDb(Record r) {
        ContentValues values = new ContentValues();
        values.put(TimesheetDatabaseHelper.KEY_DATE, r.date);
        values.put(TimesheetDatabaseHelper.KEY_START, r.clockInTime);
        values.put(TimesheetDatabaseHelper.KEY_END, r.clockOutTime);
        values.put(TimesheetDatabaseHelper.KEY_HOURS, r.hours);
        values.put(TimesheetDatabaseHelper.KEY_SICK, String.valueOf(r.sickDay));
        localDb.insert(TimesheetDatabaseHelper.TABLE_NAME, "NullPlaceHolder", values);
    }

    private ArrayList<Record> loadFromDb() {
        ArrayList<Record> a = new ArrayList<>();
        String getRecords = "SELECT * FROM RECORDS";
        final Cursor cursor = localDb.rawQuery(getRecords, null);
        cursor.moveToFirst();
        Record r;
        while (!cursor.isAfterLast()) {
            String date = cursor.getString(cursor.getColumnIndex(TimesheetDatabaseHelper.KEY_DATE));
            String start = cursor.getString(cursor.getColumnIndex(TimesheetDatabaseHelper.KEY_START));
            String end = cursor.getString(cursor.getColumnIndex(TimesheetDatabaseHelper.KEY_END));
            String hours = cursor.getString(cursor.getColumnIndex(TimesheetDatabaseHelper.KEY_HOURS));
            String sick = cursor.getString(cursor.getColumnIndex((TimesheetDatabaseHelper.KEY_SICK)));
            Log.i(ACTIVITY_NAME, "DB reads " + sick);
            if (sick.compareTo("null") == 0) {
                r = new Record(start, false);
                r.date = date;
                r.clockOutTime = end;
                r.clockInTime = start;
                r.hours = hours;
                //r.isSick = Boolean.getBoolean(sick);
                a.add(r);
                r = null;
            } else {
                r = new Record(date, true);
                a.add(r);
            }
            cursor.moveToNext();
        }
        return a;
    }

    private class Record {
        boolean isSick;
        String sickDay;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date dateObject = new Date();
        String clockInTime;
        String clockOutTime = timeFormat.format(dateObject);
        //not initialized
        String date = dateFormat.format(dateObject);
        String hours = "-1";

        public Record(String clockInTime, boolean isSick) {
            this.isSick = isSick;
            if (isSick != true) {
                this.clockInTime = clockInTime;
            } else {
                this.sickDay = clockInTime;
            }

        }

        @Override
        public String toString() {
            return ("Date: " +
                    this.date + "\nClock in time: " +
                    this.clockInTime + "\nClock out time: " + this.clockOutTime);
        }

        private String calculateHours(String in, String out) throws ParseException {
            if (this.isSick == false && in != null && out != null) {
                Date date1 = timeFormat.parse(in);
                Date date2 = timeFormat.parse(out);
                double difference = date2.getTime() - date1.getTime();
                difference = difference / 1000;
                this.hours = Double.toString(round(difference / 3600, 2));
                return Double.toString(round(difference / 3600, 2));
            } else {
                return null;
            }
        }

        public void setSickDay(String day) {
            this.sickDay = day;
            return;
        }

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        localDb.close();
    }

    public void getWorkAddress() {
        //db.collection("users").document(currentFirebaseUser).collection("records")
        DocumentReference df = db.collection("users").document(currentFirebaseUser);
        df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();
                if (doc != null) {
                    Log.i(ACTIVITY_NAME, "Firestore: work address: " + doc.getString("address"));
                    workAddress = doc.getString("address");
                } else {
                    Log.i(ACTIVITY_NAME, "Error: getting user work address.");
                }
            }
        });
        return;
    }

    public void checkLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(ACTIVITY_NAME, "User has not granted location permissions.");
            String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
            EasyPermissions.requestPermissions(this, "Please grant the location permission", 1, perms);
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        List<Address> address = null;
                        if (location != null) {
                            Log.i(ACTIVITY_NAME, "Found user Location");
                            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                            //List<Address> address ;

                            try {
                                address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                currentAddress = address.get(0).getAddressLine(0);//address.get(0).toString();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        //Log.i(ACTIVITY_NAME, currentAddress);
                    }
                });
    }
}
