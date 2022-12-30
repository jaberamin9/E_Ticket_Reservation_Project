package com.blogspot.softlabsja.e_ticketreservationadmin.BookedSeat;

public class BookedSeatModel {
    String id,busNo,pId,seat,bookedDate,validity;

    public BookedSeatModel(String id, String busNo, String pId, String seat, String bookedDate, String validity) {
        this.id = id;
        this.busNo = busNo;
        this.pId = pId;
        this.seat = seat;
        this.bookedDate = bookedDate;
        this.validity = validity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusNo() {
        return busNo;
    }

    public void setBusNo(String busNo) {
        this.busNo = busNo;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public String getBookedDate() {
        return bookedDate;
    }

    public void setBookedDate(String bookedDate) {
        this.bookedDate = bookedDate;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }
}
