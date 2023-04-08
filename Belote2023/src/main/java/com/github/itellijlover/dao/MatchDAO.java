package com.github.itellijlover.dao;

import com.github.itellijlover.db.FactoryDB;
import com.github.itellijlover.model.Equipe;
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

	/**
	 * Insère le match donné
	 * @param obj le match à insérer
	 */
	public void add(Match obj) {
		String query = "INSERT INTO match (id_match, id_tournoi, num_tour,"
				+ "equipe1, equipe2, termine) VALUES (NULL,"
				+ obj.getIdTournoi()+","
				+ obj.getNumTour()+","
				+ obj.getEq1()+","
				+ obj.getEq2()+",'"
				+ obj.getTermine()+"')";
		try {
			statement.executeUpdate(query);
		} catch (SQLException e) {
			System.out.println(query);
			e.printStackTrace();
		}
	}

	/**
	 * Ajoute tous les matchs donnés
	 * @param objs les matchs
	 * @param id_tournoi l'identifiant du tournoi des matchs
	 */
	public void addAll(List<List<Match>> objs, int id_tournoi) {
		int num_tour = 1;
		for (List<Match> liste_matchs : objs) {
			for (Match match : liste_matchs) {
				add(new Match(id_tournoi, num_tour, match.getEq1(), match.getEq2(), "nom"));
			}
			num_tour++;
		}
		String query = "UPDATE tournoi SET statut=2 WHERE id_tournoi="+id_tournoi;
		try {
			statement.executeUpdate(query);
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

	public List<Equipe> getAllFromTournoi(int id_tournoi) {
		return null;
	}

}
