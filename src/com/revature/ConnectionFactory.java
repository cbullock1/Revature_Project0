package com.revature;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class ConnectionFactory {

    private static Connection connection;

    private ConnectionFactory(){
        this.connection = null;
    }

    public static Connection getConnection() throws SQLException {

        if(connection == null){
            ResourceBundle resourceBundle = ResourceBundle.getBundle("com/revature/dbConfig");
            String Url = resourceBundle.getString("url");
            String user = resourceBundle.getString("user");
            String pass = resourceBundle.getString("pass");
            connection = DriverManager.getConnection(Url, user, pass);

            //After opening the connection run custom table creaton
            String sql = "CALL sp_CreateTables_AND_Set_User_Key";
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);

        }
        return connection;
    }

    public static void closeConnection() throws SQLException {
        System.out.println("The connection is closing...............");
        String sql = "CALL sp_Clear_Tables";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
        connection.close();
        System.out.println("Connection is closed");
    }

}
