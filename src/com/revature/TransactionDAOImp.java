package com.revature;

import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class TransactionDAOImp implements TransactionDAO{
    Connection connection;
    static AccountDAO accountDAO;

    static {
        try {
            accountDAO = AccountDAOFactory.getAccountDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public TransactionDAOImp() throws SQLException {
        this.connection = ConnectionFactory.getConnection();
    }


    private List<Transaction> makeTransactionList(PreparedStatement preparedStatement, List<Transaction> transactions) throws SQLException {
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            int transId = resultSet.getInt(1);
            String sender = resultSet.getString(2);
            String type = resultSet.getString(3);
            String recipient = resultSet.getString(4);
            double amount = resultSet.getDouble(5);
            String status = resultSet.getString(6);
            String date = resultSet.getString(7);
            String notify = resultSet.getString(8);
            transactions.add(new Transaction(transId,sender,type,recipient,amount,status,date,notify));
        }
        return transactions;
    }


    @Override
    public List<Transaction> transactionOverview(User Employee) {
        try {
            String sql = "SELECT * FROM transactions";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            List<Transaction> transactions = new ArrayList<>();
            if(Employee.getUsertype().equals("Employee"))
                return makeTransactionList(preparedStatement, transactions);

        }catch (SQLException sqlE){
            sqlE.printStackTrace();
        }
        return null;
    }

    @Override
    public void singleAccount(User customer, String trans, double amount) throws SQLException {
        String sql = "INSERT INTO transactions (sender, type, recipient, amount, status, notify) VALUES(?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,customer.getUsername());
        preparedStatement.setString(2,trans);
        preparedStatement.setString(3,customer.getUsername());
        preparedStatement.setDouble(4,amount);
        preparedStatement.setString(5,"Approved");
        preparedStatement.setString(6,"Notified");
        int rows = preparedStatement.executeUpdate();
        if(rows != 0) {
            System.out.println("TRANSACTION RECORDED SUCCESSFULLY");
        }
        else {
            System.err.println("TRANSACTION RECORDING ERROR: Something went wrong with entering your Transaction into the Records table");
        }
        System.out.println();
    }

    @Override
    public void transferInitial(User sender, String recipient, double amount) {

        Scanner scanner = new Scanner(System.in);
        boolean exists = false;
        try{
            String search = "SELECT (userName) FROM Users where userName = ?";
            PreparedStatement searching = connection.prepareStatement(search);
            searching.setString(1,recipient);
            ResultSet check = searching.executeQuery();
            if (check.next())
                exists = true;

        }catch (SQLException s){
            s.printStackTrace();
        }
        if(exists) {
            System.out.println("What account ID do you wish to withdrawal from for the transfer");
            boolean passed = false;
            while (!passed) {
                try {
                    int accountID = scanner.nextInt();
                    if (accountID <= 0) {
                        System.err.println("TRANSFER FAILED: ID cannot be less than 1");
                        return;
                    }

                    if (accountDAO.withdraw(sender, accountID, amount)) {
                        passed = true;
                        try {
                            String sql = "INSERT INTO transactions (sender,type,recipient, amount)" +
                                    "VALUES(?,'Payment', ?, ?)";
                            PreparedStatement preparedStatement = connection.prepareStatement(sql);
                            preparedStatement.setString(1, sender.getUsername());
                            preparedStatement.setString(2, recipient);
                            preparedStatement.setDouble(3, amount);

                            int rows = preparedStatement.executeUpdate();
                            if (rows != 0) {
                                System.out.println("PAYMENT SENT");
                            }

                        } catch (SQLException sqlE) {
                            sqlE.printStackTrace();
                        }
                    } else {
                        System.err.println("ACCOUNT NOT FOUND PLEASE REVIEW THE ACCOUNT'S ID");
                    }

                } catch (InputMismatchException iMiss) {
                    System.err.println("Please enter INTEGER ID OF Account you wish to transfer money to");
                    String clear = scanner.next();
                }
            }
        }
        else {
            System.err.println("TRASNFER FAILED: Recipient Account does not Exist");
        }
        System.out.println();
    }

    @Override
    public void chargeInitial(User sender, String recipient, double amount) {
        try {

            String search = "SELECT (userName) FROM Users where userName = ?";
            PreparedStatement searching = connection.prepareStatement(search);
            searching.setString(1,recipient);
            ResultSet check = searching.executeQuery();

            if(check.next()) {

                if (amount > 0) {
                    String sql = "INSERT INTO transactions (sender,type,recipient, amount)" +
                            "VALUES(?,'Charge', ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, sender.getUsername());
                    preparedStatement.setString(2, recipient);
                    preparedStatement.setDouble(3, amount);

                    int rows = preparedStatement.executeUpdate();
                    if (rows != 0) {
                        System.out.println("CHARGE SENT");
                    }
                } else {
                    System.err.println("CHARGE FAILED: CAN NOT CHARGE ANOTHER USER NEGATIVE AMOUNTS");
                }
            }
            else {
                System.err.println("CHARGE FAILED: Recipient Account does not Exist");
            }

        }catch (SQLException sqlE){
            sqlE.printStackTrace();
        }
        System.out.println();

    }

    @Override
    public List<Transaction> transactionHistory(User client) {
        try {
            String sql = "SELECT * FROM transactions WHERE sender = ? OR  recipient = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, client.getUsername());
            preparedStatement.setString(2, client.getUsername());
            List<Transaction> transactions = new ArrayList<>();
            return makeTransactionList(preparedStatement, transactions);

        }catch (SQLException sqlE){
            sqlE.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Transaction> PendingTransactions(User customer) {
        try {
            String sql = "SELECT * FROM transactions WHERE recipient = ? AND  status = 'Pending'";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, customer.getUsername());
            List<Transaction> transactions = new ArrayList<>();
            return makeTransactionList(preparedStatement, transactions);

        }catch (SQLException sqlE){
            sqlE.printStackTrace();
        }
        return null;
    }

    @Override
    public void PendingTransaction_handler(User customer, int transactionID) {
        if(transactionID < 1){
            System.err.println("TRANSACTION FAILED: Transaction ID cannot be less than 1");
            return;
        }
        try{
            String sql = "SELECT * FROM transactions WHERE recipient = ? AND transID = ? AND status = 'Pending'";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,customer.getUsername());
            preparedStatement.setInt(2, transactionID);
            ResultSet resultSet = preparedStatement.executeQuery();
            Scanner scanner = new Scanner(System.in);

            if(resultSet.next()){

                String type = resultSet.getString("type");
                double amount = resultSet.getDouble("amount");
                String sender = resultSet.getString("sender");
                System.out.println("The transaction selected is a " + type + " from " + sender + " for $" + amount);
                System.out.println("Please type Y or N to Accept or Decline the transaction");
                String responce = scanner.next();
                boolean proceed = false;
                Statement statement = connection.createStatement();
                while (!proceed) {
                    try {
                        switch (responce.toUpperCase()) {
                            case "Y":
                                if (type.equals("Payment")) {
                                    int account = 0;
                                    do {
                                        System.out.println("Please input the Account you wish to input the funds into");
                                        account = scanner.nextInt();
                                    }while (!accountDAO.deposit(customer, account, amount));
                                } else {
                                    int account = 0;
                                    do {
                                        System.out.println("Please input the Account you wish to withdrawal the funds from");
                                        account = scanner.nextInt();
                                    }while (!accountDAO.withdraw(customer, account, amount));
                                }
                                sql = "UPDATE transactions SET status = 'Approved' where transID = " + transactionID;
                                statement.executeUpdate(sql);
                                proceed = true;
                                break;
                            case "N":
                                sql = "UPDATE transactions SET status = 'Rejected' where transID = " + transactionID;
                                statement.executeUpdate(sql);
                                System.out.println("The transaction was rejected sucessfully!");
                                proceed = true;
                                break;
                            default:
                                System.err.println("Your responce needs to be in format Y or N");

                        }
                    }catch (InputMismatchException Imiss){
                        System.err.println("PLEASE ENTER AN INTEGER IN TRANSACTION ID");
                        System.out.println();
                        String clear = scanner.next();
                    }
                }

            }
            else {
                System.out.println("PENDING TRANSACTION NOT FOUND");
            }


        }catch (SQLException sqlE){
            sqlE.printStackTrace();
        }

        System.out.println();

    }

    @Override
    public List<Transaction> UnNotifiedTransactions(User customer) {

        try {
            List<Transaction> transactions = new ArrayList<>();
            String sql = "SELECT * FROM transactions WHERE sender = ? AND status != 'Pending' AND notify = 'Not Notified'";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,customer.getUsername());
            return makeTransactionList(preparedStatement, transactions);

        }catch (SQLException sqlE){
            sqlE.printStackTrace();
        }

        return null;
    }

    @Override
    public void transaction_notified_handler(User customer, int transactionID) {

        if(transactionID < 1){
            System.err.println("TRANSACTION NOTIFICATION FAILURE: Transaction ID cannot be less than 1");
            return;
        }

        try {
            Scanner scanner = new Scanner(System.in);
            String sql = "SELECT * FROM transactions WHERE transID = ? AND sender = ? AND status != 'Pending' AND notify = 'Not Notified'" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,transactionID);
            preparedStatement.setString(2,customer.getUsername());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {

                if (customer.getUsername().equals(resultSet.getString("sender"))){

                    if(!resultSet.getString("status").equals("Pending")){
                        String sqlUpdate = "UPDATE transactions SET notify = 'Notified' WHERE transID = ?";
                        preparedStatement = connection.prepareStatement(sqlUpdate);
                        preparedStatement.setInt(1,transactionID);
                        //Write cases for accepted and rejected for not notified
                        try {
                            if (resultSet.getString("type").equals("Payment")) {

                                if (resultSet.getString("status").equals("Approved")) {
                                    System.out.println("Your Payment was Approved");
                                } else {
                                    System.out.println("Your Payment was Rejected!");
                                    int refundAccount = 0;
                                    do {
                                        System.out.println("Please select the account you wish to redeposit the money into");
                                        refundAccount = scanner.nextInt();
                                    }while (!accountDAO.deposit(customer, refundAccount, resultSet.getDouble("amount")));
                                }
                                int rows = preparedStatement.executeUpdate();
                                if (rows != 0)
                                    System.out.println("Notification complete");
                            } else if (resultSet.getString("type").equals("Charge")) {

                                if (resultSet.getString("status").equals("Approved")) {
                                    System.out.println("Your Charge was Approved!");
                                    int paymentAccount = 0;
                                    do {
                                        System.out.println("Please select the account you wish to Deposit the payment into");
                                        paymentAccount = scanner.nextInt();
                                    }while (!accountDAO.deposit(customer, paymentAccount, resultSet.getDouble("amount")));
                                } else {
                                    System.out.println("Your Charge was Rejected");
                                }
                                int rows = preparedStatement.executeUpdate();
                                if (rows != 0)
                                    System.out.println("Notification complete");
                            }
                        }catch (InputMismatchException Imiss){
                            System.err.println("PLEASE ENTER INTEGER FOR THE ACCOUNT ID");
                        }

                    }else {
                        System.err.println("TRANSACTION IS STILL PENDING");
                    }
                }else{
                    System.err.println("YOU DO NOT HAVE THE PERMISSIONS TO ALTER THE NOTIFICATION");
                }
            }
            else {
                System.err.println("TRANSACTION NOT FOUND");
            }
        }catch (SQLException sqlE){
            sqlE.printStackTrace();
        }
        System.out.println();
    }
}
