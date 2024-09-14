package com.example.thirdapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.thirdapplication.ui.dashboard.DashboardFragment;
import com.example.thirdapplication.ui.home.AgentHomeFragment;
import com.example.thirdapplication.ui.home.HomeFragment;
import com.example.thirdapplication.ui.notifications.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private boolean isAgent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();

        // Make full-screen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Get the navigation view and set the listener
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigationHome) {
                    loadFragment(isAgent ? new AgentHomeFragment() : new HomeFragment());
                    return true;
                } else if (item.getItemId() == R.id.navigationDashboard) {
                    loadFragment(new DashboardFragment());
                    return true;
                } else if (item.getItemId() == R.id.navigationNotifications) {
                    loadFragment(new NotificationsFragment());
                    return true;
                }
                return false;
            }
        });

        // Check if user is logged in and determine if they are an agent
        Intent intent = getIntent();
        boolean isGuest = intent.getBooleanExtra("isGuest", false);
        if (isGuest) {
            // For guest login, directly show HomeFragment
            loadFragment(new HomeFragment());
        } else if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            checkIfUserIsAgent();
        } else {
            // Handle case where user is not logged in (e.g., redirect to login)
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void checkIfUserIsAgent() {
        String uid = mAuth.getCurrentUser().getUid();
        db.collection("User").document(uid).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Boolean agent = document.getBoolean("agent");
                            isAgent = (agent != null && agent);
                            // Load the appropriate fragment based on user type
                            loadFragment(isAgent ? new AgentHomeFragment() : new HomeFragment());
                        } else {
                            // Handle case where no document exists for user
                            startActivity(new Intent(this, LoginActivity.class));
                            finish();
                        }
                    } else {
                        // Handle error
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    }
                });
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, fragment);
        fragmentTransaction.commit();
    }
}
