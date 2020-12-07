package com.example.startuppage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;


public class Reporting extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    BarChart barChart;
    LineChart lineChat;
    int currentMonth;
    Boolean isBar = true;
    Float totalThisWeek;
    EditText currentSelectedDay;
    String[] months = new String[]{
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"};
    private void createLineChart(QuerySnapshot result){
        TextView xLabelLine =  findViewById(R.id.xlabelline_id);
        Spinner s = (Spinner) findViewById(R.id.monthDropDown);
        String currentSelectMonthStr = s.getSelectedItem().toString();

            lineChat.setVisibility(View.VISIBLE);
            barChart.setVisibility(View.GONE);

            List<Entry> entries = new ArrayList<Entry>();
            ArrayList<String> dataLoad = new ArrayList<String>();
            Float total = new Float(0.0);
            String currentSelectedDayStr = currentSelectedDay.getText().toString();
            for (QueryDocumentSnapshot data : result) {
                String Day = data.get("Date").toString().split("/")[2];
                String StartHour = data.get("StartTime").toString().split(":")[0];
                String EndHour = data.get("EndTime").toString().split(":")[0];
                String Month = data.get("Date").toString().split("/")[1];
                String Year = data.get("Date").toString().split("/")[0];
                Log.i("Records ", data.getData().toString());
                Object temp = data.get("Hours");
                int currentSelectedMonthIndex = -1;
                for (int i=0;i<months.length;i++) {
                    if (months[i].equals(currentSelectMonthStr)) {
                        currentSelectedMonthIndex = i;
                        break;
                    }
                }
                Log.i("Records ", "xxxx ");

                if(currentSelectedMonthIndex==Integer.valueOf(Month)-1) {
                    if(!currentSelectedDayStr.isEmpty()){
                        Log.i("Records ", "no day " +currentSelectedDayStr);
                        Log.i("Records ", currentSelectedDayStr);
                        if(currentSelectedDayStr.equals(Day)){
                            entries.add(new Entry(Float.valueOf(StartHour), total));
                            total+=Float.valueOf(temp.toString());
                            entries.add(new Entry(Float.valueOf(EndHour), total));

                        }
                    }else{
                        Log.i("Records ", currentSelectedDayStr);
                        Log.i("Records ", Day);
                        Log.i("Records ", "yes day ");

                        entries.add(new Entry(Float.valueOf(Day), Float.valueOf(temp.toString())));
                    }
                }else{
                    Log.i("Records ", String.valueOf(currentSelectedMonthIndex));
                    Log.i("Records ", String.valueOf(Integer.valueOf(Month)));

                }
            }
            Log.i("Records ", "xxxxasdasd ");
            totalThisWeek = total;
            Collections.sort(entries, new EntryXComparator());
            LineDataSet dataSet = new LineDataSet(entries, months[currentMonth] + " "+ currentSelectedDayStr); // add entries to dataset
        dataSet.setCircleSize(10);
            LineData lineData = new LineData(dataSet);
        lineData.setValueTextSize(15);
            Log.i("Records as", "xxxx ");


            lineChat.setData(lineData);
            Log.i("Records as", "xxxx ");

            lineChat.invalidate(); // refresh
        handleTitles();



    }
    private void createBarChart(QuerySnapshot result){

        lineChat.setVisibility(View.GONE);
        barChart.setVisibility(View.VISIBLE);
        Spinner s = (Spinner) findViewById(R.id.monthDropDown);
               String currentSelectMonthStr = s.getSelectedItem().toString();
        Float total = new Float(0.0);
        String currentSelectedDayStr = currentSelectedDay.getText().toString();
        if(currentSelectMonthStr!=null){

            List<BarEntry> entries = new ArrayList<BarEntry>();
            ArrayList<String> dataLoad = new ArrayList<String>();
            for (QueryDocumentSnapshot data : result) {
                String Day = data.get("Date").toString().split("/")[2];
                String Month = data.get("Date").toString().split("/")[1];
                String StartHour = data.get("StartTime").toString().split(":")[0];
                String EndHour = data.get("EndTime").toString().split(":")[0];
                String Year = data.get("Date").toString().split("/")[0];
                Log.i("Records ", data.getData().toString());
                Object temp = data.get("Hours");
                int currentSelectedMonthIndex = -1;
                for (int i=0;i<months.length;i++) {
                    if (months[i].equals(currentSelectMonthStr)) {
                        currentSelectedMonthIndex = i;
                        break;
                    }
                }
                if(currentSelectedMonthIndex==Integer.valueOf(Month)-1) {
                    if(!currentSelectedDayStr.isEmpty()){
                        Log.i("Records ", "no day " +currentSelectedDayStr);
                        Log.i("Records ", currentSelectedDayStr);
                        if(currentSelectedDayStr.equals(Day)){
                            entries.add(new BarEntry(Float.valueOf(StartHour), new Float(0)));
                            total+=Float.valueOf(temp.toString());
                            entries.add(new BarEntry(Float.valueOf(EndHour), total));

                        }
                    }else{
                        Log.i("Records ", currentSelectedDayStr);
                        Log.i("Records ", Day);
                        Log.i("Records ", "yes day ");

                        entries.add(new BarEntry(Float.valueOf(Day), Float.valueOf(temp.toString())));
                    }
                }else{
                    Log.i("Records ", String.valueOf(currentSelectedMonthIndex));
                    Log.i("Records ", String.valueOf(Integer.valueOf(Month)));

                }
            }
            BarDataSet dataSet = new BarDataSet(entries, months[11]); // add entries to dataset
            BarData BarData = new BarData(dataSet);
            barChart.setData(BarData);
            barChart.invalidate(); // refresh
            handleTitles();

        }else{

        }



    }
    private void getUserByEmail (String email) {
        db.collection("users").whereEqualTo("email",email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot result = task.getResult();
                    for (QueryDocumentSnapshot document: result
                         ) {
                        Log.i("Reportasdasding ", document.getData().toString());
                    }

                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }

            }
        });
    }
        private void createUserChart (String uid){
        db.collection("users").document(uid).collection("records")
                .get() .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.i("Is bar selected ", String.valueOf(isBar));
                    if (isBar) {
                        createLineChart(task.getResult());
                    } else {
                        createBarChart(task.getResult());
                    }
                    handleTitles();

                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporting);
         currentSelectedDay = findViewById(R.id.searchField);
