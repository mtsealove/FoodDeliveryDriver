package com.mtsealove.github.food_delivery_driver.Entity;

public class OrderDetail {
    String OrderTime, Location, CurrentLocation;

    public OrderDetail(String orderTime, String location, String currentLocation) {
        OrderTime = orderTime;
        Location = location;
        CurrentLocation = currentLocation;
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

    public String getCurrentLocation() {
        return CurrentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        CurrentLocation = currentLocation;
    }
}
