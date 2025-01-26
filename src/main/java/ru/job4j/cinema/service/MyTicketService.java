package ru.job4j.cinema.service;

import ru.job4j.cinema.dto.TicketMyDto;

import java.util.Collection;

public interface MyTicketService {

    Collection<TicketMyDto> findTicketDtosByUserId(int id);

}
