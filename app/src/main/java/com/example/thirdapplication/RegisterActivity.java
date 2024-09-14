package com.example.thirdapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class RegisterActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword, editTextName, editTextMobile;
    Button buttonReg;
    TextView textViewLoginNow, textViewGuestLogin;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    ProgressBar progressBar;
    CheckBox checkBoxIsAgent;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(myIntent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editTextName = findViewById(R.id.name);
        editTextMobile = findViewById(R.id.mobile);
        checkBoxIsAgent = findViewById(R.id.isAgent);
        progressBar = findViewById(R.id.progressBar);
        textViewLoginNow = findViewById(R.id.loginNow);
        textViewGuestLogin = findViewById(R.id.guestLogin);
        buttonReg = findViewById(R.id.btn_register);

        textViewLoginNow.setOnClickListener(view -> {
            Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(myIntent);
            finish();
        });

        textViewGuestLogin.setOnClickListener(view -> {
            Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
            myIntent.putExtra("isGuest", true); // Indicate guest login
            startActivity(myIntent);
            finish();
        });

        buttonReg.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String name = editTextName.getText().toString().trim();
            String mobile = editTextMobile.getText().toString().trim();
            boolean isAgent = checkBoxIsAgent.isChecked();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(RegisterActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(RegisterActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    saveUserDataInFirestore(user.getUid(), email, name, mobile, isAgent);
                                    Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                                    myIntent.putExtra("isAgent", isAgent);
                                    startActivity(myIntent);
                                    finish();
                                }
                                Toast.makeText(RegisterActivity.this, "Account Created.", Toast.LENGTH_SHORT).show();
                            } else {
                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });
    }

    private void saveUserDataInFirestore(String uid, String email, String name, String mobile, boolean isAgent) {
        User user = new User(email, name, mobile, isAgent);
        db.collection("User").document(uid).set(user, SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.d("TAG", "User data successfully written!"))
                .addOnFailureListener(e -> {
                    Log.w("TAG", "Error writing document", e);
                    Toast.makeText(RegisterActivity.this, "Failed to save user data.", Toast.LENGTH_SHORT).show();
                });
    }

    public static class User {
        private String email;
        private String name;
        private String mobile;
        private boolean agent;

        public User() {
        }

        public User(String email, String name, String mobile, boolean agent) {
            this.email = email;
            this.name = name;
            this.mobile = mobile;
            this.agent = agent;
        }

        public boolean isAgent() {
            return agent;
        }
    }
}
