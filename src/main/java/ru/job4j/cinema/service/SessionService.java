package ru.job4j.cinema.service;

import ru.job4j.cinema.dto.FilmSessionDto;

import java.util.Collection;
import java.util.Optional;

public interface SessionService {

    Collection<FilmSessionDto> findAll();

    Optional<FilmSessionDto> findById(int id);

}
