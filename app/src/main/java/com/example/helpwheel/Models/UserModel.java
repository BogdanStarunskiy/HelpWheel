package com.example.helpwheel.Models;

public class UserModel {
    String username; int consumptionPer100Km, fuelTankCapacity;
    public UserModel(String username, int consumptionPer100Km, int fuelTankCapacity){
        this.username = username;
        this.consumptionPer100Km = consumptionPer100Km;
        this.fuelTankCapacity = fuelTankCapacity;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getConsumptionPer100Km() {
        return consumptionPer100Km;
    }

    public void setConsumptionPer100Km(int consumptionPer100Km) {
        this.consumptionPer100Km = consumptionPer100Km;
    }

    public int getFuelTankCapacity() {
        return fuelTankCapacity;
    }

    public void setFuelTankCapacity(int fuelTankCapacity) {
        this.fuelTankCapacity = fuelTankCapacity;
    }
}
