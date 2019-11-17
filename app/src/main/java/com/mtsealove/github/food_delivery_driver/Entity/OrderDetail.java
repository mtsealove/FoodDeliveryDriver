package com.mtsealove.github.food_delivery_driver.Entity;

public class OrderDetail {
    String OrderTime, Location, CurrentLocation, StatusName;

    public OrderDetail(String orderTime, String location, String currentLocation, String statusName) {
        OrderTime = orderTime;
        Location = location;
        CurrentLocation = currentLocation;
        StatusName = statusName;
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

    public String getStatusName() {
        return StatusName;
    }

    public void setStatusName(String statusName) {
        StatusName = statusName;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "OrderTime='" + OrderTime + '\'' +
                ", Location='" + Location + '\'' +
                ", CurrentLocation='" + CurrentLocation + '\'' +
                ", StatusName='" + StatusName + '\'' +
                '}';
    }
}
