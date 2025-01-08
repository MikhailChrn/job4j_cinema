package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.FilmSession;

import java.util.Collection;
import java.util.Optional;

public interface FilmSessionRepository {

    Optional<FilmSession> findById(int id);

    Collection<FilmSession> findAll();

    FilmSession save(FilmSession filmSession);

    boolean deleteById(int id);

    boolean update(FilmSession filmSession);

}
