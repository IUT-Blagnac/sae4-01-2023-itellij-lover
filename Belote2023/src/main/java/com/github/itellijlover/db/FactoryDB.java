package com.github.itellijlover.db;

import java.sql.Statement;

public class FactoryDB {

    protected final Statement statement;

    public FactoryDB() {
        statement = oui.getInstance().getStatement();
    }
}
