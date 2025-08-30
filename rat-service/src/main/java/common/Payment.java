package common;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDate;
import java.util.Date;

public class Payment {

    private String paymentMethod;
    private double dueAmount;
    private double amountPaid;
    private String transactionId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private java.util.Date transactionDate = new java.util.Date(System.currentTimeMillis());

    LocalDate checkInDate;
    private LocalDate checkOutDate;

    // private double dueDuringOnBoard ;
    // private String occupancyType[] = { "Daily" , "Monthly" , "Regular" };
    private String occupancyType;
    private double currentRent;

    // Fields taking reference from guest-Master Data .
    private String bedId;
    private double securityDeposit = 5000;
    private String guestId; // (f k) from guestId
    double defaultRent;


    public Payment(String paymentMethod, double dueAmount, double amountPaid, String transactionId, Date transactionDate, LocalDate checkInDate, LocalDate checkOutDate, String occupancyType, double currentRent, String bedId, double securityDeposit, String guestId, double defaultRent) {
        this.paymentMethod = paymentMethod;
        this.dueAmount = dueAmount;
        this.amountPaid = amountPaid;
        this.transactionId = transactionId;
        this.transactionDate = transactionDate;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.occupancyType = occupancyType;
        this.currentRent = currentRent;
        this.bedId = bedId;
        this.securityDeposit = securityDeposit;
        this.guestId = guestId;
        this.defaultRent = defaultRent;
    }

    public Payment() {
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(double dueAmount) {
        this.dueAmount = dueAmount;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getOccupancyType() {
        return occupancyType;
    }

    public void setOccupancyType(String occupancyType) {
        this.occupancyType = occupancyType;
    }

    public double getCurrentRent() {
        return currentRent;
    }

    public void setCurrentRent(double currentRent) {
        this.currentRent = currentRent;
    }

    public String getBedId() {
        return bedId;
    }

    public void setBedId(String bedId) {
        this.bedId = bedId;
    }

    public double getSecurityDeposit() {
        return securityDeposit;
    }

    public void setSecurityDeposit(double securityDeposit) {
        this.securityDeposit = securityDeposit;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public double getDefaultRent() {
        return defaultRent;
    }

    public void setDefaultRent(double defaultRent) {
        this.defaultRent = defaultRent;
    }
}
