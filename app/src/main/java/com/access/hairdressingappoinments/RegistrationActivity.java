package com.access.hairdressingappoinments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.access.hairdressingappoinments.DataModels.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    public FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        CardView register =findViewById(R.id.buttonRegister);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText nameTextView = findViewById(R.id.editTextPersonName);
                EditText emailTextView = findViewById(R.id.editTextTextEmailAddressSignin);
                EditText passwordTextView = findViewById(R.id.editTextTextPasswordSignin);
                EditText phoneTextView = findViewById(R.id.editTextPhone);

                String name = nameTextView.getText().toString();
                String email = emailTextView.getText().toString();
                String password = passwordTextView.getText().toString();
                String phone = phoneTextView.getText().toString();



                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty())
                {
                    Toast.makeText(RegistrationActivity.this, "Please fill in all the required information", Toast.LENGTH_LONG).show();
                }
                else {
                    User user = new User(name, email, password, phone);
                    Register(user);
                }
            }
        });

    }

    public void Register(User user) {
        String userName = user.getEmail();
        String password = user.getPassword();
        mAuth.createUserWithEmailAndPassword(userName, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            DatabaseReference myRef = database.getReference("users").child(FirebaseAuth.getInstance().getUid());
                            myRef.setValue(user);
                            startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(RegistrationActivity.this, "Something goes wrong, Please try again.\nNote: A valid email and password with at least 6-characters are required.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}