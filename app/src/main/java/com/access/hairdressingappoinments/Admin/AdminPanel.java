package com.access.hairdressingappoinments.Admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.access.hairdressingappoinments.Adapters.TimeSlotsAdapter;
import com.access.hairdressingappoinments.DataModels.Day;
import com.access.hairdressingappoinments.R;
import com.access.hairdressingappoinments.ShowDateTImeSlot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AdminPanel extends AppCompatActivity {
    private RecyclerView recyclerViewTimeSlots;
    private Button buttonAddTimeSlot, buttonMarkDayOff, removeMarkDayOff,buttonCheckAllAppointments;
    private CalendarView calendarViewAdmin;
    private FirebaseDatabase database;
    TimeSlotsAdapter adapter;
    TextView offDay;
    String selectedDay;
    Calendar today;
    String todayDate;
    Day day;
    private ArrayList<String> timeSlots ;
    private String selectedDate;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        // Initialize Firebase database
        database = FirebaseDatabase.getInstance();
        timeSlots = new ArrayList<>();

        recyclerViewTimeSlots = findViewById(R.id.recyclerTimeSlots);
        offDay = findViewById(R.id.offDay);
        buttonAddTimeSlot = findViewById(R.id.buttonAddTimeSlot);
        buttonMarkDayOff = findViewById(R.id.buttonMarkDayOff);
        calendarViewAdmin = findViewById(R.id.calendarViewAdmin);
        removeMarkDayOff = findViewById(R.id.removeMarkDayOff);
        buttonCheckAllAppointments = findViewById(R.id.buttonCheckAllAppointments);

         today = Calendar.getInstance();
        todayDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", today.get(Calendar.YEAR), today.get(Calendar.MONTH) + 1, today.get(Calendar.DAY_OF_MONTH));
        selectedDate = todayDate;
        fetchTimeSlotsFromFirebase(selectedDate);

        // Fetch operational hours after initializing Firebase
        fetchTimeSlotsFromFirebase(selectedDate);
        // Setup RecyclerView for Time Slots
        recyclerViewTimeSlots.setLayoutManager(new LinearLayoutManager(this));
         adapter = new TimeSlotsAdapter(timeSlots, this, selectedDate);
        recyclerViewTimeSlots.setAdapter(adapter);

        // Add Time Slot Button Click Listener
        ;
        buttonAddTimeSlot.setOnClickListener(v -> {
            if (selectedDate != null) {
                // Get today's date
                 today = Calendar.getInstance();
                Calendar selectedCalendar = Calendar.getInstance();

                // Clear the time part (hours, minutes, seconds, and milliseconds) for accurate date comparison
                today.set(Calendar.HOUR_OF_DAY, 0);
                today.set(Calendar.MINUTE, 0);
                today.set(Calendar.SECOND, 0);
                today.set(Calendar.MILLISECOND, 0);

                // Assuming selectedDate is in "yyyy-MM-dd" format
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                try {
                    selectedCalendar.setTime(sdf.parse(selectedDate));
                    selectedCalendar.set(Calendar.HOUR_OF_DAY, 0);
                    selectedCalendar.set(Calendar.MINUTE, 0);
                    selectedCalendar.set(Calendar.SECOND, 0);
                    selectedCalendar.set(Calendar.MILLISECOND, 0);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (selectedCalendar.before(today)) {
                    Toast.makeText(AdminPanel.this, "Please select today or a future date", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseReference dayOffRef = database.getReference("settings/DayOffList");
                    dayOffRef.child(selectedDate).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Toast.makeText(AdminPanel.this, "This day is marked as day off", Toast.LENGTH_SHORT).show();
                            } else {
                                showAddTimeSlotDialog();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle possible errors
                        }
                    });
                }
            } else {
                Toast.makeText(AdminPanel.this, "Please select a date", Toast.LENGTH_SHORT).show();
            }
        });
        buttonCheckAllAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminPanel.this, SeeAllApointments.class));
            }
        });

        // Capture the selected date from the CalendarView
        calendarViewAdmin.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Month is 0-based, so we need to add 1 to it
            month += 1;
            selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month, dayOfMonth);
            Log.d("SelectedDate", "Selected date: " + selectedDate);  // Debugging line to confirm the selected date
            fetchTimeSlotsFromFirebase(selectedDate);
        });
        // Update UI or perform actions based on the selected day if necessary});


        // Mark Day Off Button Click Listener
        buttonMarkDayOff.setOnClickListener(v -> {
            if (selectedDate != null) {
                markDayOffInFirebase(selectedDate);
            } else {
                Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
            }
        });
        removeMarkDayOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedDate!=null){
                    FirebaseDatabase.getInstance().getReference("settings").child("DayOffList").child(selectedDate).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                FirebaseDatabase.getInstance().getReference("settings").child("DayOffList").child(selectedDate).removeValue();
                                Toast.makeText(AdminPanel.this, "Day Off Removed", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(AdminPanel.this, "This day isn't Off", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }



            }
        });
    }

    private void showAddTimeSlotDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Time Slot");

        // Inflate the layout for the dialog
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_time_slot, null);
        builder.setView(dialogView);

        // Initialize UI elements
        TimePicker startTimePicker = dialogView.findViewById(R.id.startTimePicker);
        TimePicker endTimePicker = dialogView.findViewById(R.id.endTimePicker);

        // Set up the TimePickers
        startTimePicker.setIs24HourView(true);
        endTimePicker.setIs24HourView(true);

        builder.setPositiveButton("Add", (dialog, which) -> {
            // Get the selected time
            int startHour = startTimePicker.getHour();
            int startMinute = startTimePicker.getMinute();
            int endHour = endTimePicker.getHour();
            int endMinute = endTimePicker.getMinute();

            String startTime = String.format("%02d:%02d", startHour, startMinute);
            String endTime = String.format("%02d:%02d", endHour, endMinute);

            // Add time slot to Firebase
            addTimeSlotToFirebase(startTime, endTime, selectedDay);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void addTimeSlotToFirebase(String startTime, String endTime, String selectedDay) {
        if (!selectedDate.isEmpty()) {
            DatabaseReference database = FirebaseDatabase.getInstance().getReference("DayTimmings").child(String.valueOf(selectedDate));
            HashMap hashMap = new HashMap<>();

            hashMap.put("Date", selectedDate);
            hashMap.put("DayOfWeek", selectedDay);
            hashMap.put("StartTime", startTime);
            hashMap.put("EndTime", endTime);

            database.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(AdminPanel.this, "Time added", Toast.LENGTH_SHORT).show();
                        divideSlots(endTime, startTime, selectedDate);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AdminPanel.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void divideSlots(String endTimeStr, String startTimeStr, String selectedDate) {
// Convert strings to LocalTime

        LocalTime startTime = LocalTime.parse(startTimeStr, DateTimeFormatter.ofPattern("H:mm"));
        LocalTime endTime = LocalTime.parse(endTimeStr, DateTimeFormatter.ofPattern("H:mm"));

        List<String> timeSlots = generateTimeSlots(startTime, endTime);

// Reference to the Firebase database
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("timeSlots").child(selectedDate);
        HashMap<String, Object> hashMap = new HashMap<>();

        int i = 0;
// Loop through the time slots
        for (String slot : timeSlots) {
            // Create a unique key for each time slot
            hashMap.put("TimeSlot" + String.valueOf(i), slot);
            i++;
        }

// Add the time slots to the Firebase database
        ref.setValue(hashMap);
    }

    private List<String> generateTimeSlots(LocalTime startTime, LocalTime endTime) {
        List<String> timeSlots = new ArrayList<>();
        LocalTime currentTime = startTime;

        while (currentTime.plusHours(1).isBefore(endTime) || currentTime.plusHours(1).equals(endTime)) {
            LocalTime nextTime = currentTime.plusHours(1);
            if (nextTime.isBefore(endTime) || nextTime.equals(endTime)) {
                timeSlots.add(currentTime.format(DateTimeFormatter.ofPattern("H:mm")) + " - " + nextTime.format(DateTimeFormatter.ofPattern("H:mm")));
            }
            currentTime = nextTime;
        }

        return timeSlots;
    }



    private void markDayOffInFirebase(String date) {
        DatabaseReference dayOffRef = database.getReference("settings/DayOffList");
        dayOffRef.child(date).setValue("true").addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseDatabase.getInstance().getReference("DayTimmings").child(date).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            FirebaseDatabase.getInstance().getReference("DayTimmings").child(date).removeValue();
                            FirebaseDatabase.getInstance().getReference("timeSlots").child(date).removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Toast.makeText(this, "Day off marked successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to mark day off", Toast.LENGTH_SHORT).show();
            }
        });
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
                Toast.makeText(AdminPanel.this, "Failed to load time slots.", Toast.LENGTH_SHORT).show();
            }
        });
    }



}