//        Chart toggle
        Button chartToggle =  findViewById(R.id.chartToggle);
        chartToggle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)            {
                isBar=!isBar;
                createUserChart(getCurrentUserId());

            }});
//        Month Dropdown
        Spinner s = (Spinner) findViewById(R.id.monthDropDown);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, months);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                createUserChart(getCurrentUserId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) ;
        s.setSelection(month);
        int year = calendar.get(Calendar.YEAR);
        barChart =  findViewById(R.id.barChart);
        lineChat =  findViewById(R.id.lineChart);
        XAxis barX = barChart.getXAxis();
        barChart.setLabelFor(0);
        barX.setPosition(XAxis.XAxisPosition.BOTTOM);
        XAxis lineX = lineChat.getXAxis();
        lineX.setPosition(XAxis.XAxisPosition.BOTTOM);

        currentMonth = month;

        Button SearchR = findViewById(R.id.searchReport);

        SearchR.  setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)            {
                WindowInsetsController windowInsetsController = v.getWindowInsetsController();
                windowInsetsController.hide(WindowInsets.Type.ime());
                EditText SearchR = findViewById(R.id.searchField);
                if(!SearchR.getText().toString().isEmpty() && Integer.valueOf(SearchR.getText().toString())>31) {
                    Log.i("Toast ",  "Toast ");

                    Toast.makeText(Reporting.this, "Your number of days is invalid", Toast.LENGTH_LONG).show();
                }
                if (SearchR.getText().toString().length() == 1) {
                    SearchR.setText("0"+SearchR.getText().toString());
                }
                createUserChart(getCurrentUserId());
                handleTitles();
            }});

        createUserChart(getCurrentUserId());

    } private void handleTitles(){
        EditText SearchR = findViewById(R.id.searchField);
        if(!SearchR.getText().toString().isEmpty()){
            TextView xLabelLine =  findViewById(R.id.xlabelline_id);
            TextView xLabel =  findViewById(R.id.xlabel_id);
            Spinner s = (Spinner) findViewById(R.id.monthDropDown);
            String currentSelectMonthStr = s.getSelectedItem().toString();
            xLabelLine.setText("Hours\\Hour of the day");
            xLabel.setText("Hours\\Hour of the day");
            Description desc = new Description();
            desc.setText("Showing total hours worked in one day");
            lineChat.setDescription(desc);
            barChart.setDescription(desc);
            LinearLayout totalHoursLay =  findViewById(R.id.totalHoursCon);
            TextView totalHoursText =  findViewById(R.id.totalHoursText);
            totalHoursLay.setVisibility(View.VISIBLE);
            totalHoursText.setText(totalThisWeek.toString());

        }else{
            TextView xLabelLine =  findViewById(R.id.xlabelline_id);            TextView xLabel =  findViewById(R.id.xlabel_id);

            Spinner s = (Spinner) findViewById(R.id.monthDropDown);
            String currentSelectMonthStr = s.getSelectedItem().toString();
            xLabelLine.setText("Hours\\Day of the month");
            xLabel.setText("Hours\\Day of the month");
            Description desc = new Description();
            desc.setText("Showing hours worked in the month of " + currentSelectMonthStr);
            lineChat.setDescription(desc);                    barChart.setDescription(desc);
            LinearLayout totalHoursLay =  findViewById(R.id.totalHoursCon);
            totalHoursLay.setVisibility(View.GONE);
            TextView totalHoursText =  findViewById(R.id.totalHoursText);
            totalHoursText.setText(totalThisWeek.toString());

        }
    }
    private String getCurrentUserId(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String userId = user.getUid();
return userId;
        }
        else{
            return null;
        }
    }
}