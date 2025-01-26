package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.repository.TicketRepository;


import java.util.Collection;

import java.util.Optional;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Optional<Ticket> addTicket(Ticket ticket) {
        if (isTicketAvaliable(ticket)) {
            return Optional.of(ticketRepository.save(ticket));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Ticket> findById(int id) {
        return ticketRepository.findById(id);
    }

    @Override
    public Collection<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    private boolean isTicketAvaliable(Ticket ticket) {
        boolean isTicketAvaliable = true;
        Collection<Ticket> ticketsInSession
                = ticketRepository.findBySessionId(ticket.getSessionId());
        for (Ticket t : ticketsInSession) {
            if (t.getRowNumber() == ticket.getRowNumber()
                    && t.getPlaceNumber() == ticket.getPlaceNumber()) {
                isTicketAvaliable = false;
                break;
            }
        }
        return isTicketAvaliable;
    }
}
