package com.github.itellijlover.model;

public class Equipe {

	private final int id;
	private final int num;
	private final int id_tournoi;
	private String nom_j1;
	private String nom_j2;

	/**
	 * Constructeur de la classe Equipe.
	 * @param id l'identifiant de l'équipe.
	 * @param num le numéro de l'équipe.
	 * @param id_tournoi l'identifiant du tournoi.
	 * @param nom_j1 le nom du premier joueur de l'équipe.
	 * @param nom_j2 le nom du deuxième joueur de l'équipe.
	 */
	public Equipe(int id, int num, int id_tournoi, String nom_j1, String nom_j2) {
		this.id = id;
		this.num = num;
		this.id_tournoi = id_tournoi;
		this.nom_j1 = nom_j1;
		this.nom_j2 = nom_j2;
	}

	/**
	 * Getter pour l'identifiant de l'équipe.
	 * @return L'identifiant de l'équipe.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Getter pour le numéro de l'équipe.
	 * @return Le numéro de l'équipe.
	 */
	public int getNum() {
		return num;
	}

	/**
	 * Getter pour l'identifiant du tournoi.
	 * @return L'identifiant du tournoi.
	 */
	public int getIdTournoi() {
		return id_tournoi;
	}

	/**
	 * Getter pour le nom du premier joueur de l'équipe.
	 * @return Le nom du premier joueur de l'équipe.
	 */
	public String getNomJ1() {
		return nom_j1;
	}

	/**
	 * Setter pour le nom du premier joueur de l'équipe.
	 * @param nom_j1 Le nouveau nom du premier joueur de l'équipe.
	 */
	public void setNomJ1(String nom_j1) {
		this.nom_j1 = nom_j1;
	}

	/**
	 * Getter pour le nom du deuxième joueur de l'équipe.
	 * @return Le nom du deuxième joueur de l'équipe.
	 */
	public String getNomJ2() {
		return nom_j2;
	}

	/**
	 * Setter pour le nom du deuxième joueur de l'équipe.
	 * @param nom_j2 Le nouveau nom du deuxième joueur de l'équipe.
	 */
	public void setNomJ2(String nom_j2) {
		this.nom_j2 = nom_j2;
	}

}
