package com.access.hairdressingappoinments;

import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.access.hairdressingappoinments.Adapters.TimeSlotsAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ShowDateTImeSlot extends AppCompatActivity {

    private CalendarView calendarView;
    private RecyclerView recyclerViewTimeSlots;
    private FirebaseDatabase database;
    TextView offDay;
    String todayDate;
    private List<String> timeSlots = new ArrayList<>();
    private TimeSlotsAdapter adapter;
    String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_date_time_slot);

        database = FirebaseDatabase.getInstance();

        // Initialize views
        calendarView = findViewById(R.id.calendarView);
        recyclerViewTimeSlots = findViewById(R.id.recyclerViewTimeSlots);
        offDay = findViewById(R.id.offDay);


        // Setup RecyclerView with an initial date
        Calendar today = Calendar.getInstance();
         todayDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", today.get(Calendar.YEAR), today.get(Calendar.MONTH) + 1, today.get(Calendar.DAY_OF_MONTH));
        selectedDate = todayDate;
        // Setup RecyclerView
        recyclerViewTimeSlots.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TimeSlotsAdapter(timeSlots, this, todayDate);
        recyclerViewTimeSlots.setAdapter(adapter);
        // Set a listener on the CalendarView
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Month is 0-based, so add 1
            month += 1;
             selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month, dayOfMonth);
            fetchTimeSlotsFromFirebase(selectedDate);

            // Update the adapter with the new selected date
            adapter = new TimeSlotsAdapter(timeSlots, this, selectedDate);
            recyclerViewTimeSlots.setAdapter(adapter);
        });

        // Optionally, fetch time slots for the current date on activity start
        fetchTimeSlotsFromFirebase(todayDate);

    }

    private void fetchTimeSlotsFromFirebase(String date) {
        // Clear the current list
        timeSlots.clear();

        // Reference to the specific date in Firebase
        DatabaseReference timeSlotsRef = database.getReference("timeSlots").child(date);

        timeSlotsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                        recyclerViewTimeSlots.setVisibility(View.VISIBLE);
                        offDay.setVisibility(View.GONE);
                    for (DataSnapshot slotSnapshot : snapshot.getChildren()) {
                        String timeSlot = slotSnapshot.getValue(String.class);
                        timeSlots.add(timeSlot);

                    }
                    // Notify adapter that data has changed
                    adapter.notifyDataSetChanged();
                }
                else {
                    FirebaseDatabase.getInstance().getReference("settings").child("DayOffList").child(date).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                recyclerViewTimeSlots.setVisibility(View.GONE);
                                offDay.setVisibility(View.VISIBLE);
                            }
                            else {
                                recyclerViewTimeSlots.setVisibility(View.GONE);
                                offDay.setVisibility(View.VISIBLE);
                                offDay.setText("No Appointments Available yet");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
                Toast.makeText(ShowDateTImeSlot.this, "Failed to load time slots.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}