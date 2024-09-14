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

        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Get navigation view and set the listener
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
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
        });

        // Retrieve user status from intent
        Intent intent = getIntent();
        boolean isGuest = intent.getBooleanExtra("isGuest", false);
        isAgent = intent.getBooleanExtra("isAgent", false);

        if (isGuest) {
            loadFragment(new HomeFragment());
        } else if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            // Load the appropriate fragment based on whether the user is an agent
            loadFragment(isAgent ? new AgentHomeFragment() : new HomeFragment());
        } else {
            // Redirect to login if user is not logged in
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, fragment);
        fragmentTransaction.commit();
    }
}
