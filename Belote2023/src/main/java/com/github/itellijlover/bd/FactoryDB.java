package com.github.itellijlover.bd;

import com.github.itellijlover.DatabaseSingleton;

import java.sql.Connection;
import java.sql.Statement;

public class FactoryDB {

    protected static Statement statement;

    public FactoryDB() {
        try {
            Connection connection = DatabaseSingleton.getInstance().getConnection();
            statement = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
