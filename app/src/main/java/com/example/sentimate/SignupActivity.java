package com.example.sentimate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    EditText username, email, password;
    Button btnSignup;

    private FirebaseAuth fAuth;
    private FirebaseDatabase fDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnSignup = findViewById(R.id.btnSignup);

        fAuth = FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameText = username.getText().toString().trim();
                String emailText = email.getText().toString().trim();
                String passwordText = password.getText().toString().trim();

                if (usernameText.isEmpty() || emailText.isEmpty() || passwordText.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                fAuth.createUserWithEmailAndPassword(emailText, passwordText)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                String userId = fAuth.getCurrentUser().getUid();

                                // Store username and email in database under this UID
                                DatabaseReference userRef = fDatabase.getReference("users").child(userId);
                                userRef.child("username").setValue(usernameText);
                                userRef.child("email").setValue(emailText);

                                Toast.makeText(SignupActivity.this, "User Registered Successfully!", Toast.LENGTH_SHORT).show();

                                // Redirect to HomeActivity directly after signup
                                Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(SignupActivity.this, "Signup failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }
}
