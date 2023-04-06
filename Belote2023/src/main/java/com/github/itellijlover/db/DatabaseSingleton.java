package com.github.itellijlover.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseSingleton {

    private static volatile DatabaseSingleton instance;

    private static final String DB_URL = "jdbc:hsqldb:file:%s\\belote;shutdown=true";

    private static final String FOLDER = System.getenv("APPDATA") + "\\jBelote";

    private final Connection connection;

    private DatabaseSingleton() throws SQLException {
        // Créer le répertoire pour stocker les fichiers de la base de données s'il n'existe pas encore
        System.out.println("Dossier de stockage : " + FOLDER);
        if (!new File(FOLDER).isDirectory()) {
            if (!new File(FOLDER).mkdir()) {
                System.out.println("Impossible de créer le dossier de stockage, fonctionnement impossible, fermeture imminente.");
                System.exit(1);
            }
        }

        // Se connecter à la base de données
        String dbUrl = String.format(DB_URL, FOLDER);
        connection = DriverManager.getConnection(dbUrl, "sa", "");
    }

    public static DatabaseSingleton getInstance() throws SQLException {
        if (instance == null) {
            synchronized (DatabaseSingleton.class) {
                if (instance == null) {
                    instance = new DatabaseSingleton();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

}
