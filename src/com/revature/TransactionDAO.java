package com.revature;

import java.sql.SQLException;
import java.util.List;

public interface TransactionDAO {
    public void singleAccount(User customer, String trans, double amount) throws SQLException;
    public void transferInitial(User sender, String recipient, double amount);
    public void chargeInitial(User sender, String recipient, double amount);
    public List<Transaction> transactionHistory(User client);
    public List<Transaction> PendingTransactions(User customer);
    public void PendingTransaction_handler(User customer, int transactionID);
    public List<Transaction> UnNotifiedTransactions(User customer);
    public void transaction_notified_handler(User customer, int transactionID);
}
