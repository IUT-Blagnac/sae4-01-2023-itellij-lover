package com.github.itellijlover.model;

public class Match {

	private int id;
	private int num_tour;
	private final int eq1;
	private final int eq2;
	private int score1;
	private int score2;

	/**
	 * Constructeur d'un match avec score.
	 *
	 * @param _idmatch : l'identifiant du match.
	 * @param _e1      : l'identifiant de la première équipe.
	 * @param _e2      : l'identifiant de la deuxième équipe.
	 * @param _score1  : le score de la première équipe.
	 * @param _score2  : le score de la deuxième équipe.
	 */
	public Match(int _idmatch, int _e1, int _e2, int _score1, int _score2, int _num_tour) {
		id = _idmatch;
		num_tour = _num_tour;
		eq1 = _e1;
		eq2 = _e2;
		score1 = _score1;
		score2 = _score2;
	}

	/**
	 * Constructeur d'un match sans score.
	 *
	 * @param _e1 : l'identifiant de la première équipe.
	 * @param _e2 : l'identifiant de la deuxième équipe.
	 */
	public Match(int _e1, int _e2) {
		eq1 = _e1;
		eq2 = _e2;
	}

	/**
	 * Getter pour l'identifiant du match.
	 *
	 * @return L'identifiant du match.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Getter pour le numéro du tour.
	 *
	 * @return Le numéro du tour.
	 */
	public int getNumTour() {
		return num_tour;
	}

	/**
	 * Getter pour l'identifiant de la première équipe.
	 *
	 * @return L'identifiant de la première équipe.
	 */
	public int getEq1() {
		return eq1;
	}

	/**
	 * Getter pour l'identifiant de la deuxième équipe.
	 *
	 * @return L'identifiant de la deuxième équipe.
	 */
	public int getEq2() {
		return eq2;
	}

	/**
	 * Getter pour le score de la première équipe.
	 *
	 * @return Le score de la première équipe.
	 */
	public int getScore1() {
		return score1;
	}

	/**
	 * Setter pour le score de la première équipe.
	 *
	 * @param score1 Le nouveau score de la première équipe.
	 */
	public void setScore1(int score1) {
		this.score1 = score1;
	}

	/**
	 * Getter pour le score de la deuxième équipe.
	 *
	 * @return Le score de la deuxième équipe.
	 */
	public int getScore2() {
		return score2;
	}

	/**
	 * Setter pour le score de la deuxième équipe.
	 *
	 * @param score2 Le nouveau score de la deuxième équipe.
	 */
	public void setScore2(int score2) {
		this.score2 = score2;
	}

	/**
	 * Méthode pour obtenir une représentation textuelle du match.
	 *
	 * @return Une chaîne de caractères représentant le match.
	 */
	public String toString() {
		if (eq1 < eq2) {
			return "  " + eq1 + " contre " + eq2;
		} else {
			return "  " + eq2 + " contre " + eq1;
		}
	}

}
