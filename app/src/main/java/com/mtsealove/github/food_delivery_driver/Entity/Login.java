package com.mtsealove.github.food_delivery_driver.Entity;

public class Login {
    String ID, Name, ManagerID;

    public Login(String ID, String name, String managerID) {
        this.ID = ID;
        Name = name;
        ManagerID = managerID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getManagerID() {
        return ManagerID;
    }

    public void setManagerID(String managerID) {
        ManagerID = managerID;
    }

    @Override
    public String toString() {
        return "Login{" +
                "ID='" + ID + '\'' +
                ", Name='" + Name + '\'' +
                ", ManagerID='" + ManagerID + '\'' +
                '}';
    }
}
