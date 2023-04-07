package com.github.itellijlover.controller;

import com.github.itellijlover.dao.EquipeDAO;
import com.github.itellijlover.dao.MatchDAO;
import com.github.itellijlover.dao.TournoiDAO;
import com.github.itellijlover.db.DatabaseConnection;
import com.github.itellijlover.model.Equipe;
import com.github.itellijlover.model.Match;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;

public class TournoiController {

	private final Statement statement; // TODO enlever quand toutes les DAO seront faites
	private final TournoiDAO tournoiDAO = TournoiDAO.getInstance(); // TODO à faire
	private final EquipeDAO equipeDAO = EquipeDAO.getInstance();
	private final MatchDAO matchDAO = MatchDAO.getInstance(); // TODO à faire

	private int id;
	private final String nom;
	private int statut_en_int;
	private String statut_en_string; // TODO inutile ?

	private List<Equipe> list_equipe;
	private List<Match> list_match;

	public TournoiController(String nom) {
		this.statement = DatabaseConnection.getInstance().getStatement(); // TODO enlever

		this.nom = nom;

		try {
			ResultSet rs = statement.executeQuery("SELECT * FROM tournoi WHERE nom_tournoi = '" + mysql_real_escape_string(nom) + "';");
			if (!rs.next()) {
				return;
			}
			this.id = rs.getInt("id_tournoi");

			this.statut_en_int = rs.getInt("statut");
			rs.close();

		} catch (SQLException e) {
			System.out.println("Erreur SQL : " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		switch (this.statut_en_int) {
			case 0:
				statut_en_string = "Inscription des joueurs";
				break;
			case 1:
				statut_en_string = "Génération des matchs";
				break;
			case 2:
				statut_en_string = "Matchs en cours";
				break;
			case 3:
				statut_en_string = "Terminé";
				break;
			default:
				statut_en_string = "Inconnu";
				break;
		}
	}

	public int getId() {
		return id;
	}

	public String getNom() {
		return nom;
	}

	public int getStatut() {
		return statut_en_int;
	}

	public String getStatutString() {
		return statut_en_string;
	}


	// Gestion tournois TODO mettre dans TournoiDAO.java

	public static void creerTournoi() {
		Statement statement = DatabaseConnection.getInstance().getStatement();

		String string = JOptionPane.showInputDialog(
				null,
				"Entrez le nom du tournoi",
				"Nom du tournoi",
				JOptionPane.PLAIN_MESSAGE);

		if (string != null && !string.isEmpty()) {
			try {
				string =  mysql_real_escape_string(string);
				if (string.length() < 3) {
					JOptionPane.showMessageDialog(null, "Le tournoi n'a pas été créé. Nom trop court.");
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (string.equals("")) {
				JOptionPane.showMessageDialog(null, "Le tournoi n'a pas été créé. Ne pas mettre de caractères spéciaux ou accents dans le nom");
			} else {
				ResultSet rs;
				try {
					rs = statement.executeQuery("SELECT id_tournoi FROM tournoi WHERE nom_tournoi = '" + string + "';");
					if (rs.next()) {
						JOptionPane.showMessageDialog(null, "Le tournoi n'a pas été créé. Un tournoi du même nom existe déjà");
						return;
					}
					System.out.println("INSERT INTO tournoi (id_tournoi, nb_matchs, nom_tournoi, statut) VALUES (NULL, 10, '"+string+"', 0)");
					statement.executeUpdate("INSERT INTO tournoi (id_tournoi, nb_matchs, nom_tournoi, statut) VALUES (NULL, 10, '"+string+"', 0)");
				} catch (SQLException e) {
					System.out.println("Erreur requete insertion nouveau tournoi:" + e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}

	public static void deleteTournoi(String nom) {
		Statement statement = DatabaseConnection.getInstance().getStatement();

		try {
			int idt;
			ResultSet rs = statement.executeQuery("SELECT id_tournoi FROM tournoi WHERE nom_tournoi = '" + mysql_real_escape_string(nom) + "';");
			rs.next();
			idt = rs.getInt(1);
			rs.close();
			System.out.println("ID du tournoi à supprimer:" + idt);
			statement.executeUpdate("DELETE FROM match   WHERE id_tournoi = " + idt);
			statement.executeUpdate("DELETE FROM equipe  WHERE id_tournoi = " + idt);
			statement.executeUpdate("DELETE FROM tournoi WHERE id_tournoi = " + idt);
		} catch (SQLException e) {
			System.out.println("Erreur suppression" + e.getMessage());
		} catch (Exception e) {
			System.out.println("Erreur inconnue");
		}
	}


	// Gestion équipes

	public void addEquipe() {
		int num_new_equipe = list_equipe.size()+1;

		boolean already_used = true;

		while (already_used) {
			already_used = false;
			for (Equipe equipe : list_equipe) {
				if (equipe.getId() == num_new_equipe) {
					already_used = true;
					num_new_equipe++;
					break;
				}
			}
		}

		Equipe equipe = new Equipe(0, num_new_equipe, id, "Joueur 1", "Joueur 2");

		equipeDAO.add(equipe);

		list_equipe.add(equipe);
	}

	public void updateEquipe(int index) {
		Equipe equipe = list_equipe.get(index);
		equipeDAO.update(equipe);
		list_equipe.remove(equipe);
		list_equipe.add(equipe);
	}

	public void deleteEquipe(int id_equipe) {
		for (Equipe e : list_equipe) {
			if (e.getId() == id_equipe) {
				equipeDAO.delete(e.getId());
				list_equipe.remove(e);
				break;
			}
		}
	}

	public Equipe getEquipe(int index) {
		if (list_equipe == null) getEquipes();
		return list_equipe.get(index);
	}

	public void getEquipes() {
		list_equipe = equipeDAO.getFromTournoi(id);
	}

	public int getNbEquipes() {
		if (list_equipe == null) getEquipes();
		return list_equipe.size();
	}


	// Gestion matchs TODO mettre dans MacthDAO.java

	public void genererMatchs() {
		int nbt = 1;

		System.out.println("Nombre d'équipes : " + getNbEquipes());
		System.out.println("Nombre de tours  : " + nbt);
		StringBuilder req = new StringBuilder("INSERT INTO match ( id_match, id_tournoi, num_tour, equipe1, equipe2, termine ) VALUES\n");
		Vector<Vector<Match>> ms;
		ms = getMatchsToDo(getNbEquipes(), nbt);
		int z = 1;
		char v = ' ';
		assert ms != null;
		for (Vector<Match> t : ms) {
			for (Match m:t) {
				req.append(v).append("(NULL,").append(id).append(", ").append(z).append(", ").append(m.getEq1()).append(", ").append(m.getEq2()).append(", 'non')");
				v = ',';
			}
			req.append("\n");
			z++;
		}
		System.out.println(req);
		try {
			statement.executeUpdate(req.toString());
			statement.executeUpdate("UPDATE tournoi SET statut=2 WHERE id_tournoi=" + id + ";");
			this.statut_en_int = 2;
		} catch(SQLException e) {
			System.out.println("Erreur validation équipes : " + e.getMessage());
		}
	}

	public void majMatch() {
		list_match = new Vector<>();
		try {
			ResultSet rs= statement.executeQuery("SELECT * FROM match WHERE id_tournoi="+ id + ";");
			while (rs.next()) {
				list_match.add(new Match(rs.getInt("id_match"), rs.getInt("equipe1"), rs.getInt("equipe2"), rs.getInt("score1"), rs.getInt("score2"), rs.getInt("num_tour")));
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void majMatch(int index) {
		String termine = (getMatch(index).getScore1() > 0 || getMatch(index).getScore2() > 0) ? "oui":"non";
		System.out.println(termine);
		String req = "UPDATE match SET equipe1='" + getMatch(index).getEq1() + "', equipe2='" + getMatch(index).getEq2() + "',  score1='" + getMatch(index).getScore1() + "',  score2='" +getMatch(index).getScore2() + "', termine='" + termine + "' WHERE id_match = " + getMatch(index).getId() + ";";
		try {
			statement.executeUpdate(req);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		majMatch();
	}

	public Match getMatch(int index) {
		if (list_match == null) majMatch();
		return list_match.get(index);
	}

	public static Vector<Vector<Match>> getMatchsToDo(int nbJoueurs, int nbTours) {
		if (nbTours  >= nbJoueurs) {
			System.out.println("Erreur tours < equipes");
			return null;
		}

		int[] tabJoueurs;
		if ((nbJoueurs % 2) == 1) {
			// Nombre impair de joueurs, on rajoute une �quipe fictive
			tabJoueurs   = new int[nbJoueurs+1];
			tabJoueurs[nbJoueurs] = -1;
			for (int z = 0; z < nbJoueurs;z++) {
				tabJoueurs[z] = z+1;
			}
			nbJoueurs++;
		} else {
			tabJoueurs   = new int[nbJoueurs];
			for (int z = 0; z < nbJoueurs;z++) {
				tabJoueurs[z] = z+1;
			}
		}

		boolean quitter;
		int i, increment = 1, temp;

		Vector<Vector<Match>> retour = new Vector<>();

		Vector<Match> vm;

		for (int r = 1; r <= nbTours;r++) {
			if (r > 1) {
				temp = tabJoueurs[nbJoueurs - 2];
				for (i = (nbJoueurs - 2) ; i > 0; i--) {
					tabJoueurs[i] = tabJoueurs[i-1];
				}
				tabJoueurs[0] = temp;
			}
			i       = 0;
			quitter = false;
			vm = new Vector<>();
			while (!quitter) {
				if (tabJoueurs[i] != -1 && tabJoueurs[nbJoueurs - 1  - i] != -1) {
					vm.add(new Match(tabJoueurs[i], tabJoueurs[nbJoueurs - 1  - i]));
				}
				// Sinon : Nombre impair de joueur, le joueur n'a pas d'adversaire
				i += increment;
				if (i >= nbJoueurs / 2) {
					quitter = true;
				}
			}
			retour.add(vm);
		}
		return retour;
	}

	public int getNbMatchs() {
		if (list_match == null) majMatch();
		return list_match.size();
	}


	// Gestion tours TODO mettre dans ToursDAO.java

	public void ajouterTour() {
		// Recherche du nombre de tours actuel
		int nbtoursav;
		if (getNbTours() >= (getNbEquipes() -1)) return;
		System.out.println("Eq:" + getNbEquipes() + "  tours" + getNbTours());
		try {
			ResultSet rs = statement.executeQuery("SELECT MAX (num_tour)  FROM match WHERE id_tournoi="+ id +"; ");
			rs.next();
			nbtoursav = rs.getInt(1);
			rs.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return;
		}
		System.out.println("Nombre de tours avant:" + nbtoursav);

		if (nbtoursav == 0) {
			Vector<Match> ms;

			ms = getMatchsToDo(getNbEquipes(), nbtoursav + 1).lastElement();

			StringBuilder req = new StringBuilder("INSERT INTO match ( id_match, id_tournoi, num_tour, equipe1, equipe2, termine ) VALUES\n");
			char v = ' ';
			for (Match m:ms) {
				req.append(v).append("(NULL,").append(id).append(", ").append(nbtoursav + 1).append(", ").append(m.getEq1()).append(", ").append(m.getEq2()).append(", 'non')");
				v = ',';
			}
			req.append("\n");

			try {
				statement.executeUpdate(req.toString());
			} catch(SQLException e) {
				System.out.println("Erreur ajout tour : " + e.getMessage());
			}
		} else {
			try {
				ResultSet rs;
				rs = statement.executeQuery("SELECT equipe, (SELECT count(*) FROM match m WHERE (m.equipe1 = equipe AND m.score1 > m.score2  AND m.id_tournoi = id_tournoi) OR (m.equipe2 = equipe AND m.score2 > m.score1 )) as matchs_gagnes FROM  (select equipe1 as equipe,score1 as score from match where id_tournoi=" + this.id + " UNION select equipe2 as equipe,score2 as score from match where id_tournoi=" + this.id + ") GROUP BY equipe  ORDER BY matchs_gagnes DESC;");

				ArrayList<Integer> ordreeq= new ArrayList<>();
				while (rs.next()) {
					ordreeq.add(rs.getInt("equipe"));
					System.out.println(rs.getInt(1) +" _ " + rs.getString(2));
				}
				System.out.println("Taille"+ordreeq.size());
				int i;
				boolean fini;
				StringBuilder req = new StringBuilder("INSERT INTO match ( id_match, id_tournoi, num_tour, equipe1, equipe2, termine ) VALUES\n");
				char v = ' ';
				while (ordreeq.size() > 1) {
					System.out.println("Taille " + ordreeq.size());
					int j=0;
					while (j<ordreeq.size()) {
						System.out.println(ordreeq.get(j));
						j++;
					}
					i=1;
					do {
						rs = statement.executeQuery("SELECT COUNT(*) FROM match m WHERE ( (m.equipe1 = " + ordreeq.get(0) + " AND m.equipe2 = " + ordreeq.get(i) + ") OR (m.equipe2 = " + ordreeq.get(0) + " AND m.equipe1 = " + ordreeq.get(i) + ")  )");
						rs.next();
						if (rs.getInt(1) > 0) {
							// Le match est déjà joué
							i++;
							fini = false;

						} else {
							fini = true;
							req.append(v).append("(NULL,").append(id).append(", ").append(nbtoursav + 1).append(", ").append(ordreeq.get(0)).append(", ").append(ordreeq.get(i)).append(", 'non')");
							System.out.println(ordreeq.get(0) + ", " +  ordreeq.get(i));
							ordreeq.remove(0);
							ordreeq.remove(i-1);
							v = ',';
						}
					} while(!fini);
				}
				System.out.println(req);
				statement.executeUpdate(req.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void supprimerTour() {
		int nbtoursav;
		try {
			ResultSet rs = statement.executeQuery("SELECT MAX (num_tour) FROM match WHERE id_tournoi="+ id +"; ");
			rs.next();
			nbtoursav = rs.getInt(1);
			rs.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return ;
		}

		try {
			statement.executeUpdate("DELETE FROM match WHERE id_tournoi="+ id +" AND num_tour=" + nbtoursav);
		} catch (SQLException e) {
			System.out.println("Erreur del tour : " + e.getMessage());
		}
	}

	public int getNbTours() {
		try {
			ResultSet rs = statement.executeQuery("SELECT MAX (num_tour)  FROM match WHERE id_tournoi="+ id +"; ");
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return -1;
		}
	}

	public static String mysql_real_escape_string(String str) {
		if (str == null) {
			return null;
		}

		if (str.replaceAll("[a-zA-Z\\d_!@#$%^&*()-=+~.;:,\\Q[\\E\\Q]\\E<>{}/? ]","").length() < 1) {
			return str;
		}

		String clean_string = str;
		clean_string = clean_string.replaceAll("\\n","\\\\n");
		clean_string = clean_string.replaceAll("\\r", "\\\\r");
		clean_string = clean_string.replaceAll("\\t", "\\\\t");
		clean_string = clean_string.replaceAll("\\00", "\\\\0");
		clean_string = clean_string.replaceAll("'", "''");
		return clean_string;

	}

}
