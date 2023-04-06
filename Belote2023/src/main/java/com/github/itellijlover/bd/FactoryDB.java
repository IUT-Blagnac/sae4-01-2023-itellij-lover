package com.github.itellijlover.bd;

import java.sql.Connection;

public class FactoryDB {

    protected Connection connection = SingleConnexion.getUniqueInstance();

}
