package com.access.hairdressingappoinments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.access.hairdressingappoinments.Admin.AdminPanel;
import com.access.hairdressingappoinments.DataModels.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {
    public FirebaseDatabase database;
    FirebaseAuth auth;
    String userName,password;
    EditText passwordTextView , userNameTextView;

    CardView register;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_in);
        auth=FirebaseAuth.getInstance();

        userNameTextView = findViewById(R.id.editTextTextEmailAddressLogin);
        passwordTextView = findViewById(R.id.editTextTextPasswordLogin);
        database = FirebaseDatabase.getInstance();
        CardView login = findViewById(R.id.buttonLogin);
        CardView signin = findViewById(R.id.buttonSignin);
        CardView Admin = findViewById(R.id.Admin);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(SignInActivity.this, RegistrationActivity.class));
               finish();
            }
        });
        Admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = userNameTextView.getText().toString();
                password = passwordTextView.getText().toString();
                if (!userName.isEmpty() && !password.isEmpty() && userName.equals("admin@gmail.com") && password.equals("admin")) {
                    startActivity(new Intent(SignInActivity.this, AdminPanel.class));
                    finish();
                    Toast.makeText(SignInActivity.this, "Connect as Admin", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SignInActivity.this, "Please enter correct Admin Details", Toast.LENGTH_SHORT).show();
                }
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                 userName = userNameTextView.getText().toString();
                 password = passwordTextView.getText().toString();



                if(userName.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignInActivity.this, "Please enter user name and password", Toast.LENGTH_LONG).show();
                }
                else {
                    Login(userName,password);
                }
            }
        });

    }
    public void Login(String userName, String password) {
        auth.signInWithEmailAndPassword(userName, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                          startActivity(new Intent(SignInActivity.this, MainActivity.class));
                          finish();
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(SignInActivity.this, "Successful Login", Toast.LENGTH_LONG).show();
                        } else {

                                // If sign in fails, display a message to the user.
                                Toast.makeText(SignInActivity.this, "Failed to Connect", Toast.LENGTH_LONG).show();
                            }
                    }
                });
    }
}