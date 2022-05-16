package com.revature;

import java.util.List;

public interface AccountDAO {

    public boolean createAccount(User customer, double initialB);
    public boolean deposit(User customer, int accountID, double amount);
    public boolean withdraw(User customer, int accountID, double amount);
    public boolean checkAmounts(User customer, double amount);
    public List<Account> getAllUserAccounts(String customer);
    public Account findAccountByID(User customer, int accountID);

}
