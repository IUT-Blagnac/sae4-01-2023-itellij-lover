package com.github.itellijlover.dao;

import com.github.itellijlover.db.FactoryDB;
import com.github.itellijlover.model.Equipe;
import com.github.itellijlover.model.Match;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

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

	/**
	 * Met à jour le match sur la BD
	 * @param obj le match
	 */
	public void update(Match obj) {
		String termine = (obj.getScore1() > 0 || obj.getScore2() > 0) ? "oui" : "non";
		String query = "UPDATE match SET "
				+ "equipe1=" + obj.getEq1()
				+ ", equipe2=" + obj.getEq2()
				+ ", score1=" + obj.getScore1()
				+ ", score2=" +obj.getScore2()
				+ ", termine='" + termine
				+ "' WHERE id_match=" + obj.getId();
		try {
			statement.executeUpdate(query);
		} catch (SQLException e) {
			System.out.println(query);
			e.printStackTrace();
		}
	}

	public void delete(int id) {
	}

	public Match get(int id) {
		return null;
	}

	public List<Match> getAll() {
		return null;
	}

	/**
	 * Récupère tous les matchs d'un tournoi en particulier
	 * @param id_tournoi l'identifiant du tournoi
	 * @return tous les matchs du tournoi spécifié
	 */
	public List<Match> getAllFromTournoi(int id_tournoi) {
		List<Match> list_match = new ArrayList<>();

		String query = "SELECT * FROM match WHERE id_tournoi="+id_tournoi;
		try {
			ResultSet rs = statement.executeQuery(query);

			while (rs.next()) {
				list_match.add(new Match(
						rs.getInt("id_match"),
						rs.getInt("equipe1"),
						rs.getInt("equipe2"),
						rs.getInt("score1"),
						rs.getInt("score2"),
						rs.getInt("num_tour")));
			}

			rs.close();
		} catch (SQLException e) {
			System.out.println(query);
			e.printStackTrace();
		}

		return list_match;
	}

}
