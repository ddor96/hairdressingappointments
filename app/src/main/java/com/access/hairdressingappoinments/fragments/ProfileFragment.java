package com.access.hairdressingappoinments.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.access.hairdressingappoinments.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    TextView name,EMail, phone;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_profile, container, false);

        name=root.findViewById(R.id.username);
        EMail=root.findViewById(R.id.user_contact);
        phone=root.findViewById(R.id.phone);

        FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String Name= snapshot.child("name").getValue(String.class);
                    String Phone= snapshot.child("phone").getValue(String.class);
                    String Email= snapshot.child("email").getValue(String.class);

                    name.setText(Name);
                    EMail.setText(Email);
                    phone.setText(Phone);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return root;
    }
}