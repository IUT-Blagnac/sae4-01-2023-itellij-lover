package com.github.itellijlover.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class oui {

    private static oui instance;

    private final Statement statement;

    private oui() {
        // créer le répertoire pour stocker les fichiers de la base de données s'il n'existe pas encore
        String FOLDER = System.getenv("APPDATA") + "\\jBelote";
        System.out.println("Dossier de stockage : " + FOLDER);
        if (!new File(FOLDER).isDirectory()) {
            if (!new File(FOLDER).mkdir()) {
                System.out.println("Impossible de créer le dossier de stockage, le logiciel ne peut pas fonctionner sans, fermeture imminente.");
                System.exit(1);
            }
        }

        // se connecte à la base de données
        String DB_URL = "jdbc:hsqldb:file:%s\\belote;shutdown=true";
        String dbUrl = String.format(DB_URL, FOLDER);
        try {
            Connection connection = DriverManager.getConnection(dbUrl, "sa", "");
            statement = connection.createStatement();
            instance = this;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static oui getInstance() {
        if (instance == null) new oui();
        return instance;
    }

    public Statement getStatement() {
        return statement;
    }

}
