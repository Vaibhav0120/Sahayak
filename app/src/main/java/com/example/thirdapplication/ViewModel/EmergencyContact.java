package com.example.thirdapplication.ViewModel;

public class EmergencyContact {
    private String title;
    private String phoneNumber;
    private boolean isDefault;
    private String id; // Assuming there's an ID field

    // Default constructor required for Firebase
    public EmergencyContact() {
    }

    public EmergencyContact(String title, String phoneNumber, boolean isDefault) {
        this.title = title;
        this.phoneNumber = phoneNumber;
        this.isDefault = isDefault;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
