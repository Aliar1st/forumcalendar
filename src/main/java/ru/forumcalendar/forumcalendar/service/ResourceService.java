package ru.forumcalendar.forumcalendar.service;

import java.util.List;

public interface ResourceService<T, M, F> {

    boolean exist(int id);

    T get(int id);

    List<M> getAll();

    T save(F form);

    T delete(int id);
}
