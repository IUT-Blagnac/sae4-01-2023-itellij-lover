package com.github.itellijlover.model;

public 	class Equipe {

	private final int id;

	private final int num;

	private final int id_tournoi;

	private String nom_j1;

	private String nom_j2;

	public Equipe(int _id, int _num, int _id_tournoi, String _nom_j1, String _nom_j2) {
		id = _id;
		num = _num;
		id_tournoi = _id_tournoi;
		nom_j1 = _nom_j1;
		nom_j2 = _nom_j2;
	}

	public int getId() {
		return id;
	}

	public int getNum() {
		return num;
	}

	public int getIdTournoi() {
		return id_tournoi;
	}

	public String getNomJ1() {
		return nom_j1;
	}

	public void setNomJ1(String nom_j1) {
		this.nom_j1 = nom_j1;
	}

	public String getNomJ2() {
		return nom_j2;
	}

	public void setNomJ2(String nom_j2) {
		this.nom_j2 = nom_j2;
	}

}
