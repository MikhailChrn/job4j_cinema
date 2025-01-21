package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Ticket;

import java.util.Collection;
import java.util.Optional;

public interface TicketRepository {

    Optional<Ticket> findById(int id);

    Collection<Ticket> findAll();

    Collection<Ticket> findBySessionId(int id);

    Ticket save(Ticket ticket);

    boolean deleteById(int id);

    boolean update(Ticket ticket);

}
