package ru.job4j.cinema.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.model.File;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.Genre;
import ru.job4j.cinema.repository.*;


import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CinemaMovieServiceTest {

    private static FilmRepository filmRepository;
    private static FilmSessionRepository filmSessionRepository;
    private static GenreRepository genreRepository;
    private static HallRepository hallRepository;
    private static FileRepository fileRepository;
    private static MovieService cinemaMovieService;

    @BeforeAll
    public static void initServices() {
        filmRepository = mock(FilmRepository.class);
        filmSessionRepository = mock(FilmSessionRepository.class);
        genreRepository = mock(GenreRepository.class);
        hallRepository = mock(HallRepository.class);
        fileRepository = mock(FileRepository.class);
        cinemaMovieService = new CinemaMovieService(filmRepository,
                filmSessionRepository,
                hallRepository,
                genreRepository,
                fileRepository);
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        when(filmRepository.findAll()).thenReturn(emptyList());
        when(fileRepository.findById(anyInt()))
                .thenReturn(Optional.empty());

        assertThat(cinemaMovieService.findAllFilms().size())
                .isEqualTo(0);
        assertThat(cinemaMovieService.findFilmById(0))
                .isEqualTo(Optional.empty());
    }

    @Test
    public void whenGetFilmWhatWeHave() {
        Film originalFilm = new Film("original", "empty",
                1890, 6, 99, 99999, 7);
        originalFilm.setId(7);

        when(filmRepository.findById(originalFilm.getId()))
                .thenReturn(Optional.of(originalFilm));
        when(genreRepository.findById(originalFilm.getGenreId()))
                .thenReturn(Optional.of(
                        new Genre(originalFilm.getGenreId(), "Romance")));
        when(fileRepository.findById(originalFilm.getId()))
                .thenReturn(Optional.of(
                        new File(originalFilm.getId(), "test", "test")));

        FilmDto filmDto = new FilmDto(originalFilm.getName(),
                originalFilm.getDescription(),
                originalFilm.getYear(),
                genreRepository.findById(originalFilm.getGenreId()).get().getName(),
                originalFilm.getMinimalAge(),
                originalFilm.getDurationInMinutes(),
                fileRepository.findById(originalFilm.getFileId()).get().getPath());
        filmDto.setId(originalFilm.getId());

        assertThat(cinemaMovieService.findFilmById(originalFilm.getId()).get())
                .isEqualTo(filmDto);
    }

    @Test
    public void whenGetFilmSessionWhatWeHave() {
        /**
         * TODO test fo filmSessionDTO
         */
    }
}