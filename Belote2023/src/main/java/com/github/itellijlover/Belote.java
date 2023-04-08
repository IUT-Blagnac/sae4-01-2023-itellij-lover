package com.github.itellijlover;

import com.github.itellijlover.db.DatabaseConnection;
import com.github.itellijlover.view.Window;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import javax.swing.JFrame;

public class Belote {

	public static void main(String[] args) {
		try {
			// se connecter à la base de données
			Statement statement = DatabaseConnection.getInstance().getStatement();

			// importer le schéma SQL à partir d'un fichier
			importSQL(statement, new File("create.sql"));

			// interface graphique
			Window window = new Window();
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		} catch (Exception e) {
			Window.afficherErreur("Erreur lors de l'initialisation du logiciel. Vérifiez votre installation Java et vos droits d'accès sur le dossier AppData.");
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Importe un schéma SQL dans la base de données.
	 * @param st L'objet Statement utilisé pour exécuter les requêtes.
	 * @param in Le fichier SQL contenant le schéma à importer.
	 */
	public static void importSQL(Statement st, File in) {
		try (Scanner s = new Scanner(in)) {
			s.useDelimiter("(;(\r)?\n)|(--\n)");
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
		} catch (SQLException | FileNotFoundException e) {
			Window.afficherErreur("Erreur lors de l'import du schéma SQL : " + e.getMessage());
			e.printStackTrace();
		}
	}
}
