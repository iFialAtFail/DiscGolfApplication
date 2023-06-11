package com.manleysoftware.michael.discgolfapp.data;

import android.content.Context;

import com.manleysoftware.michael.discgolfapp.application.AlreadyExistsException;

public interface Repository<T> {
    void add(T entity, Context context) throws AlreadyExistsException;
    void update(T entity, Context context);
    void delete(T entity, Context context);
    T findByPrimaryKey(T template);
}
