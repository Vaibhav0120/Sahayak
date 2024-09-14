package com.example.thirdapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.thirdapplication.R;
import com.example.thirdapplication.ViewModel.EmergencyContact; // Ensure this is correct based on your package
import java.util.List;

public class EmergencyContactsAdapter extends RecyclerView.Adapter<EmergencyContactsAdapter.ViewHolder> {

    private final List<EmergencyContact> contacts;
    private final OnContactClickListener onContactClickListener;

    public EmergencyContactsAdapter(List<EmergencyContact> contacts, OnContactClickListener onContactClickListener) {
        this.contacts = contacts;
        this.onContactClickListener = onContactClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_emergency_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EmergencyContact contact = contacts.get(position);
        holder.titleTextView.setText(contact.getTitle());
        holder.numberTextView.setText(contact.getPhoneNumber()); // Updated method name

        holder.deleteButton.setVisibility(contact.isDefault() ? View.GONE : View.VISIBLE);

        holder.itemView.setOnClickListener(v -> onContactClickListener.onContactClick(contact));
        holder.deleteButton.setOnClickListener(v -> onContactClickListener.onDeleteClick(contact));
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public interface OnContactClickListener {
        void onContactClick(EmergencyContact contact);
        void onDeleteClick(EmergencyContact contact);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView numberTextView;
        ImageButton deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.emergency_title);
            numberTextView = itemView.findViewById(R.id.emergency_number);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}
