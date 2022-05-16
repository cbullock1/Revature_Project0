package com.revature;

import java.text.DecimalFormat;

public class Transaction {
    private int TransactionID;
    private String Sender;
    private String transType;
    private String recipient;
    private double amount;
    private String status;
    private String timestamp;
    private String notify;

    public String getNotify() {
        return notify;
    }

    public Transaction(){

    }

    public Transaction(int transactionID, String sender, String transType, String recipient, double amount, String status, String timestamp, String notify) {
        TransactionID = transactionID;
        Sender = sender;
        this.transType = transType;
        this.recipient = recipient;
        this.amount = amount;
        this.status = status;
        this.timestamp = timestamp;
        this.notify = notify;
    }

    @Override
    public String toString() {
        DecimalFormat money = new DecimalFormat("#.00");
        return "Transaction{" +
                "TransactionID=" + TransactionID +
                ", Sender='" + Sender + '\'' +
                ", transType='" + transType + '\'' +
                ", recipient='" + recipient + '\'' +
                ", amount=" + money.format(amount) +
                ", status='" + status + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
        //String format = "[%12d|%10s|%15s|%10s|%13s|%10s|%10s]";
        //return String.format(format, TransactionID, Sender, transType, recipient, money.format(amount), status, timestamp, notify);
    }

    public int getTransactionID() {
        return TransactionID;
    }

    public void setTransactionID(int transactionID) {
        TransactionID = transactionID;
    }

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
