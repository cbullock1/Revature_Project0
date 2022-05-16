package com.revature;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAOImp implements AccountDAO{
    Connection connection;

    public AccountDAOImp() throws SQLException {
        this.connection = ConnectionFactory.getConnection();
    }

    @Override
    public void createAccount(User customer, double initialB) {
        if(initialB >= 0) {
            try {
                String sql = "INSERT INTO accounts (balance, userName) VALUES(?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setDouble(1, initialB);
                preparedStatement.setString(2, customer.getUsername());

                int rows = preparedStatement.executeUpdate();

                if (rows != 0) {
                    System.out.println("ACCOUNT CREATION SUCCESSFUL");
                }

            } catch (SQLException sqlE) {
                sqlE.printStackTrace();
            }
        }
        else {
            System.err.println("ACCOUNT CREATION FAILED: Initial Balance cannot be Negative");
        }
        System.out.println();
    }

    @Override
    public boolean deposit(User customer, int accountID, double amount) {
        if(accountID >= 1) {
            if (amount >= 0) {
                try {
                    Account inital = findAccountByID(customer, accountID);
                    if (inital != null) {
                        if (inital.getUser().equals(customer.getUsername())) {
                            double balance = inital.getBalance();
                            double total = balance + amount;
                            String sql = "UPDATE accounts SET balance = " + total + " WHERE accountID = " + accountID;
                            Statement statement = connection.createStatement();
                            int rows = statement.executeUpdate(sql);
                            if (rows == 1) {
                                System.out.println("DEPOSIT SUCCESSFUL");
                                return true;
                            }
                        } else {
                            System.err.println("DEPOSIT FAILED: You cannot deposit funds in account you don't own");
                        }
                    } else {
                        System.err.println("DEPOSIT FAILED: Account not found");
                    }
                } catch (SQLException sqlE) {
                    sqlE.printStackTrace();
                }
            } else {
                System.err.println("DEPOSIT FAILED: Deposit amount cannot be negative");
            }
        }
        else {
            System.err.println("DEPOSIT FAILED: Account ID cannot be less than 1");
        }
        System.out.println();
        return false;
    }

    @Override
    public boolean withdraw(User customer, int accountID, double amount) {
        if(accountID >= 1) {
            if (amount >= 0) {
                try {
                    Account target = findAccountByID(customer,accountID);
                    if (target != null) {
                        if (target.getUser().equals(customer.getUsername())) {
                            double balance = target.getBalance();
                            if (balance > amount) {
                                double total = balance - amount;
                                String sql = "UPDATE accounts SET balance = " + total + " WHERE accountID  = " + accountID;
                                Statement statement = connection.createStatement();
                                int rows = statement.executeUpdate(sql);
                                if (rows == 1) {
                                    System.out.println("WITHDRAWAL SUCCESSFUL");
                                    return true;
                                }
                            } else {
                                System.err.println("WITHDRAWAL FAILED: Balance is less than withdraw!");
                            }
                        } else {
                            System.err.println("WITHDRAWAL FAILED: You cannot withdraw money from an account that doesn't belong to you");
                        }
                    } else {
                        System.err.println("WITHDRAWAL FAILED: Account not found");
                    }
                } catch (SQLException sqlE) {
                    sqlE.printStackTrace();
                }
            } else {
                System.err.println("WITHDRAWAL FAILED: Amount cannot be negative");
            }
        }
        else {
            System.err.println("WITHDRAWAL FAILED: Account ID cannot be less than 1");
        }
        System.out.println();
        return false;
    }

    @Override
    public List<Account> getAllUserAccounts(String customer) {
        try {
            String sql = "SELECT * FROM accounts WHERE userName = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, customer);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Account> accounts = new ArrayList<>();
            while (resultSet.next())
                accounts.add(new Account(resultSet.getInt(1), resultSet.getDouble(2), resultSet.getString(3)));

            return accounts;

        }catch (SQLException sqlE){

        }
        return null;
    }

    @Override
    public Account findAccountByID(User customer, int accountID) {
        if(accountID >= 1) {
            try {
                String sql = "SELECT * FROM accounts WHERE accountID = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, accountID);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    if(resultSet.getString("userName").equals(customer.getUsername()))
                        return new Account(resultSet.getInt(1), resultSet.getDouble(2), resultSet.getString(3));
                    else {
                        System.err.println("ACCOUNT ERROR: You do not own this account please double check your Account ID(s)");
                    }
                }
                else {
                    System.err.println("ACCOUNT NOT FOUND");
                    System.out.println();
                }

            } catch (SQLException sqlE) {
                sqlE.printStackTrace();
            }
        }
        else {
            System.err.println("ACCOUNT SEARCH FAILED: Account ID cannot be less than 1");
        }
        System.out.println();
        return null;
    }
}
