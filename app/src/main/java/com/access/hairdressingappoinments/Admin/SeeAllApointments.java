package com.access.hairdressingappoinments.Admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.access.hairdressingappoinments.Adapters.BookedAppointmentsAdapter;
import com.access.hairdressingappoinments.BookAppointmentsActivity;
import com.access.hairdressingappoinments.DataModels.AppointmentsData;
import com.access.hairdressingappoinments.R;
import com.access.hairdressingappoinments.ShowDateTImeSlot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class SeeAllApointments extends AppCompatActivity {
    private RecyclerView recyclerViewBookedAppointments;
    private DatabaseReference database;

    private ArrayList<AppointmentsData> appointments;

    BookedAppointmentsAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_see_all_apointments);


        recyclerViewBookedAppointments = findViewById(R.id.recyclerViewBookedAppointments);
        recyclerViewBookedAppointments.setLayoutManager(new LinearLayoutManager(this));
        appointments = new ArrayList<>();
        adapter = new BookedAppointmentsAdapter(SeeAllApointments.this, appointments);


        database = FirebaseDatabase.getInstance().getReference("AllUserAppointments");


        callRecycler();

    }

    private void callRecycler() {
        database.orderByChild("timeStamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                appointments.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        AppointmentsData entriesModel = dataSnapshot.getValue(AppointmentsData.class);
                        if (entriesModel != null) {
                            appointments.add(entriesModel);
                        } else {
                            Log.e("callRecycler", "Appointment data is null");
                        }
                    }

                    // Reverse the list to show the most recent dates first
                    Collections.reverse(appointments);

                    // Notify the adapter that the data set has changed
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("callRecycler", "No data found");
                }
                recyclerViewBookedAppointments.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors
                Log.e("callRecycler", "Database error: " + error.getMessage());
            }
        });
    }


}
