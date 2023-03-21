package com.github.itellijlover;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Belote {

	private static final String DB_URL = "jdbc:hsqldb:file:%s\\belote;shutdown=true";

	public static void main(String[] args) {
		Connection connection;
		Statement statement;

		try {
			// Créer le répertoire pour stocker les fichiers de la base de données s'il n'existe pas encore
			String folder = System.getenv("APPDATA") + "\\jBelote";
			System.out.println("Dossier de stockage : " + folder);
			if (!new File(folder).isDirectory()) {
				if (!new File(folder).mkdir()) {
					System.out.println("Imppossible de créer le dossier de stockage, fonctionnement impossible, fermeture imminente.");
					System.exit(1);
				}
			}

			// Se connecter à la base de données
			String dbUrl = String.format(DB_URL, folder);
			connection = DriverManager.getConnection(dbUrl, "sa", "");
			statement = connection.createStatement();

			// Importer le schéma SQL à partir d'un fichier
			importSQL(connection, new File("create.sql"));

			// Interface graphique
			Fenetre f = new Fenetre(statement);
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Impossible de se connecter à la base de données. Vérifiez qu'une autre instance du logiciel n'est pas déjà ouverte.");
			System.out.println(e.getMessage());
			System.exit(0);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erreur lors de l'initialisation du logiciel. Vérifiez votre installation Java et vos droits d'accès sur le dossier AppData.");
			System.out.println(e.getMessage());
			System.exit(0);
		}
	}

	public static void importSQL(Connection conn, File in) {
		try (Scanner s = new Scanner(in)) {
			s.useDelimiter("(;(\r)?\n)|(--\n)");
			try (Statement st = conn.createStatement()) {
				while (s.hasNext()) {
					String line = s.next();
					if (line.startsWith("/*!") && line.endsWith("*/")) {
						int i = line.indexOf(' ');
						line = line.substring(i + 1, line.length() - " */".length());
					}

					if (line.trim().length() > 0) {
						st.execute(line);
					}
				}
			}
		} catch (SQLException | FileNotFoundException e) {
			System.out.println("Erreur lors de l'import du schéma SQL : " + e.getMessage());
		}
	}
}
