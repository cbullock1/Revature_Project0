package com.revature;

import java.sql.SQLException;

public class TransactionDAOFactory {
    private static TransactionDAO transactionDAO;

    private TransactionDAOFactory(){}

    public static TransactionDAO getTransactionDAO() throws SQLException {

        if(transactionDAO == null)
            transactionDAO = new TransactionDAOImp();

        return transactionDAO;
    }
}
