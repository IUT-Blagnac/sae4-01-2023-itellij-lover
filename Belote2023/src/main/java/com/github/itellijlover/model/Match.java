package com.github.itellijlover.model;

public class Match {

	private int id;
	private int id_tournoi;
	private int num_tour;
	private String termine;
	private final int eq1;
	private final int eq2;
	private int score1;
	private int score2;

	/**
	 * Constructeur d'un match avec score.
	 *
	 * @param id : l'identifiant du match.
	 * @param eq1 : l'identifiant de la première équipe.
	 * @param eq2 : l'identifiant de la deuxième équipe.
	 * @param score1  : le score de la première équipe.
	 * @param score2  : le score de la deuxième équipe.
	 */
	public Match(int id, int eq1, int eq2, int score1, int score2, int num_tour) {
		this.id = id;
		this.num_tour = num_tour;
		this.eq1 = eq1;
		this.eq2 = eq2;
		this.score1 = score1;
		this.score2 = score2;
	}

	/**
	 * Constructeur d'un match avec tournoi, numéro de tour et état.
	 *
	 * @param id_tournoi identifiant du tournoi
	 * @param num_tour numéro du tour
	 * @param eq1 numéro de l'équipe 1
	 * @param eq2 numéro de l'équipe 2
	 * @param termine état "terminé"
	 */
	public Match(int id_tournoi, int num_tour, int eq1, int eq2, String termine) {
		this.id_tournoi = id_tournoi;
		this.num_tour = num_tour;
		this.termine = termine;
		this.eq1 = eq1;
		this.eq2 = eq2;
	}

	/**
	 * Constructeur d'un match avec seulement le numéro des équipes.
	 *
	 * @param eq1 : l'identifiant de la première équipe.
	 * @param eq2 : l'identifiant de la deuxième équipe.
	 */
	public Match(int eq1, int eq2) {
		this.eq1 = eq1;
		this.eq2 = eq2;
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
	 * Getter pour l'identifiant du tournoi du match.
	 *
	 * @return L'identifiant du tournoi du match.
	 */
	public int getIdTournoi() {
		return id_tournoi;
	}

	/**
	 * Getter pour l'état du match.
	 *
	 * @return L'état du match.
	 */
	public String getTermine() {
		return termine;
	}

	/**
	 * Méthode pour obtenir une représentation textuelle du match.
	 *
	 * @return Une chaîne de caractères représentant le match.
	 */
	public String toString() {
		if (eq1 < eq2) {
			return eq1 + " contre " + eq2;
		} else {
			return eq2 + " contre " + eq1;
		}
	}

}
