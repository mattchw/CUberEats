package com.example.tommylee.cubereats;

public class Order {

    /**
     * imgurl :
     * name : Med Can
     * time : 07:00-22:00
     */

    private String customerID;
    private String driverID;

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

}
