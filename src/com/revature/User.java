package com.revature;

import java.text.DecimalFormat;
import java.util.List;

public class User {

    private int ID;
    private String Username;
    private String LastName;
    private String FirstName;
    private String Password;
    private String Usertype;
    private String Status;
    private List<Account> accounts;

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public User(){

    }
    public User(int ID, String Username, String LastName, String FirstName, String Password, String UserType, String Status){
        this.ID = ID;
        this.Username = Username;
        this.LastName = LastName;
        this.FirstName = FirstName;
        this.Password = Password;
        this.Usertype = UserType;
        this.Status = Status;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setUsertype(String usertype) {
        Usertype = usertype;
    }

    public int getID() {
        return ID;
    }

    public String getUsername() {
        return Username;
    }

    public String getLastName() {
        return LastName;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getPassword() {
        return Password;
    }

    public String getUsertype() {
        return Usertype;
    }

    public String getStatus() {
        return Status;
    }

    @Override
    public String toString() {
        return "User{" +
                "ID=" + ID +
                ", Username='" + Username + '\'' +
                ", LastName='" + LastName + '\'' +
                ", FirstName='" + FirstName + '\'' +
                //", Password='" + Password + '\'' +
                ", Usertype='" + Usertype + '\'' +
                ", Status='" + Status + '\'' +
                '}';
        //String format = "[ %2d | %2s | %2s | %2s | %2s | %2s | %2s ]";
        //return String.format(format, ID, Username, LastName, FirstName, Password, Usertype, Status);
    }
}
