package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.repository.FilmRepository;
import ru.job4j.cinema.repository.FilmSessionRepository;
import ru.job4j.cinema.repository.HallRepository;
import ru.job4j.cinema.repository.TicketRepository;

import java.util.Collection;


import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class FilmSessionServiceImpl implements FilmSessionService {

    private final HallRepository hallRepository;

    private final FilmRepository filmRepository;

    private final FilmSessionRepository filmSessionRepository;

    private final TicketRepository ticketRepository;

    public FilmSessionServiceImpl(HallRepository hallRepository,
                                  FilmRepository filmRepository,
                                  FilmSessionRepository filmSessionRepository,
                                  TicketRepository ticketRepository) {
        this.hallRepository = hallRepository;
        this.filmRepository = filmRepository;
        this.filmSessionRepository = filmSessionRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Collection<FilmSessionDto> findAll() {
        ConcurrentMap<Integer, FilmSessionDto> filmSessionDtos = new ConcurrentHashMap<>();

        FilmSessionDto filmSessionDto;
        int id;

        for (FilmSession filmSession : filmSessionRepository.findAll()) {
            id = filmSession.getId();
            filmSessionDto = new FilmSessionDto(
                    id,
                    filmRepository.findById(filmSession.getFilmId()).get().getName(),
                    hallRepository.findById(filmSession.getHallId()).get().getName(),
                    filmSession.getStartTime().toString(),
                    filmSession.getPrice(),
                    filmSession.getFilmId()
            );

            filmSessionDtos.put(id, filmSessionDto);
        }

        return filmSessionDtos.values();
    }

    @Override
    public Optional<FilmSessionDto> findById(int id) {
        Optional<FilmSession> optFilmSession = filmSessionRepository.findById(id);
        if (optFilmSession.isEmpty()) {
            return Optional.empty();
        }

        FilmSession filmSession = optFilmSession.get();

        FilmSessionDto filmSessionDto = new FilmSessionDto(
                id,
                filmRepository.findById(filmSession.getFilmId()).get().getName(),
                hallRepository.findById(filmSession.getHallId()).get().getName(),
                filmSession.getStartTime().toString(),
                filmSession.getPrice(),
                filmSession.getFilmId()
        );

        addTicketsToFilmSessionDto(filmSessionDto);

        return Optional.of(filmSessionDto);
    }

    private void addTicketsToFilmSessionDto(FilmSessionDto filmSessionDto) {
        Collection<Ticket> tickets
                = ticketRepository.findBySessionId(filmSessionDto.getId());

        tickets.forEach(
                ticket -> filmSessionDto.getTickets().add(ticket));
    }
}
