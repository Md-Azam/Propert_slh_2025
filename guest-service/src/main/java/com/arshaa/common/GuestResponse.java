package com.arshaa.common;

import com.arshaa.entity.Guest;

public class GuestResponse {

    private int amountPaid;
    private String message;
    private int paymentId;
    private String guestId ;


    public GuestResponse(int amountPaid, String message, int paymentId, String guestId) {
        this.amountPaid = amountPaid;
        this.message = message;
        this.paymentId = paymentId;
        this.guestId = guestId ;
    }

    public GuestResponse() {
    }

    public int getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(int amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public String getGuestId(){
        return guestId ;


    }
    public void setGuessId(String guestId) {
        this.guestId = guestId;
    }

}
