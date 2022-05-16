package com.revature;

import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserDAOImp implements UserDAO{
    Connection connection;
    public UserDAOImp() throws SQLException {
        this.connection = ConnectionFactory.getConnection();
    }

    @Override
    public User login(String userName, String password) {
        try {
            String sql = "SELECT * FROM users WHERE userName = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userName);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            if (password.equals(resultSet.getString(5))) {
                int id = resultSet.getInt(1);
                String LastName = resultSet.getString(2);
                String FirstName = resultSet.getString(3);
                String uName = resultSet.getString(4);
                String pass = resultSet.getString(5);
                String UserType = resultSet.getString(6);
                String Status = resultSet.getString(7);
                return new User(id, uName, LastName, FirstName, pass, UserType, Status);
            } else {
                System.out.println("Invalid Password!");
                System.out.println();
                return null;

            }
        }catch (SQLException sqlE){
            System.err.println("Invalid UserName!");
            System.out.println();
        }

        return null;
    }

    @Override
    public User Register() {
        boolean error_catch = false;
        boolean reg_success;
        System.out.println("Thank you for choosing to register at the Bank of Register");
        System.out.println();
        String user = null;
        do {
            reg_success = true;
            error_catch = false;
            try {
                String sql = "INSERT INTO users (LastName, FirstName, userName, password, accountType) VALUES(?,?,?,?,'Customer')";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                Scanner scanner = new Scanner(System.in);
                System.out.println("Please enter your Last Name");
                preparedStatement.setString(1, scanner.next());
                System.out.println("Please enter your First Name");
                preparedStatement.setString(2, scanner.next());
                System.out.println("Please enter a UserName");
                user = scanner.next();
                preparedStatement.setString(3, user);
                System.out.println("Please enter a Password");
                preparedStatement.setString(4, scanner.next());
                int rows = preparedStatement.executeUpdate();
                if (rows == 0) {
                    System.out.println("Account Creation Failed please try again");
                    System.out.println();
                }
            } catch (SQLException sqlE) {
                System.err.println("There was an error in Account Registration");
                System.err.println("Please re-enter information with a different UserName");
                System.out.println();
                error_catch = true;
            }finally {
                if(error_catch)
                    reg_success = false;
                else
                    System.out.println("Account Registration Successful");
            }
        }while (!reg_success);

        try {
            String sql = "Select * from users where userName = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,user);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int id = resultSet.getInt(1);
            String LastName = resultSet.getString(2);
            String FirstName = resultSet.getString(3);
            String uName = resultSet.getString(4);
            String pass = resultSet.getString(5);
            String UserType = resultSet.getString(6);
            String Status = resultSet.getString(7);
            return new User(id,uName,LastName,FirstName,pass,UserType, Status);

        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> getUsers(User employee) throws SQLException{
        if(employee.getUsertype().equals("Employee")) {
            String sql = "Select * From users WHERE accountType = 'Customer'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            List<User> customers = new ArrayList<>();
            while (resultSet.next()) {
                customers.add(new User(
                        resultSet.getInt(1),
                        resultSet.getString(4),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getString(7)
                ));

            }

            return customers;
        }
        return null;
    }

    @Override
    public void StatusUpdate(User employee, int customerId) {
        if(customerId >= 1) {
            try {
                if (employee.getUsertype().equals("Employee")) {
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("Would you like this customer to be Approved or Rejected");
                    String sql = "UPDATE users SET status = ? WHERE id = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    boolean check = false;
                    while (!check) {
                        String option = scanner.next();
                        switch (option.toUpperCase()) {
                            case "APPROVED":
                            case "REJECTED":
                                preparedStatement.setString(1, option);
                                check = true;
                                break;
                            default:
                                System.out.println("Invalid Input. Please enter Accepted or Rejected as typed");
                        }
                    }
                    preparedStatement.setInt(2, customerId);
                    int rows = preparedStatement.executeUpdate();
                    if (rows != 0)
                        System.out.println("Customer Status updated sucessfully");
                    else
                        System.out.println("OOOPs....Something went wrong");

                    System.out.println();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else {
            System.err.println("STATUS UPDATE FAILED: Customer ID cannot be less than 1");
        }
    }

}
