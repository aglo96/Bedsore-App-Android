package com.example.ag.bedsoreapp;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class SensorFragment1 extends Fragment  {
    private DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    private LineChart lineChart;
    private View rootView;
    private Date date;
    private static ArrayList<Float> listTimeStamp = new ArrayList<Float>();
    private static ArrayList<Float> listPressure = new ArrayList<Float>();
    private String firebaseDate;
    private String chosenDate;
    private String displayDate;
    private final List<Entry> lineEntries = new ArrayList<>();  //list of all data points for the chart
    private TextView dateTextView;

    private float pressureFloat;
    private String timeStamp;



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 111) {
            lineChart.clear();
            chosenDate = data.getStringExtra("queryDate");
            displayDate = data.getStringExtra("displayDate");
            dateTextView.setText(displayDate);
            plotChart();

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_graph,container,false);

        Date date = Calendar.getInstance().getTime(); //Date from DatePicker

        SimpleDateFormat mDateFormat = new SimpleDateFormat("d MMMM yyyy");
        mDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
        displayDate = mDateFormat.format(date);
        dateTextView = (TextView) rootView.findViewById(R.id.dateTextView); //must use rootview
        dateTextView.setText(displayDate);

        SimpleDateFormat mDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        mDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
        chosenDate = mDateFormat2.format(date);



        lineChart = (LineChart) rootView.findViewById(R.id.linechart);

        Button datePicker = (Button) rootView.findViewById(R.id.datePicker); //must use rootview
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new SensorDatePickerFragment();
                newFragment.setTargetFragment(SensorFragment1.this, 111);
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });

        plotChart();

        return rootView;
    }



    public void plotChart() {
        DatabaseReference ref =mDatabaseReference.child("Sensor Data");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    // To obtain the chosen date to be queried
                    if (ds.getKey().equals(chosenDate)) {
                        firebaseDate = chosenDate;
                        Log.i("FirebaseDate", firebaseDate);
                        // Query the data from the chosen date
                        Query query = mDatabaseReference.child("Sensor Data").child(firebaseDate).orderByKey().limitToLast(1000);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    try {
                                        pressureFloat = ds.child("PValue1").getValue(Float.class);
                                        timeStamp = ds.child("Timestamp").getValue(String.class);   // Retrieve timestamp from database
                                    }
                                    catch (NullPointerException ex) {
                                        Log.i("ERROR", "NullPointerException");
                                    }
                                    listPressure.add(pressureFloat);
                                    Log.i("PRESSUREVALUES", listPressure.toString());

                                    float timeStampFloat = Float.parseFloat(timeStamp);   // Convert timestamp to float and pass the float value to the graph
                                    listTimeStamp.add(timeStampFloat);

                                    lineEntries.add(new Entry(timeStampFloat,pressureFloat)); // Append the data point to the Arraylist of data points.
                                }

                                MPAndroidChart.displayLineChart(lineEntries, lineChart);   // Plot the graph out using the list of data points.

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.e("THERE IS DATABASE ERROR",lineEntries.toString());
                            }
                        });
                    }
                    // remove existing displayed graph from ui if chosen date does not have any data.
                    else if (!ds.getKey().equals(chosenDate)){
                        Log.i("NOT CHOSEN", "NOT CHOSEN");
                        lineChart.clear();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }






}


