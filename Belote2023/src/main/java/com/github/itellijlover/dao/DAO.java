package com.github.itellijlover.dao;

import java.util.List;

public interface DAO<T> {

    public void add(T obj);

    public void update(T obj);

    public void delete(int id);

    public T get(int id);

    public List<T> getAll();

}
