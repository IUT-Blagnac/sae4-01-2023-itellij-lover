package com.github.itellijlover.DAO;

import com.github.itellijlover.Tournoi;
import com.github.itellijlover.db.FactoryDB;
import com.github.itellijlover.model.Equipe;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

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

	public void add(Equipe obj) {
		String query = "INSERT INTO equipes VALUES (NULL,"
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

	public void update(Equipe obj) {
		String query = "UPDATE equipes SET "
				+ "nom_j1 = '"+Tournoi.mysql_real_escape_string(obj.getNomJ1())+"',"
				+ "nom_j2 = '"+Tournoi.mysql_real_escape_string(obj.getNomJ2())+"' "
				+ "WHERE id_equipe = "+obj.getId()+";";
		try {
			statement.executeUpdate(query);
		} catch (SQLException e) {
			System.out.println(query);
			e.printStackTrace();
		}
	}

	public void delete(int id) {
		String query = "DELETE FROM equipes WHERE id_equipe = " + id;
		try {
			statement.executeUpdate(query);
		} catch (SQLException e) {
			System.out.println(query);
			e.printStackTrace();
		}
	}

	public Equipe get(int id) {
		Equipe equipe = null;

		String query = "SELECT * FROM equipes WHERE id_equipe = "+id;
		try {
			ResultSet rs = statement.executeQuery(query);

			if (rs.next()) {
				equipe = new Equipe(
						rs.getInt("id_equipe"),
						rs.getInt("num_equipe"),
						rs.getInt("id_tournoi"),
						rs.getString("nom_j1"),
						rs.getString("nom_j2"));
			}

			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return equipe;
	}

	public List<Equipe> getAll() {
		List<Equipe> list_equipe = new Vector<>();

		String query = "SELECT * FROM equipes;";
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
			e.printStackTrace();
		}

		return list_equipe;
	}

	public List<Equipe> getFromTournoi(int id_tournoi) {
		List<Equipe> list_equipe = new Vector<>();

		String query = "SELECT * FROM equipes WHERE id_tournoi = "
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
			e.printStackTrace();
		}

		return list_equipe;
	}

}
