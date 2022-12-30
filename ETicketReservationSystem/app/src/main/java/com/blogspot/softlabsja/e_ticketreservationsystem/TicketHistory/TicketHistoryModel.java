package com.blogspot.softlabsja.e_ticketreservationsystem.TicketHistory;

public class TicketHistoryModel {
    String busNo,startingPoint,endingPoint,ticketPrice,seat,bookedDate,validity;

    public TicketHistoryModel(String busNo, String startingPoint, String endingPoint, String ticketPrice, String seat, String bookedDate, String validity) {
        this.busNo = busNo;
        this.startingPoint = startingPoint;
        this.endingPoint = endingPoint;
        this.ticketPrice = ticketPrice;
        this.seat = seat;
        this.bookedDate = bookedDate;
        this.validity = validity;
    }

    public String getBusNo() {
        return busNo;
    }

    public void setBusNo(String busNo) {
        this.busNo = busNo;
    }

    public String getStartingPoint() {
        return startingPoint;
    }

    public void setStartingPoint(String startingPoint) {
        this.startingPoint = startingPoint;
    }

    public String getEndingPoint() {
        return endingPoint;
    }

    public void setEndingPoint(String endingPoint) {
        this.endingPoint = endingPoint;
    }

    public String getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(String ticketPrice) {
        this.ticketPrice = ticketPrice;
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
