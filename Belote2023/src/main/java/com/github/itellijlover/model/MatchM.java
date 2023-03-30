package com.github.itellijlover.model;

public class MatchM {

	private int idmatch;
	private final int eq1;
	private final int eq2;
	private int score1;
	private int score2;
	private int num_tour;


	public int getIdmatch() {
		return idmatch;
	}

	public int getEq1() {
		return eq1;
	}

	public int getEq2() {
		return eq2;
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

	public MatchM(int _idmatch, int _e1, int _e2, int _score1, int _score2, int _num_tour) {
		idmatch = _idmatch;
		eq1 = _e1;
		eq2 = _e2;
		score1 = _score1;
		score2 = _score2;
		num_tour = _num_tour;
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
