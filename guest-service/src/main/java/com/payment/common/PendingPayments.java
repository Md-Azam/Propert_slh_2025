package com.payment.common;

public class PendingPayments {
    private int pendingPayments;
    //private double overallDue;

    public int getPendingPayments() {
        return pendingPayments;
    }

    public void setPendingPayments(int pendingPayments) {
        this.pendingPayments = pendingPayments;
    }


//    public double getOverallDue() {
//        return overallDue;
//    }
//
//    public void setOverallDue(double overallDue) {
//        this.overallDue = overallDue;
//    }

    public PendingPayments(int pendingPayments) {
        super();
        this.pendingPayments = pendingPayments;
        //this.overallDue = overallDue;
    }

    public PendingPayments() {
        super();

    }

}
