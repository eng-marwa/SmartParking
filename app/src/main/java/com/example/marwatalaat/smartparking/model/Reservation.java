package com.example.marwatalaat.smartparking.model;

import java.util.Date;

/**
 * Created by MarwaTalaat on 11/30/2016.
 */

public class Reservation {

    private int placeId;
    private Date startDate;
    private String status;
    private int counter;
    private String floor;
    private String room;

    public int getCounter() {
        return counter;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public Reservation() {
    }



    public Reservation(int placeId, Date startDate, String status, int counter, String floor, String room) {
        this.placeId = placeId;
        this.startDate = startDate;
        this.status = status;
        this.counter = counter;
        this.floor = floor;
        this.room = room;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
