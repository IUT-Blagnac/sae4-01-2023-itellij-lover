package com.github.itellijlover.model;

public class MatchM {

	private int idmatch, eq1, eq2, score1, score2, num_tour;

	private boolean termine;

	public int getIdmatch() {
		return idmatch;
	}

	public void setIdmatch(int idmatch) {
		this.idmatch = idmatch;
	}

	public int getEq1() {
		return eq1;
	}

	public void setEq1(int eq1) {
		this.eq1 = eq1;
	}

	public int getEq2() {
		return eq2;
	}

	public void setEq2(int eq2) {
		this.eq2 = eq2;
	}

	public int getScore1() {
		return score1;
	}

	public void setScore1(int score1) {
		this.score1 = score1;
	}

	public int getScore2() {
		return score2;
	}

	public void setScore2(int score2) {
		this.score2 = score2;
	}

	public int getNum_tour() {
		return num_tour;
	}

	public void setNum_tour(int num_tour) {
		this.num_tour = num_tour;
	}

	public boolean isTermine() {
		return termine;
	}

	public void setTermine(boolean termine) {
		this.termine = termine;
	}

	public MatchM(int _idmatch, int _e1, int _e2, int _score1, int _score2, int _num_tour, boolean _termine) {
		idmatch = _idmatch;
		eq1 = _e1;
		eq2 = _e2;
		score1 = _score1;
		score2 = _score2;
		num_tour = _num_tour;
		termine = _termine;
	}

	public MatchM(int _e1, int _e2) {
		eq1 = _e1;
		eq2 = _e2;
	}

	public String toString() {
		if (eq1 < eq2) {
			return "  " + eq1 + " contre " + eq2;
		} else {
			return "  " + eq2 + " contre " + eq1;
		}
	}
 }
