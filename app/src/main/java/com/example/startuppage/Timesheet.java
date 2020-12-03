package com.example.startuppage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Timesheet extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "TimeSheet";
    Button clockInButton;
    Button clockOutButton;
    ArrayList<Record> recordArray;
    ListView timeSheetView;
    RecordAdapter adapter;
    TextView timeView;
    boolean inOutFlag = false;
    DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    //https://stackoverflow.com/questions/7057845/save-arraylist-to-sharedpreferences
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timesheet);

        clockInButton = findViewById(R.id.clockInButton);
        clockOutButton = findViewById(R.id.clockOutButton);
        timeSheetView = findViewById(R.id.timeSheetView);
        timeView = findViewById(R.id.inTimeView);

        recordArray = new ArrayList<>();
        //recordArray.add(new Record("test clock in time"));

        adapter = new RecordAdapter(this);
        timeSheetView.setAdapter(adapter);
        clockInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (inOutFlag == false){
                    Toast t = Toast.makeText(getApplicationContext(),"Clocked In",Toast.LENGTH_SHORT);
                    t.show();
                    //String clockInTime = timeFormat.format(dateObject);
                    Date dateObject = new Date();
                    timeView.setText("Clocked in at: "+timeFormat.format(dateObject));
                    inOutFlag = true;
                }else{
                    Toast t = Toast.makeText(getApplicationContext(),"Error: Must Clock Out First",Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });
        clockOutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(timeView.getText().toString().compareTo("Clocked out") != 0) {
                    Toast t = Toast.makeText(getApplicationContext(), "Clocked Out", Toast.LENGTH_SHORT);
                    t.show();
                    String clockInText = timeView.getText().toString();
                    recordArray.add(new Record(
                            clockInText.substring(
                                    clockInText.length() - 8)));

                    adapter.notifyDataSetChanged();
                    inOutFlag = false;
                    timeView.setText("Clocked out");
                }else{
                    Toast t = Toast.makeText(getApplicationContext(),"Error: Must Clock In First",Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });


    }
    private class RecordAdapter extends ArrayAdapter<String>{
        public RecordAdapter(@NonNull Context context) {
            super(context, 0);
        }
        public int getCount(){

            return recordArray.size();
        }
        public String getItem(int position){
            return recordArray.get(position).toString();
        }
        public String getClockOutTime(int position){
            return recordArray.get(position).clockOutTime;
        }
        public String getClockInTime(int position){
            return recordArray.get(position).clockInTime;
        }
        public String getDate(int position){
            return recordArray.get(position).date;
        }
        public String getHours(int position) throws ParseException {
            return recordArray.get(position).calculateHours(getClockInTime(position),getClockOutTime(position));

        }
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = Timesheet.this.getLayoutInflater();
            View result = inflater.inflate(R.layout.timesheet_record,null);

            TextView dateView = result.findViewById(R.id.dateView);
            TextView clockInTimeView = result.findViewById(R.id.clockInView);
            TextView clockOutTimeView = result.findViewById(R.id.clockOutView);
            TextView hoursView = result.findViewById(R.id.hoursView);

            Date currentTime  = Calendar.getInstance().getTime();


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
            return result;
        }
    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    private class Record {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        int id;
        Date dateObject = new Date();
        String clockInTime;
        String clockOutTime = timeFormat.format(dateObject);
        //not initialized
        String date = dateFormat.format(dateObject);
        double hours;
        public Record(String clockInTime){
            this.clockInTime = clockInTime;
        }
        @Override
        public String toString(){
            return ("Date: "+this.date+"\nClock in time: "+this.clockInTime+"\nClock out time: "+this.clockOutTime);
        }
        public void clockout(){
            Date d = new Date();
            this.clockOutTime = timeFormat.format(d);
        }
        public int getId(){
            return this.id;
        }
        private String calculateHours(String in, String out) throws ParseException {
            Date date1 = timeFormat.parse(in);
            Date date2 = timeFormat.parse(out);
            double difference = date2.getTime() - date1.getTime();
            difference = difference/1000;
            Log.i(ACTIVITY_NAME,"Hours difference: "+ difference);
            return Double.toString(round(difference/3600,2));
        }

    }
}