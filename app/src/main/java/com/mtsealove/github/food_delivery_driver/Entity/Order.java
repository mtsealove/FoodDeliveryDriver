package com.mtsealove.github.food_delivery_driver.Entity;

public class Order {
    String OrderTime, Location;
    int OrderID;

    public Order(String orderTime, String location, int orderID) {
        OrderTime = orderTime;
        Location = location;
        OrderID = orderID;
    }

    public String getOrderTime() {
        return OrderTime;
    }

    public void setOrderTime(String orderTime) {
        OrderTime = orderTime;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int orderID) {
        OrderID = orderID;
    }
}
