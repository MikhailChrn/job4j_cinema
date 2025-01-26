package ru.job4j.cinema.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.repository.*;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

class FilmSessionServiceImplTest {

    private static HallRepository hallRepository;
    private static FilmRepository filmRepository;
    private static FilmSessionRepository filmSessionRepository;
    private static TicketRepository ticketRepository;
    private static FilmSessionService filmSessionService;

    @BeforeAll
    public static void initServices() {
        hallRepository = mock(HallRepository.class);
        filmRepository = mock(FilmRepository.class);
        filmSessionRepository = mock(FilmSessionRepository.class);
        ticketRepository = mock(TicketRepository.class);

        filmSessionService = new FilmSessionServiceImpl(
                hallRepository,
                filmRepository,
                filmSessionRepository,
                ticketRepository
        );
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        when(filmSessionRepository.findAll())
                .thenReturn(emptyList());
        when(filmSessionRepository.findById(anyInt()))
                .thenReturn(empty());

        assertThat(filmSessionService.findAll().size())
                .isEqualTo(0);
        assertThat(filmSessionService.findById(anyInt()))
                .isEqualTo(empty());
    }

    @Test
    public void whenFindByIdFilmSessionDtoThenSuccess() {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime stopTime = startTime.plusHours(2);

        Hall testHall = new Hall(9, "hall_test",
                99, 99, "empty");
        when(hallRepository.findById(testHall.getId()))
                .thenReturn(Optional.of(testHall));

        Film testFilm = new Film();
        testFilm.setId(99);
        testFilm.setName("film_test");
        when(filmRepository.findById(testFilm.getId()))
                .thenReturn(Optional.of(testFilm));

        FilmSession filmSessionOriginal
                = new FilmSession(99, 99, testHall.getId(),
                startTime, stopTime, 999);
        when(filmSessionRepository.findById(filmSessionOriginal.getId()))
                .thenReturn(Optional.of(filmSessionOriginal));

        FilmSessionDto expFilmSessionDto = new FilmSessionDto(
                filmSessionOriginal.getId(), testFilm.getName(), "hall_test",
                startTime.toString(), filmSessionOriginal.getPrice());

        assertThat(filmSessionService.findById(filmSessionOriginal.getId()))
                .isEqualTo(Optional.of(expFilmSessionDto));
    }
}