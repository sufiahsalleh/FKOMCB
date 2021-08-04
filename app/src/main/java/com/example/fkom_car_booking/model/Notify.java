package com.example.fkom_car_booking.model;

public class Notify {
        String NotifyID;
        String BookID;
        String userID;

        public Notify(String bookID, String userID, String NotifyID) {
            BookID = bookID;
            this.userID = userID;
            this.NotifyID = NotifyID;
        }

        public Notify() {

        }

        public String getNotifyID() {
            return NotifyID;
        }

        public void setNotifyID(String notifyID) {
            NotifyID = notifyID;
        }

        public String getBookID() {
            return BookID;
        }

        public void setBookID(String bookID) {
            BookID = bookID;
        }

        public String getUserID() {
            return userID;
        }

        public void setUserID(String userID) {
            this.userID = userID;
        }

}
