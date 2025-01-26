package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.dto.TicketMyDto;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.repository.TicketRepository;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class MyTicketServiceImpl implements MyTicketService {

    private final TicketRepository ticketRepository;

    private final FilmSessionService filmSessionService;

    public MyTicketServiceImpl(TicketRepository ticketRepository,
                               FilmSessionService filmSessionService) {
        this.ticketRepository = ticketRepository;
        this.filmSessionService = filmSessionService;
    }

    @Override
    public Collection<TicketMyDto> findTicketDtosByUserId(int id) {
        ConcurrentMap<Integer, TicketMyDto> ticketMyDtos = new ConcurrentHashMap<>();

        TicketMyDto ticketMyDto;
        FilmSessionDto filmSessionMyDto;

        for (Ticket ticket : ticketRepository.findByUserId(id)) {
            filmSessionMyDto = filmSessionService.findById(ticket.getSessionId()).get();

            ticketMyDto = new TicketMyDto();
            ticketMyDto.setId(ticket.getId());
            ticketMyDto.setFilmTitle(filmSessionMyDto.getFilmTitle());
            ticketMyDto.setHallTitle(filmSessionMyDto.getHallTitle());
            ticketMyDto.setRowNumber(ticket.getRowNumber());
            ticketMyDto.setPlaceNumber(ticket.getPlaceNumber());
            ticketMyDto.setStartTime(filmSessionMyDto.getStartTime());

            ticketMyDtos.put(ticketMyDto.getId(), ticketMyDto);
        }

        return ticketMyDtos.values();
    }
}
