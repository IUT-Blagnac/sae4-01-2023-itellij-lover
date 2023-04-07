package com.github.itellijlover.DAO;

import com.github.itellijlover.db.FactoryDB;
import com.github.itellijlover.model.Tournoi;

import java.util.List;

/**
 * Classe qui communique avec la BD pour mettre à jour les modifications des Tournois
 * TODO à compléter et à utiliser
 */
public class TournoiDAO extends FactoryDB implements DAO<Tournoi> {

	private static TournoiDAO instance;

	private TournoiDAO() {
		instance = this;
	}

	public static TournoiDAO getInstance() {
		if (instance == null) new TournoiDAO();
		return instance;
	}

	public void add(Tournoi obj) {
	}

	public void update(Tournoi obj) {
	}

	public void delete(int id) {
	}

	public Tournoi get(int id) {
		return null;
	}

	public List<Tournoi> getAll() {
		return null;
	}

}
