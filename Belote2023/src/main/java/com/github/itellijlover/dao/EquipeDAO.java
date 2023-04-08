package com.github.itellijlover.dao;

import com.github.itellijlover.controller.TournoiController;
import com.github.itellijlover.db.FactoryDB;
import com.github.itellijlover.model.Equipe;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe qui communique avec la BD pour mettre à jour les modifications des équipes
 */
public class EquipeDAO extends FactoryDB implements DAO<Equipe> {

	private static EquipeDAO instance;

	private EquipeDAO() {
		instance = this;
	}

	public static EquipeDAO getInstance() {
		if (instance == null) new EquipeDAO();
		return instance;
	}

	/**
	 * Ajoute une nouvelle équipe dans la base de données.
	 *
	 * @param obj L'équipe à ajouter.
	 */
	public void add(Equipe obj) {
		String query = "INSERT INTO equipe VALUES (NULL,"
				+ obj.getNum()+","
				+ obj.getIdTournoi()+",'"
				+ obj.getNomJ1()+"','"
				+ obj.getNomJ2()+"');";
		try {
			statement.executeUpdate(query);
		} catch (SQLException e) {
			System.out.println(query);
			e.printStackTrace();
		}
	}

	/**
	 * Met à jour une équipe existante dans la base de données.
	 *
	 * @param obj L'équipe à mettre à jour.
	 */
	public void update(Equipe obj) {
		String query = "UPDATE equipe SET "
				+ "nom_j1 = '"+ TournoiController.mysql_real_escape_string(obj.getNomJ1())+"',"
				+ "nom_j2 = '"+ TournoiController.mysql_real_escape_string(obj.getNomJ2())+"' "
				+ "WHERE id_equipe = "+obj.getId()+";";
		try {
			statement.executeUpdate(query);
		} catch (SQLException e) {
			System.out.println(query);
			e.printStackTrace();
		}
	}

	/**
	 * Supprime une équipe de la base de données en fonction de son identifiant.
	 *
	 * @param id L'identifiant de l'équipe à supprimer.
	 */
	public void delete(int id) {
		String query = "DELETE FROM equipe WHERE id_equipe = " + id;
		try {
			statement.executeUpdate(query);
		} catch (SQLException e) {
			System.out.println(query);
			e.printStackTrace();
		}
	}

	/**
	 * Récupère toutes les équipes d'un tournoi donné, triées par numéro d'équipe croissant.
	 *
	 * @param id_tournoi L'identifiant du tournoi pour lequel récupérer les équipes.
	 * @return Une liste des équipes du tournoi donné, triées par numéro d'équipe croissant.
	 */
	public List<Equipe> getAllFromTournoi(int id_tournoi) {
		List<Equipe> list_equipe = new ArrayList<>();

		String query = "SELECT * FROM equipe WHERE id_tournoi = "
				+ id_tournoi
				+ " ORDER BY num_equipe;";
		try {
			ResultSet rs = statement.executeQuery(query);

			while (rs.next()) {
				list_equipe.add(new Equipe(
						rs.getInt("id_equipe"),
						rs.getInt("num_equipe"),
						rs.getInt("id_tournoi"),
						rs.getString("nom_j1"),
						rs.getString("nom_j2")));
			}

			rs.close();
		} catch (SQLException e) {
			System.out.println(query);
			e.printStackTrace();
		}

		return list_equipe;
	}

}
