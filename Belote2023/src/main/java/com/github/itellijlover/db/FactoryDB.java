package com.github.itellijlover.db;

import java.sql.Statement;

public class FactoryDB {

    protected final Statement statement;

    protected FactoryDB() {
        statement = DatabaseConnection.getInstance().getStatement();
    }

}
