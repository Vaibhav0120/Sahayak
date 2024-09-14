package com.example.thirdapplication.ui.dashboard;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.NavOptions;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thirdapplication.R;
import com.example.thirdapplication.ViewModel.EmergencyContact;
import com.example.thirdapplication.Adapter.EmergencyContactsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private EmergencyContactsAdapter adapter;
    private List<EmergencyContact> contacts;
    private List<EmergencyContact> filteredContacts;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private EditText searchBox;
    private ProgressBar progressBar; // Added this line

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        recyclerView = view.findViewById(R.id.recycler_view_emergency_contacts);
        searchBox = view.findViewById(R.id.editTextTextPersonName); // Get reference to search box
        progressBar = view.findViewById(R.id.progress_bar); // Initialize the ProgressBar

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        contacts = new ArrayList<>();
        filteredContacts = new ArrayList<>();
        adapter = new EmergencyContactsAdapter(filteredContacts, new EmergencyContactsAdapter.OnContactClickListener() {
            @Override
            public void onContactClick(EmergencyContact contact) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE);
                } else {
                    dialPhoneNumber(contact.getPhoneNumber());
                }
            }

            @Override
            public void onDeleteClick(EmergencyContact contact) {
                if (!contact.isDefault()) {
                    deleteContact(contact);
                } else {
                    Toast.makeText(getContext(), "Cannot delete default contact", Toast.LENGTH_SHORT).show();
                }
            }
        });
        recyclerView.setAdapter(adapter);

        // Initialize Firebase instances
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Load default contacts and user contacts
        loadDefaultContacts();
        loadUserContacts();

        // Set up the "Add Contact" button click listener
        Button addContactButton = view.findViewById(R.id.button_add_contact);
        addContactButton.setOnClickListener(v -> showAddContactDialog());

        // Add TextWatcher to search box for filtering contacts
        searchBox.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterContacts(s.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {
            }
        });

        // Set up the back button click listener
        ImageView backBtn = view.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);

            // Create NavOptions to clear back stack
            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.navigation_home, true) // Pop up to home fragment
                    .build();

            // Navigate to HomeFragment with NavOptions
            navController.navigate(R.id.navigation_home, null, navOptions);
        });
    }

    // Show progress bar
    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    // Hide progress bar
    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    // Load default emergency contacts and add them to the list
    private void loadDefaultContacts() {
        contacts.clear();
        contacts.add(new EmergencyContact("Call Disaster Related Info", "112", true));
        contacts.add(new EmergencyContact("Call Police", "100", true));
        contacts.add(new EmergencyContact("Call Ambulance", "102", true));
        contacts.add(new EmergencyContact("Call Search And Rescue", "115", true));
        contacts.add(new EmergencyContact("Call Electricity Emergency", "123", true));
        contacts.add(new EmergencyContact("Call Child Helpline", "1098", true));
        contacts.add(new EmergencyContact("Call Accident Helpline", "108", true));
        adapter.notifyDataSetChanged();
    }

    // Load user-specific contacts from Firestore
    private void loadUserContacts() {
        showProgressBar(); // Show the progress bar

        String userId = getUserId();
        if (userId != null) {
            db.collection("User").document(userId).collection("EmergencyContacts")
                    .get()
                    .addOnCompleteListener(task -> {
                        hideProgressBar(); // Hide the progress bar when loading is complete

                        if (task.isSuccessful()) {
                            List<EmergencyContact> userContacts = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                EmergencyContact contact = document.toObject(EmergencyContact.class);
                                if (contact != null) {
                                    userContacts.add(contact);
                                }
                            }
                            contacts.clear();
                            loadDefaultContacts(); // Load default contacts again
                            contacts.addAll(userContacts);
                            filterContacts(searchBox.getText().toString()); // Apply filter to the loaded contacts
                        } else {
                            Toast.makeText(getContext(), "Failed to load contacts.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            hideProgressBar(); // Hide the progress bar if there is no user ID
        }
    }

    // Dial a phone number
    private void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PHONE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (!contacts.isEmpty()) {
                    dialPhoneNumber(contacts.get(0).getPhoneNumber()); // Example to dial first contact
                }
            } else {
                Toast.makeText(getContext(), "Permission denied to make calls", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Show dialog for adding a new contact
    private void showAddContactDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_contact, null);

        final EditText nameEditText = dialogView.findViewById(R.id.edit_text_name);
        final EditText phoneEditText = dialogView.findViewById(R.id.edit_text_phone);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Emergency Contact")
                .setView(dialogView)
                .setPositiveButton("Add", (dialog, which) -> {
                    String name = nameEditText.getText().toString();
                    String phoneNumber = phoneEditText.getText().toString();
                    if (!name.isEmpty() && !phoneNumber.isEmpty()) {
                        EmergencyContact newContact = new EmergencyContact(name, phoneNumber, false);
                        addNewContact(newContact);
                    } else {
                        Toast.makeText(getContext(), "Please enter both name and phone number", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Delete a contact from Firestore
    private void deleteContact(EmergencyContact contact) {
        String userId = getUserId();
        if (userId != null && contact.getId() != null) {
            db.collection("User").document(userId).collection("EmergencyContacts")
                    .document(contact.getId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        contacts.remove(contact);
                        filterContacts(searchBox.getText().toString()); // Reapply filter after deletion
                        Toast.makeText(getContext(), "Contact deleted.", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to delete contact.", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    // Add a new contact to Firestore
    private void addNewContact(EmergencyContact contact) {
        String userId = getUserId();
        if (userId != null) {
            DocumentReference userContactsRef = db.collection("User").document(userId).collection("EmergencyContacts").document();
            contact.setId(userContactsRef.getId());
            userContactsRef.set(contact)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("DashboardFragment", "Contact added successfully");
                        contacts.add(contact);
                        filterContacts(searchBox.getText().toString()); // Reapply filter after addition
                        Toast.makeText(getContext(), "Contact added.", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("DashboardFragment", "Failed to add contact", e);
                        Toast.makeText(getContext(), "Failed to add contact.", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    // Get the current user's ID
    private String getUserId() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser != null ? currentUser.getUid() : null;
    }

    // Filter contacts based on search query
    private void filterContacts(String query) {
        filteredContacts.clear();
        if (TextUtils.isEmpty(query)) {
            filteredContacts.addAll(contacts);
        } else {
            for (EmergencyContact contact : contacts) {
                if (contact.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredContacts.add(contact);
                }
            }
        }
        adapter.notifyDataSetChanged(); // Notify adapter about data change
    }

    private static final int REQUEST_CALL_PHONE = 1;
}
