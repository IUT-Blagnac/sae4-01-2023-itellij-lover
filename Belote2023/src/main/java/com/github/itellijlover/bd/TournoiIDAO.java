package com.github.itellijlover.bd;

import com.github.itellijlover.model.Tournoi;

import java.util.List;

public interface TournoiIDAO extends IDAO<Tournoi> {

    public List<Tournoi> getAll (String name);

}
