package com.access.hairdressingappoinments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.access.hairdressingappoinments.Adapters.BookedAppointmentsAdapter;
import com.access.hairdressingappoinments.Adapters.TimeSlotsAdapter;
import com.access.hairdressingappoinments.DataModels.AppointmentsData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BookAppointmentsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewBookedAppointments;
    private DatabaseReference database;

    private ArrayList<AppointmentsData> appointments ;
    String uid = FirebaseAuth.getInstance().getUid();
    BookedAppointmentsAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointments);

        CardView cardViewBookAppointment = findViewById(R.id.cardViewBookAppointment);
        recyclerViewBookedAppointments = findViewById(R.id.recyclerViewBookedAppointments);
        recyclerViewBookedAppointments.setLayoutManager(new LinearLayoutManager(this));
        appointments = new ArrayList<>();
        adapter = new BookedAppointmentsAdapter(BookAppointmentsActivity.this, appointments);

        uid = FirebaseAuth.getInstance().getUid(); // Ensure this is not null
        if (uid == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }
        database = FirebaseDatabase.getInstance().getReference("UserAppointments").child(uid);




        callRecycler();

        cardViewBookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(BookAppointmentsActivity.this, ShowDateTImeSlot.class));
            }
        });
    }

    private void callRecycler() {

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                appointments.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    AppointmentsData entriesModel = dataSnapshot.getValue(AppointmentsData.class);
                    // Add the model to the array list
                    appointments.add(entriesModel);

                    // Notify the adapter that the data set has changed
                    adapter.notifyDataSetChanged();
                }

                recyclerViewBookedAppointments.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}