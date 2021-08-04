package com.example.fkom_car_booking.model;

public class Users {
    private String userID, StaffName, StaffID, type, StaffEmail, Password;

    public Users() {

    }

    public Users(String userID, String StaffName, String StaffID, String type, String StaffEmail, String Password) {
        this.userID = userID;
        this.StaffName = StaffName;
        this.StaffID = StaffID;
        this.type = type;
        this.StaffEmail = StaffEmail;
        this.Password = Password;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getStaffName() {
        return StaffName;
    }

    public void setStaffName(String staffName) {
        StaffName = staffName;
    }

    public String getStaffID() {
        return StaffID;
    }

    public void setStaffID(String staffID) {
        StaffID = staffID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStaffEmail() {
        return StaffEmail;
    }

    public void setStaffEmail(String staffEmail) {
        StaffEmail = staffEmail;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
