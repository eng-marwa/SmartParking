package com.example.marwatalaat.smartparking.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MarwaTalaat on 11/9/2016.
 */

public class User implements Parcelable{
    private String name;
    private String phoneNumber;
    private String carNumber;

    public User() {
    }

    public User(String name, String phoneNumber, String carNumber) {

        this.name = name;
        this.phoneNumber = phoneNumber;
        this.carNumber = carNumber;
    }

    protected User(Parcel in) {
        name = in.readString();
        phoneNumber = in.readString();
        carNumber = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phoneNumber);
        dest.writeString(carNumber);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

   }
