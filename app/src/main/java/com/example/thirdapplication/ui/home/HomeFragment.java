package com.example.thirdapplication.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.thirdapplication.Adapter.MainIconListAdapter;
import com.example.thirdapplication.R;
import com.example.thirdapplication.ViewModel.MainViewModel;
import com.example.thirdapplication.databinding.FragmentHomeBinding;
import com.example.thirdapplication.LoginActivity;
import com.example.thirdapplication.ui.Machine_Learning.ML_Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private MainViewModel mainViewModel;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize the ViewModel
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // Set the user email
        setUserEmail();

        // Initialize RecyclerView
        initRecyclerView();

        // Set up the Logout button
        setupLogoutButton();

        // Set up the Weather Card click listener
        setupWeatherCardClickListener();

        return root;
    }

    @SuppressLint("SetTextI18n")
    private void setUserEmail() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            binding.userEmailTxtView.setText(email != null ? email : "No Email");
        } else {
            binding.userEmailTxtView.setText("Guest User");
        }
    }

    private void setupLogoutButton() {
        binding.LogoutTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sign out from Firebase
                mAuth.signOut();

                // Redirect to LoginActivity
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    private void initRecyclerView() {
        // Ensure RecyclerView is properly initialized with a LinearLayoutManager and an adapter
        binding.mainIconView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.mainIconView.setAdapter(new MainIconListAdapter(mainViewModel.loadData()));
        binding.mainIconView.setNestedScrollingEnabled(true);
    }

    private void setupWeatherCardClickListener() {
        binding.WeatherCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtain the NavController from the NavHostFragment
                NavController navController = NavHostFragment.findNavController(HomeFragment.this);

                // Navigate to ML_Fragment using the NavController
                navController.navigate(R.id.action_homeFragment_to_mlFragment);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Clean up binding to avoid memory leaks
        binding = null;
    }
}
