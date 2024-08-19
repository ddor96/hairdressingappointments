package com.access.hairdressingappoinments.Adapters;

import static java.security.AccessController.getContext;

import android.app.Activity;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.access.hairdressingappoinments.BookAppointmentsActivity;
import com.access.hairdressingappoinments.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class TimeSlotsAdapter extends RecyclerView.Adapter<TimeSlotsAdapter.ViewHolder> {

    private List<String> timeSlots;
    private Context context;
    String date;

    public TimeSlotsAdapter(List<String> timeSlots, Context context, String date) {
        this.timeSlots = timeSlots;
        this.context = context;
        this.date = date;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.time_slot_cards, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.timeSlot.setText(timeSlots.get(position));
        holder.itemView.setOnClickListener(v -> {

            String uid=FirebaseAuth.getInstance().getUid();
            if (uid!=null) {
                HashMap<String, Object> hashMap = new HashMap<>();
                String key = String.valueOf(System.currentTimeMillis());
                hashMap.put("Time", timeSlots.get(position));
                hashMap.put("Date", date);
                hashMap.put("Uid", uid);
                hashMap.put("key", key);
                String dateString = date;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                try {
                    Date date2 = sdf.parse(dateString);
                    long timestamp = date2.getTime();  // Convert to timestamp
                    hashMap.put("timeStamp", timestamp);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // Save selected time slot to Firebase

                FirebaseDatabase.getInstance().getReference("AllUserAppointments").child(key).setValue(hashMap);

                FirebaseDatabase.getInstance().getReference("UserAppointments").child(uid).child(key).setValue(hashMap);
                Toast.makeText(context, "Congratulations Your Appointment is Booked", Toast.LENGTH_SHORT).show();
                ((Activity) context).finish();
            }


        });
    }

    @Override
    public int getItemCount() {
        return timeSlots.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView timeSlot;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timeSlot = itemView.findViewById(R.id.text1);
        }
    }
}
