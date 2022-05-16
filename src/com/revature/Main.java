package com.revature;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {

    static UserDAO userDAO;
    static AccountDAO accountDAO;
    static TransactionDAO transactionDAO;
    static {
        try {
            userDAO = UserDAOFactory.getUserDAO();
            accountDAO = AccountDAOFactory.getAccountDAO();
            transactionDAO = TransactionDAOFactory.getTransactionDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void EmployeeActions(User employee) throws SQLException {
        boolean session = true;
        Scanner scanner = new Scanner(System.in);
        while (session) {
            System.out.println("As an employee of Revature Bank we are responcible for the banks administrative tasks");
            System.out.println("Please select one of the following options");
            System.out.println("Type 1 to view all the current customers");
            System.out.println("Type 2 to view all of the accounts for a specific customer");
            System.out.println("Type 3 to change the status of the customer account");
            System.out.println("Type anything else to loggout");
            System.out.println();
            try {
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        List<User> users = userDAO.getUsers(employee);
                        for (User customer : users)
                            System.out.println(customer.toString());
                        System.out.println();
                        break;
                    case 2:
                        System.out.println("Please type the userName of the customer");
                        String userName = scanner.next();
                        List<Account> accounts = accountDAO.getAllUserAccounts(userName);
                        for(Account account : accounts)
                            System.out.println(account.toString());
                        System.out.println();
                        break;
                    case 3:
                        System.out.println("Which Customer would you like to change the status of?");
                        System.out.println("Please enter the Customer's ID");
                        int customer_id;
                        try {
                            customer_id = scanner.nextInt();
                            userDAO.StatusUpdate(employee, customer_id);
                        } catch (InputMismatchException in) {
                            System.err.println("Please enter a numeric input that is >= 1");
                            System.out.println();
                            String handle = scanner.next();
                        }
                        break;
                    default:
                        System.out.println("Logging Out.....");
                }
            }catch (InputMismatchException IMP){
                session = false;
                String clear = scanner.next();
                System.out.println("Logging Out........");
            }
        }
    }

    public static void CustomerActions(User customer) throws SQLException {
        boolean session = true;
        Scanner scanner = new Scanner(System.in);
        while (session){

            System.out.println("Please select one of the follow options");
            System.out.println("1. Create bank account");
            System.out.println("2. View List of your Accounts");
            System.out.println("3. View Balance of a specific account");
            System.out.println("4. Deposit funds into an Account");
            System.out.println("5. Withdraw funds from an Account");
            System.out.println("6. Charge an Another User Account");
            System.out.println("7. Send a Payment to another User Account");
            System.out.println("8. Check for Pending Transactions");
            System.out.println("9. Check for Unacknowledged Transactions");
            System.out.println("Any other input will equate to a Log out request");
            try {
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        System.out.println("Please enter an initial balance");
                        try {
                            double balance = scanner.nextDouble();
                            accountDAO.createAccount(customer, balance);
                            transactionDAO.singleAccount(customer, "Create", balance);
                        }catch (InputMismatchException bal){
                            System.err.println("Input for this option must be an Double");
                            System.out.println();
                            String clear = scanner.next();
                        }
                        break;
                    case 2:
                        List<Account> accounts = accountDAO.getAllUserAccounts(customer.getUsername());
                        for (Account account : accounts)
                            System.out.println(account.toString());
                        System.out.println();
                        break;
                    case 3:
                        System.out.println("Please input the ID of the account you wish to see the balance of");
                        try {
                            int id = scanner.nextInt();
                            Account account = accountDAO.findAccountByID(customer,id);
                            if(account != null) {
                                DecimalFormat money = new DecimalFormat("#.00");
                                System.out.println("The balance of the " + account.getAccountID() + " account is $" + money.format(account.getBalance()) + ".");
                                System.out.println();
                            }
                        }catch (InputMismatchException inE){
                            System.err.println("The input of this option must be an integer");
                            System.out.println();
                            String clear = scanner.next();
                        }
                        break;
                    case 4:
                        System.out.println("Please enter the ID of the Account you wish to deposit money into");
                        try {
                            int DepositID = scanner.nextInt();
                            System.out.println("Please enter the amount you wish to deposit");
                            double DepositAmount = scanner.nextDouble();
                            if(accountDAO.deposit(customer, DepositID, DepositAmount))
                                transactionDAO.singleAccount(customer, "Deposit", DepositAmount);
                        }catch (InputMismatchException miss){
                            System.err.println("Input for this operation must be an integer");
                            System.out.println();
                            String clear = scanner.next();
                        }
                        break;
                    case 5:
                        System.out.println("Please enter the ID of the Account you wish to withdraw money from");
                        try {
                            int WithdrawID = scanner.nextInt();
                            System.out.println("Please enter the amount you wish to withdraw");
                            double WithdrawAmount = scanner.nextDouble();
                            if(accountDAO.withdraw(customer, WithdrawID, WithdrawAmount))
                                transactionDAO.singleAccount(customer, "Withdraw", WithdrawAmount);
                        }catch (InputMismatchException miss) {
                            System.err.println("Input for this operation must be an integer");
                            System.out.println();
                            String clear = scanner.next();
                        }
                        break;
                    case 6:
                        System.out.println("Please enter the user name of the person you wish to charge");
                        String recipient = scanner.next();
                        System.out.println("Please enter the amount you wish to charge");
                        try {
                            double amount = scanner.nextDouble();
                            transactionDAO.chargeInitial(customer, recipient, amount);
                        }catch (InputMismatchException miss) {
                            System.err.println("Input for this operation must be an double");
                            System.out.println();
                            String clear = scanner.next();
                        }
                        break;
                    case 7:
                        System.out.println("Please enter the user name of the person you wish to Transfer money to");
                        String rec = scanner.next();
                        System.out.println("Please enter the amount you wish to transfer");
                        try {
                            double amt = scanner.nextDouble();
                            transactionDAO.transferInitial(customer,rec, amt);
                        }catch (InputMismatchException miss) {
                            System.err.println("Input for this operation must be an Double");
                            System.out.println();
                            String clear = scanner.next();
                        }
                        break;
                    case 8:
                        List<Transaction> transactions = transactionDAO.PendingTransactions(customer);
                        for (Transaction tran : transactions) {
                            System.out.println(tran.toString());
                        }
                        if (transactions.size() > 0) {
                            System.out.println("You have some pending transaction would you like to handle one of them");
                            System.out.println("Y/N. Anything other input will be taken as N");
                            String userCase = scanner.next();
                            switch (userCase.toUpperCase()) {
                                case "Y":
                                    System.out.println("Please enter the ID of the Transaction you wish to handle");
                                    try {
                                        int transId = scanner.nextInt();
                                        transactionDAO.PendingTransaction_handler(customer, transId);
                                    }catch (InputMismatchException miss) {
                                        System.err.println("Input for this operation must be an integer");
                                        System.out.println();
                                        String clear = scanner.next();
                                    }
                                    break;
                                default:
                                    System.out.println("Returning to main options");
                            }
                        }
                        break;

                    case 9:
                        transactions = transactionDAO.UnNotifiedTransactions(customer);
                        for (Transaction tran : transactions) {
                            System.out.println(tran.toString());
                        }
                        if (transactions.size() > 0) {
                            System.out.println("You have some Unacknowledged transactions would you like to handle one of them");
                            System.out.println("Y/N. Anything other input will be taken as N");
                            String userCase = scanner.next();
                            switch (userCase.toUpperCase()) {
                                case "Y":
                                    System.out.println("Please enter the ID of the Transaction you wish to Acknowledge");
                                    try {
                                        int transId = scanner.nextInt();
                                        transactionDAO.transaction_notified_handler(customer, transId);
                                    }catch (InputMismatchException miss) {
                                        System.out.println("Input for this operation must be an integer");
                                        System.out.println();
                                        String clear = scanner.next();
                                    }
                                    break;
                                default:
                                    System.out.println("Returning to main options");
                            }
                        }
                        break;

                    default:
                        session = false;
                }
            }catch (InputMismatchException in){
                session = false;
                String clear = scanner.next();
            }

        }

    }

    public static void LoggedIn(User account) throws SQLException {
        System.out.println("Welcome " + account.getFirstName() + " " + account.getLastName());
        System.out.println();
        if(account.getStatus().equals("APPROVED")) {
            if (account.getUsertype().equals("Employee"))
                EmployeeActions(account);
            else
                CustomerActions(account);
        }
        else if(account.getStatus().equals("PENDING")){
            System.out.println("Your account is still pending....Please wait for an employee to review your account");
        }
        else
            System.out.println("Your account has been REJECTED!");


        System.out.println("Logged out");


    }
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("Welcome to Revature Online Banking!");
            System.out.println("Please type : LOGIN to login or REGISTER to register");
            System.out.println();
            String choice = scanner.next();

            User UserAccount = null;

            switch (choice.toUpperCase()) {
                case "LOGIN":
                    System.out.println("Please type your Username");
                    String usern = scanner.next();
                    System.out.println("Please type you Password");
                    String pass = scanner.next();
                    UserAccount = userDAO.login(usern, pass);
                    if (UserAccount != null)
                        LoggedIn(UserAccount);
                    break;
                case "REGISTER":
                    UserAccount = userDAO.Register();
                    System.out.println("Do you wish to continue to account managemnt? Y/N");
                    String cont = scanner.next();
                    switch (cont.toUpperCase()) {
                        case "Y":
                            LoggedIn(UserAccount);
                            break;
                        default:
                            System.out.println("Thank you for signing up with us!");
                    }
                    break;
                default:
                    System.out.println("Leaving Revature Banking");
                    running = false;
            }
            System.out.println();
        }
        ConnectionFactory.closeConnection();
    }
}
