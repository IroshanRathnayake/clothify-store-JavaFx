package com.clothify.repository;

import javafx.collections.ObservableList;

public interface CrudDao <T> extends SuperDao{
    boolean save(T t);
    boolean update(T t);
    boolean delete(String id);
    ObservableList<T> findAll();
    T findById(String id);
    T findByPhone(String phone);
    String findLastID();
}
