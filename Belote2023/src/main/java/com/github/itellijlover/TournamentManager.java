package com.github.itellijlover;

import com.github.itellijlover.dao.EquipeDAO;
import com.github.itellijlover.dao.MatchDAO;
import com.github.itellijlover.db.DatabaseConnection;
import com.github.itellijlover.model.Equipe;
import com.github.itellijlover.model.Match;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Classe gérant un tournoi
 */
public class TournamentManager {

	private final Statement statement;
	private final EquipeDAO equipeDAO = EquipeDAO.getInstance();
	private final MatchDAO matchDAO = MatchDAO.getInstance();

	private int id;
	private final String nom;
	private int statut_en_int;
	private String statut_en_string;

	private List<Equipe> list_equipe;
	private List<Match> list_match;

	public TournamentManager(String nom) {
		this.statement = DatabaseConnection.getInstance().getStatement();

		this.nom = nom;

		try {
			ResultSet rs = statement.executeQuery("SELECT * FROM tournoi WHERE nom_tournoi = '" + mysql_real_escape_string(nom) + "';");
			if (!rs.next()) {
				return;
			}
			this.id = rs.getInt("id_tournoi");

			this.statut_en_int = rs.getInt("statut");
			rs.close();

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


	// Gestion tournois

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
					statement.executeUpdate("INSERT INTO tournoi (id_tournoi, nb_matchs, nom_tournoi, statut) VALUES (NULL, 10, '"+string+"', 0)");
				} catch (SQLException e) {
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
			statement.executeUpdate("DELETE FROM match   WHERE id_tournoi = " + idt);
			statement.executeUpdate("DELETE FROM equipe  WHERE id_tournoi = " + idt);
			statement.executeUpdate("DELETE FROM tournoi WHERE id_tournoi = " + idt);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	// Gestion équipes

	/**
	 * Ajoute une équipe par défaut au tournoi
	 */
	public void addEquipe() {
		int num_new_equipe = list_equipe.size()+1;

		boolean already_used = true;

		while (already_used) {
			already_used = false;
			for (Equipe equipe : list_equipe) {
				if (equipe.getNum() == num_new_equipe) {
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

	/**
	 * Met à jour une équipe en fonction de l'index dans la liste d'équipes
	 * @param index index de l'équipe à mettre à jour dans la liste d'équipes
	 */
	public void updateEquipe(int index) {
		Equipe equipe = list_equipe.get(index);
		equipeDAO.update(equipe);
		list_equipe.remove(equipe);
		list_equipe.add(equipe);
	}

	/**
	 * Supprime une équipe du tournoi en fonction de son identifiant
	 * @param id_equipe identifiant de l'équipe
	 */
	public void deleteEquipe(int id_equipe) {
		equipeDAO.delete(id_equipe);
		for (Equipe e : list_equipe) {
			if (e.getId() == id_equipe) {
				list_equipe.remove(e);
				break;
			}
		}
	}

	/**
	 * Récupère une équipe de la liste d'équipe en fonction de l'index
	 * @param index index de l'équipe à récupérer
	 * @return l'équipe correspondant à l'index
	 */
	public Equipe getEquipe(int index) {
		if (list_equipe == null) getEquipes();
		return list_equipe.get(index);
	}

	/**
	 * Stock toutes les équipes correspondant au tournoi dans la liste d'équipes
	 */
	public void getEquipes() {
		list_equipe = equipeDAO.getAllFromTournoi(id);
	}

	/**
	 * @return le nombre d'équipes du tournoi
	 */
	public int getNbEquipes() {
		if (list_equipe == null) getEquipes();
		return list_equipe.size();
	}


	// Gestion matchs

	/**
	 * Génère les matchs du premier tour du tournoi, en fonciton du nombre d'équipe
	 */
	public void genererMatchs() {
		List<List<Match>> matchs = genererMatchsToDo(getNbEquipes(), 1);
		matchDAO.addAll(matchs, id);
		statut_en_int = 2;
		statut_en_string = "Matchs en cours";
	}

	/**
	 * Génère les match en fonction du nombre d'équipe et de tours
	 * @param nbEquipes nombre d'équipe dans le tournoi
	 * @param nbTours nombre de tour
	 * @return liste de liste de match
	 */
	public static List<List<Match>> genererMatchsToDo(int nbEquipes, int nbTours) {
		if (nbTours  >= nbEquipes) {
			System.out.println("Erreur tours < equipes");
			return null;
		}

		int[] tabEquipes;
		if ((nbEquipes % 2) == 1) {
			// Nombre impair de joueurs, on rajoute une équipe fictive
			tabEquipes = new int[nbEquipes+1];
			tabEquipes[nbEquipes] = -1;
			for (int i = 0; i < nbEquipes; i++) {
				tabEquipes[i] = i+1;
			}
			nbEquipes++;
		} else {
			tabEquipes = new int[nbEquipes];
			for (int i = 0; i < nbEquipes; i++) {
				tabEquipes[i] = i+1;
			}
		}

		boolean quitter;
		int i, temp;
		int increment = 1;

		List<List<Match>> resultat = new ArrayList<>();

		List<Match> vector_matchs;

		for (int y = 1; y <= nbTours;y++) {
			if (y > 1) {
				temp = tabEquipes[nbEquipes - 2];
				for (i = (nbEquipes - 2) ; i > 0; i--) {
					tabEquipes[i] = tabEquipes[i-1];
				}
				tabEquipes[0] = temp;
			}
			i = 0;
			quitter = false;
			vector_matchs = new ArrayList<>();
			while (!quitter) {
				if (tabEquipes[i] != -1 && tabEquipes[nbEquipes - 1  - i] != -1) {
					vector_matchs.add(new Match(tabEquipes[i], tabEquipes[nbEquipes - 1  - i]));
				}
				// Sinon : Nombre impair de joueur, le joueur n'a pas d'adversaire
				i += increment;
				if (i >= nbEquipes / 2) {
					quitter = true;
				}
			}
			resultat.add(vector_matchs);
		}
		return resultat;
	}

	/**
	 * met à jour le match de la liste à l'index "index"
	 * @param index index du match dans la liste
	 */
	public void updateMatch(int index) {
		Match match = getMatch(index);
		matchDAO.update(match);
	}

	/**
	 * @param index index du match cherché
	 * @return le match correspondant à l'index du match dans la liste de match
	 */
	public Match getMatch(int index) {
		if (list_match == null) getMatchs();
		return list_match.get(index);
	}

	/**
	 * Récupère les matchs liés au tournoi
	 */
	public void getMatchs() {
		list_match = matchDAO.getAllFromTournoi(id);
	}

	/**
	 * @return le nombre de matchs de la liste de matchs
	 */
	public int getNbMatchs() {
		if (list_match == null) getMatchs();
		return list_match.size();
	}


	// Gestion tours

	/**
	 * Ajoute un nouveau tour, créer les matchs pour le nouveau match
	 */
	public void ajouterTour() {
		// Recherche du nombre de tours actuel
		int nbtoursav;
		if (getNbTours() >= (getNbEquipes() -1)) return;
		try {
			ResultSet rs = statement.executeQuery("SELECT MAX (num_tour) FROM match WHERE id_tournoi="+id);
			rs.next();
			nbtoursav = rs.getInt(1);
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}

		if (nbtoursav == 0) {
			List<List<Match>> matchs = genererMatchsToDo(getNbEquipes(), nbtoursav + 1);

			List<Match> ms = matchs.get(matchs.size()-1);

			StringBuilder query = new StringBuilder("INSERT INTO match (id_match, id_tournoi, num_tour, equipe1, equipe2, termine ) VALUES\n");
			char virgule = ' ';
			for (Match m : ms) {
				query.append(virgule).append("(NULL,").append(id).append(", ").append(nbtoursav + 1).append(", ").append(m.getEq1()).append(", ").append(m.getEq2()).append(", 'non')");
				virgule = ',';
			}
			query.append("\n");

			try {
				statement.executeUpdate(query.toString());
			} catch(SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				ResultSet rs;
				rs = statement.executeQuery("SELECT equipe, (SELECT count(*) FROM match m WHERE (m.equipe1 = equipe AND m.score1 > m.score2  AND m.id_tournoi = id_tournoi) OR (m.equipe2 = equipe AND m.score2 > m.score1 )) as matchs_gagnes FROM  (select equipe1 as equipe,score1 as score from match where id_tournoi=" + this.id + " UNION select equipe2 as equipe,score2 as score from match where id_tournoi=" + this.id + ") GROUP BY equipe  ORDER BY matchs_gagnes DESC;");

				ArrayList<Integer> ordreeq= new ArrayList<>();
				while (rs.next()) {
					ordreeq.add(rs.getInt("equipe"));
				}
				int i;
				boolean fini;
				StringBuilder query = new StringBuilder("INSERT INTO match ( id_match, id_tournoi, num_tour, equipe1, equipe2, termine ) VALUES\n");
				char virgule = ' ';
				while (ordreeq.size() > 1) {
					int j = 0;
					while (j < ordreeq.size()) {
						j++;
					}
					i = 1;
					do {
						rs = statement.executeQuery("SELECT COUNT(*) FROM match m WHERE ( (m.equipe1 = " + ordreeq.get(0) + " AND m.equipe2 = " + ordreeq.get(i) + ") OR (m.equipe2 = " + ordreeq.get(0) + " AND m.equipe1 = " + ordreeq.get(i) + ")  )");
						rs.next();
						if (rs.getInt(1) > 0) {
							// Le match est déjà joué
							i++;
							fini = false;

						} else {
							fini = true;
							query.append(virgule).append("(NULL,").append(id).append(", ").append(nbtoursav + 1).append(", ").append(ordreeq.get(0)).append(", ").append(ordreeq.get(i)).append(", 'non')");
							ordreeq.remove(0);
							ordreeq.remove(i-1);
							virgule = ',';
						}
					} while(!fini);
				}
				statement.executeUpdate(query.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Supprime les matchs du dernier tour
	 */
	public void deleteLastTour() {
		int num_tour;
		String query = "SELECT MAX (num_tour) FROM match WHERE id_tournoi="+id;
		try {
			ResultSet rs = statement.executeQuery(query);
			rs.next();
			num_tour = rs.getInt(1);
			rs.close();
			matchDAO.deleteFromTournoiAndNumTour(id, num_tour);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return le nombre de tour du tournoi ou -1 en cas d'erreur
	 */
	public int getNbTours() {
		try {
			ResultSet rs = statement.executeQuery("SELECT MAX (num_tour) FROM match WHERE id_tournoi="+id);
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
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
