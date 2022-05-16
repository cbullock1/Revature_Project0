package com.revature;

import java.sql.SQLException;

public class AccountDAOFactory {

    private static AccountDAO accountDAO;

    private AccountDAOFactory(){

    }

    public static AccountDAO getAccountDAO() throws SQLException {

        if(accountDAO == null)
            accountDAO = new AccountDAOImp();

        return accountDAO;
    }

}
