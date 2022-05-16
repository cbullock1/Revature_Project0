package com.revature;

import java.text.DecimalFormat;

public class Account {
    private int accountID;
    private double balance;
    private String user;

    public Account(){

    }

    public Account(int accountID, double balance, String user) {
        this.accountID = accountID;
        this.balance = balance;
        this.user = user;
    }

    @Override
    public String toString() {
        DecimalFormat money = new DecimalFormat("#.00");
        return "Account{" +
                "accountID=" + accountID +
                ", balance=" + money.format(balance) +
                ", user='" + user + '\'' +
                '}';
        //String format = "| %12d | %10s | %15s | %10s | %13s | %10s | %10s |";
        //return String.format(format, accountID, money.format(balance), user);
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
