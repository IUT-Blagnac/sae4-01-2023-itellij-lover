package com.github.itellijlover.bd;

import com.github.itellijlover.model.Tournoi;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class TournoiDAOImpl extends FactoryDB implements IDAO<Tournoi> {

	public void add(Tournoi obj) {
		// TODO Auto-generated method stub
		
	}

	public void delete(int id) {
		// TODO Auto-generated method stub
		String sql ="delete * from actor where actor_id="+id;
		try {
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Tournoi getOne(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Tournoi> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Tournoi> getAll(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
