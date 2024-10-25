package com.clothify.repository;

import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface CrudDao <T> extends SuperDao{
    boolean save(T t) throws SQLException;
    boolean update(T t);
    boolean delete(String id);
    ObservableList<T> findAll();
    T findById(String id);
    String findLastID();
}
