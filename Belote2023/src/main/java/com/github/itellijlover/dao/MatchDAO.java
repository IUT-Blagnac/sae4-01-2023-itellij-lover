package com.github.itellijlover.dao;

import com.github.itellijlover.db.FactoryDB;
import com.github.itellijlover.model.Match;

import java.sql.SQLException;
import java.util.List;

/**
 * Classe qui communique avec la BD pour mettre à jour les modifications des Matchs
 * TODO à compléter et à utiliser
 */
public class MatchDAO extends FactoryDB implements DAO<Match> {

	private static MatchDAO instance;

	private MatchDAO() {
		instance = this;
	}

	public static MatchDAO getInstance() {
		if (instance == null) new MatchDAO();
		return instance;
	}

	public void add(Match obj) {
	}

	/**
	 * Ajoute tout les matchs d'un tour d'un tournoi
	 * @param objs les matchs
	 * @param id_tournoi l'identifiant qdu tournoi
	 */
	public void addAll(List<List<Match>> objs, int id_tournoi) {
		StringBuilder query = new StringBuilder("INSERT INTO match (id_match, id_tournoi, num_tour, equipe1, equipe2, termine) VALUES ");
		int num_tour = 1;
		char virgule = ' ';
		for (List<Match> liste_matchs : objs) {
			for (Match match : liste_matchs) {
				query.append(virgule)
						.append("(NULL,")
						.append(id_tournoi)
						.append(", ")
						.append(num_tour)
						.append(", ")
						.append(match.getEq1())
						.append(", ")
						.append(match.getEq2())
						.append(", 'non')");
				virgule = ',';
			}
			num_tour++;
		}
		try {
			statement.executeUpdate(query.toString());
			statement.executeUpdate("UPDATE tournoi SET statut=2 WHERE id_tournoi="+id_tournoi);
		} catch(SQLException e) {
			System.out.println(query);
			System.out.println("Erreur validation équipes : " + e.getMessage());
		}
	}

	public void update(Match obj) {
	}

	public void delete(int id) {
	}

	public Match get(int id) {
		return null;
	}

	public List<Match> getAll() {
		return null;
	}

}
