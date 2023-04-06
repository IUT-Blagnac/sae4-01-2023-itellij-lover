package com.github.itellijlover.model;

public 	class Equipe {

	private final int id;

	private final int num;

	private String nom_j1;

	private String nom_j2;

	public Equipe(int _id, int _num, String _nom_j1, String _nom_j2) {
		id = _id;
		num = _num;
		this.nom_j1 = _nom_j1;
		nom_j2 = _nom_j2;
	}

	public int getId() {
		return id;
	}

	public int getNum() {
		return num;
	}

	public String getNom_j1() {
		return nom_j1;
	}

	public void setNom_j1(String nom_j1) {
		this.nom_j1 = nom_j1;
	}

	public String getNom_j2() {
		return nom_j2;
	}

	public void setNom_j2(String nom_j2) {
		this.nom_j2 = nom_j2;
	}

}
