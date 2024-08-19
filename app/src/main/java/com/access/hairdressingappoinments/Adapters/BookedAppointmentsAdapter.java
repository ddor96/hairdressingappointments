package com.access.hairdressingappoinments.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.access.hairdressingappoinments.DataModels.AppointmentsData;
import com.access.hairdressingappoinments.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class BookedAppointmentsAdapter extends RecyclerView.Adapter<BookedAppointmentsAdapter.MyViewHolder> {

    Context context;
    private ArrayList<AppointmentsData> bookedAppointments;

    public BookedAppointmentsAdapter(Context context, ArrayList<AppointmentsData> bookedAppointments) {
        this.context = context;
        this.bookedAppointments = bookedAppointments;
    }

    @NonNull
    @Override
    public BookedAppointmentsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.booked_appointments_cards, parent, false);
        return new BookedAppointmentsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookedAppointmentsAdapter.MyViewHolder holder, int position) {
        AppointmentsData data= bookedAppointments.get(position);
        holder.Date.setText(data.getDate());
        holder.Time.setText(data.getTime());

        String uid = data.getUid();
        if (uid != null) {
            FirebaseDatabase.getInstance().getReference("users").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        String name = snapshot.child("name").getValue(String.class);
                        holder.Name.setText(name);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });
        } else {
            Log.e("BookedAppointmentsAdapter", "UID is null");
            // Handle null UID case, possibly by setting a default value or hiding the name field
        }

    }

    @Override
    public int getItemCount() {
        return bookedAppointments.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView Name,Date,Time;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Name =itemView.findViewById(R.id.Name);
            Date =itemView.findViewById(R.id.Date);
            Time =itemView.findViewById(R.id.Time);

        }
    }
}
