package com.github.itellijlover.dao;

import java.util.List;

public interface DAO<T> {

    void add(T obj);

    void update(T obj);

    void delete(int id);

    List<T> getAllFromTournoi(int id_tournoi);

}
