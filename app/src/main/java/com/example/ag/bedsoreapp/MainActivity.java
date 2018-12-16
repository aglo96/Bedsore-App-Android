package com.example.ag.bedsoreapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    private long pressure1;
    private long pressure2;
    private long pressure3;
    private long pressure4;


    @Override
    protected void onPause() { // Removes the transition animation when switching activities
        super.onPause();
        overridePendingTransition(0,0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Querying Firebase
        Query query1 = mDatabaseReference.child("Sensor Data").orderByKey().limitToLast(1);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    final String date = ds.getKey();
                    Query query2 = mDatabaseReference.child("Sensor Data").child(date).orderByKey().limitToLast(1);
                    query2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                ImageView circleOne = findViewById(R.id.circle1);
                                ImageView circleTwo = findViewById(R.id.circle2);
                                ImageView circleThree = findViewById(R.id.circle3);
                                ImageView circleFour = findViewById(R.id.circle4);

                                try {
                                    pressure1 = ds.child("Pressure1").getValue(long.class);
                                    pressure2 = ds.child("Pressure2").getValue(long.class);
                                    pressure3 = ds.child("Pressure3").getValue(long.class);
                                    pressure4 = ds.child("Pressure4").getValue(long.class);
                                }
                                catch (NullPointerException ex) {
                                    Log.i("ERROR", "NullPointerException");
                                }

                                // Turn RED if pressure[i] ==1 because it exceeds threshold
                                if (pressure1 == 1){
                                    circleOne.setImageResource(R.drawable.circles_red);
                                }else{
                                    circleOne.setImageResource(R.drawable.circles_green);
                                }

                                if (pressure2 == 1){
                                    circleTwo.setImageResource(R.drawable.circles_red);
                                }else{
                                    circleTwo.setImageResource(R.drawable.circles_green);
                                }

                                if (pressure3 == 1){
                                    circleThree.setImageResource(R.drawable.circles_red);
                                }else{
                                    circleThree.setImageResource(R.drawable.circles_green);
                                }

                                if (pressure4 == 1){
                                    circleFour.setImageResource(R.drawable.circles_red);
                                }else{
                                    circleFour.setImageResource(R.drawable.circles_green);
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.i("firebaseError", "ERROR");
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("firebaseError2", "ERROR");
            }
        });





        // Bottom navigation bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.historical_data:
                        Intent intent = new Intent(MainActivity.this, GraphActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.doctor:
                        Intent intent2 = new Intent(MainActivity.this, DoctorChat.class);
                        startActivity(intent2);
                        break;
                    case R.id.medicalRecords:
                        Intent intent3 = new Intent(MainActivity.this, Records.class);
                        startActivity(intent3);
                        break;
                }
                return true;
            }
        });
    }
}
