package com.github.itellijlover;

import com.github.itellijlover.db.DatabaseConnection;
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

	public static void main(String[] args) {
		try {
			// se connecter à la base de données
			Connection connection = DatabaseConnection.getInstance().getStatement().getConnection();

			// importer le schéma SQL à partir d'un fichier
			importSQL(connection, new File("create.sql"));

			// interface graphique
			Fenetre fenetre = new Fenetre();
			fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
