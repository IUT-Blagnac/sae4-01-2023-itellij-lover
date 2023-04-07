package com.github.itellijlover.dao;

import com.github.itellijlover.db.FactoryDB;
import com.github.itellijlover.model.Match;

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
