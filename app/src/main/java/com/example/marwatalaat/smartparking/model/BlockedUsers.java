package com.example.marwatalaat.smartparking.model;

/**
 * Created by MarwaTalaat on 1/12/2017.
 */

public class BlockedUsers {
    private String email;
    private String carNumber;

    public BlockedUsers(String email, String carNumber) {
        this.email = email;
        this.carNumber = carNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }
}
