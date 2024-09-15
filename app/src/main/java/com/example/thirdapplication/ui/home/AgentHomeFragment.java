package com.example.thirdapplication.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.FragmentTransaction; // Import to handle fragment transactions

import com.example.thirdapplication.Adapter.OngoingAdapter;
import com.example.thirdapplication.Domain.OngoingDomain;
import com.example.thirdapplication.LoginActivity;
import com.example.thirdapplication.R;
import com.example.thirdapplication.databinding.FragmentAgentHomeBinding;
import com.example.thirdapplication.ui.Events.EventFragment; // Import EventFragment
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AgentHomeFragment extends Fragment {

    private FragmentAgentHomeBinding binding; // Correct binding for the fragment's layout
    private RecyclerView.Adapter adapterOngoing;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextView agentNameTextView;
    private TextView logoutTxtView; // Logout TextView

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAgentHomeBinding.inflate(inflater, container, false);
        agentNameTextView = binding.agentName; // Reference the TextView
        logoutTxtView = binding.LogoutTxtView; // Reference Logout TextView

        loadUserData(); // Load user data from Firestore
        initRecyclerView(); // Initialize RecyclerView
        setupLogout(); // Setup logout functionality
        setupEventButton(); // Setup event button click listener

        return binding.getRoot(); // Return the root view from the binding
    }

    private void initRecyclerView() {
        ArrayList<OngoingDomain> ongoingDomainArrayList = new ArrayList<>();
        ongoingDomainArrayList.add(new OngoingDomain("Koppal\n Karnataka", "August 26, 2012", 100, "ongoing1"));
        ongoingDomainArrayList.add(new OngoingDomain("Bagalkot\n Karnataka", "July 13, 2003", 100, "ongoing1"));
        binding.viewOngoingRecycle.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapterOngoing = new OngoingAdapter(ongoingDomainArrayList);
        binding.viewOngoingRecycle.setAdapter(adapterOngoing);
    }

    private void loadUserData() {
        String uid = mAuth.getCurrentUser().getUid(); // Get current user's UID
        db.collection("User").document(uid).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Fetch the "name" field from Firestore
                            String name = document.getString("name");
                            if (name != null) {
                                agentNameTextView.setText("Hi " + name + "!");
                            }
                        } else {
                            Log.d("AgentHomeFragment", "No such document");
                        }
                    } else {
                        Log.d("AgentHomeFragment", "get failed with ", task.getException());
                    }
                });
    }

    private void setupLogout() {
        // Set click listener on the logout TextView
        logoutTxtView.setOnClickListener(v -> {
            // Log the user out
            FirebaseAuth.getInstance().signOut();

            // Show a toast message
            Toast.makeText(getActivity(), "Logged out successfully", Toast.LENGTH_SHORT).show();

            // Navigate back to LoginActivity
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
            startActivity(intent);

            // Finish the current fragment/activity
            getActivity().finish();
        });
    }

    private void setupEventButton() {
        // Set click listener for the event button (eventBtn)
        binding.eventBtn.setOnClickListener(v -> {
            // Perform fragment transaction to replace AgentHomeFragment with EventFragment
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.nav_host_fragment_activity_main, new EventFragment()); // Replace with EventFragment
            transaction.addToBackStack(null); // Add to back stack so the user can navigate back
            transaction.commit(); // Commit the transaction
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Clear binding to avoid memory leaks
    }
}
