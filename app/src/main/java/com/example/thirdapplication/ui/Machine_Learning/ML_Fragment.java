package com.example.thirdapplication.ui.Machine_Learning;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.thirdapplication.Adapter.WeatherListAdapter;
import com.example.thirdapplication.R;
import com.example.thirdapplication.ViewModel.MainViewModel;
import com.example.thirdapplication.databinding.FragmentMlBinding;

public class ML_Fragment extends Fragment {

    private FragmentMlBinding binding;
    private MainViewModel mainViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMlBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize the ViewModel
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // Initialize RecyclerView
        initRecyclerView();

        // Handle backBtn click using NavController
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.navigation_home); // Use the correct action or destination ID for your home fragment
            }
        });

        return root;
    }

    private void initRecyclerView() {
        // Set up RecyclerView with a LinearLayoutManager and the WeatherListAdapter
        binding.WeatherView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.WeatherView.setAdapter(new WeatherListAdapter(mainViewModel.loadWeather()));  // Assuming loadWeather() loads the list of weather data
        binding.WeatherView.setNestedScrollingEnabled(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Clean up binding to avoid memory leaks
        binding = null;
    }
}
