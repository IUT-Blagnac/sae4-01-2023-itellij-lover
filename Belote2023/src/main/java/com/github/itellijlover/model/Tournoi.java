package com.github.itellijlover.model;

import java.util.List;

public class Tournoi {

    private int id;
    private final String nom_tournoi;
    private int statut_en_int;
    private String statut_en_string; // TODO inutile ?

    private List<Equipe> list_equipe;
    private List<Match> list_match;

    public Tournoi(String nom) {
        nom_tournoi = nom;
    }

    public String getNom_tournoi() {
        return nom_tournoi;
    }

    public int getId() {
        return id;
    }

    public int getStatut_en_int() {
        return statut_en_int;
    }

    public void setStatut_en_int(int statut_en_int) {
        this.statut_en_int = statut_en_int;
    }

    public String getStatut_en_string() {
        return statut_en_string;
    }

    public void setStatut_en_string(String statut_en_string) {
        this.statut_en_string = statut_en_string;
    }

    public List<Equipe> getList_equipe() {
        return list_equipe;
    }

    public void setList_equipe(List<Equipe> list_equipe) {
        this.list_equipe = list_equipe;
    }

    public List<Match> getList_match() {
        return list_match;
    }

    public void setList_match(List<Match> list_match) {
        this.list_match = list_match;
    }
}
