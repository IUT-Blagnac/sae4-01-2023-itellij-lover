package com.github.itellijlover;

import com.github.itellijlover.db.DatabaseSingleton;
import com.github.itellijlover.vue.Fenetre;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Belote {

	public static Statement statement;

	public static void main(String[] args) {
		Connection connection;

		try {
			// Se connecter à la base de données
			connection = DatabaseSingleton.getInstance().getConnection();
			statement = connection.createStatement();

			// Importer le schéma SQL à partir d'un fichier
			importSQL(connection, new File("create.sql"));

			// Interface graphique
			Fenetre f = new Fenetre(statement);
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		} catch (SQLException e) {
			Fenetre.afficherErreur("Impossible de se connecter à la base de données. Vérifiez qu'une autre instance du logiciel n'est pas déjà ouverte.");
			System.out.println(e.getMessage()); // pour le développeur
			System.exit(0);
		} catch (Exception e) {
			Fenetre.afficherErreur("Erreur lors de l'initialisation du logiciel. Vérifiez votre installation Java et vos droits d'accès sur le dossier AppData.");
			System.out.println(e.getMessage()); // pour le développeur
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
			Fenetre.afficherErreur("Erreur lors de l'import du schéma SQL : " + e.getMessage());
		}
	}
}
