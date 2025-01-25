package ru.job4j.cinema.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.repository.TicketRepository;

import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TicketServiceImplTest {

    private static TicketRepository ticketRepository;
    private static TicketService ticketService;

    @BeforeAll
    public static void initServices() {
        ticketRepository = mock(TicketRepository.class);

        ticketService = new TicketServiceImpl(ticketRepository);
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        when(ticketRepository.findAll()).thenReturn(emptyList());
        when(ticketRepository.findById(anyInt())).thenReturn(empty());

        assertThat(ticketService.findAll().size()).isEqualTo(0);
        assertThat(ticketService.findById(anyInt())).isEqualTo(empty());
    }

    @Test
    public void whenFindTicketByIdThenSuccess() {
        Ticket expTicket = new Ticket(
                9, 99, 9, 9, 9);

        when(ticketRepository.findById(expTicket.getId()))
                .thenReturn(Optional.of(expTicket));

        assertThat(ticketService.findById(expTicket.getId()).get())
                .isEqualTo(expTicket);
    }

    @Test
    public void whenFindTicketByIdThenFail() {
        // TODO
    }
}