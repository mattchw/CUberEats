package com.example.tommylee.cubereats;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class Order {

    /**
     * imgurl :
     * name : Med Can
     * time : 07:00-22:00
     */

    private String customerID;
    private String driverID;
    private String customerName;
    private String driverName;
    private ArrayList<String> mealID = new ArrayList<>();
    private boolean isPaid;
    private String documentID;
    private GeoPoint driverCoordinate;
    private GeoPoint customerCoordinate;

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public GeoPoint getCustomerCoordinate() {
        return customerCoordinate;
    }

    public void setCustomerCoordinate(GeoPoint customerCoordinate) {
        this.customerCoordinate = customerCoordinate;
    }

    public GeoPoint getDriverCoordinate() {
        return driverCoordinate;
    }

    public void setDriverCoordinate(GeoPoint driverCoordinate) {
        this.driverCoordinate = driverCoordinate;
    }

    public boolean getIsPaid() { return isPaid; }

    public void setIsPaid(boolean paid) {
        isPaid = paid;
    }

    public void setDocumentID(String documentID) { this.documentID = documentID; }

    public String getDocumentID() { return documentID; }

    public ArrayList<String> getMealID() {
        return mealID;
    }

    public void setMealID(ArrayList<String> mealID) {
        this.mealID = mealID;
    }
}
