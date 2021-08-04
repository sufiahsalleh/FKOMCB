package com.example.fkom_car_booking.model;

public class Booking {

    private String BookID;
    String bookingStatus;
    String userID;
    String dateFrom;
    String dateTo;
    String timeIn;
    String timeOut;
    String workorder;
    String programme;
    String address;
    String drivername;
    Integer nopassanger;
    String NotifyStatus;

    public Booking(String userID, String bookingStatus, String BookID, String dateFrom, String dateTo, String timeIn, String timeOut, String workorder, String programme, String address, String drivername, Integer nopassanger, String NotifyStatus) {
        this.BookID = BookID;
        this.bookingStatus = bookingStatus;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.workorder = workorder;
        this.programme = programme;
        this.address = address;
        this.drivername = drivername;
        this.nopassanger = nopassanger;
        this.userID = userID;
        this.NotifyStatus = NotifyStatus;
    }

    public Booking() {

    }

    public String getNotifyStatus() {
        return NotifyStatus;
    }

    public void setNotifyStatus(String notifyStatus) {
        NotifyStatus = notifyStatus;
    }

    public String getBookID() {
        return BookID;
    }

    public void setBookID(String bookID) {
        BookID = bookID;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public String getWorkorder() {
        return workorder;
    }

    public void setWorkorder(String workorder) {
        this.workorder = workorder;
    }

    public String getProgramme() {
        return programme;
    }

    public void setProgramme(String programme) {
        this.programme = programme;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDrivername() {
        return drivername;
    }

    public void setDrivername(String drivername) {
        this.drivername = drivername;
    }

    public Integer getNopassanger() {
        return nopassanger;
    }

    public void setNopassanger(Integer nopassanger) {
        this.nopassanger = nopassanger;
    }
}
