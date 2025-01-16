package ru.job4j.cinema.service;

import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.dto.FilmSessionDto;

import java.util.Collection;
import java.util.Optional;

public interface MovieService {

    Optional<FilmDto> findFilmById(int id);

    Collection<FilmDto> findAllFilms();

    Optional<FilmSessionDto> findSessionById(int id);

    Collection<FilmSessionDto> findAllSessions();

}
