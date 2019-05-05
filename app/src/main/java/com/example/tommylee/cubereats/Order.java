package com.example.tommylee.cubereats;

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

    public boolean getPaid() { return isPaid; }

    public void setPaid(boolean paid) {
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
