package com.access.hairdressingappoinments.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.access.hairdressingappoinments.Admin.AdminPanel;
import com.access.hairdressingappoinments.BookAppointmentsActivity;
import com.access.hairdressingappoinments.R;
import com.access.hairdressingappoinments.SignInActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;


public class HomeFragment extends Fragment {

    CardView appointment ;
    FirebaseAuth auth;
    CardView logout;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_home, container, false);

        auth= FirebaseAuth.getInstance();
        appointment=root.findViewById(R.id.appointment);
        logout=root.findViewById(R.id.logout);

        appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), BookAppointmentsActivity.class));
                
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(logout.getContext());
                builder.setTitle("Logout");
                builder.setMessage("Are you sure you want to Logout?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (auth.getCurrentUser()!=null){
                            auth.signOut();
                            startActivity(new Intent(getContext(), SignInActivity.class));
                            Toast.makeText(getContext(), "Logged Out", Toast.LENGTH_SHORT).show();
                            ((Activity)getContext()).finish();
                        }
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

            }
        });


        return root;
    }
}