package ru.job4j.cinema.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.FilmSessionService;
import ru.job4j.cinema.service.TicketService;
import ru.job4j.cinema.service.UserService;

import java.util.Optional;

import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TicketControllerTest {

    private TicketController ticketController;

    private UserService userService;

    private FilmSessionService filmSessionService;

    private TicketService ticketService;

    private HttpSession session;

    private HttpServletRequest request;

    @BeforeEach
    public void initServices() {
        userService = mock(UserService.class);
        filmSessionService = mock(FilmSessionService.class);
        ticketService = mock(TicketService.class);
        session = mock(HttpSession.class);
        request = mock(HttpServletRequest.class);
        ticketController = new TicketController(userService,
                filmSessionService,
                ticketService);
    }

    @Test
    public void whenRequestBuyTicketPageBySessionIdThenGetBuyingPage() {
        FilmSessionDto filmSessionDto = new FilmSessionDto();
        filmSessionDto.setId(9);

        when(filmSessionService.findById(filmSessionDto.getId()))
                .thenReturn(Optional.of(filmSessionDto));

        Model model = new ConcurrentModel();
        String view = ticketController.getBuyTicketsPageBySessionId(model,
                filmSessionDto.getId(), session);
        Object actualFilmSessionDto = model.getAttribute("filmSessionDto");

        assertThat(view).isEqualTo("/tickets/buy_request");
        assertThat(actualFilmSessionDto).isEqualTo(filmSessionDto);
    }

    @Test
    public void whenRequestBuyTicketPageByNonExistentSessionIdThenRespondError() {
        String expectedMessage
                = "Сеанс с указанным идентификатором не найден";

        when(filmSessionService.findById(anyInt())).thenReturn(empty());

        Model model = new ConcurrentModel();
        String view = ticketController.getBuyTicketsPageBySessionId(model, -1, session);
        Object actualExceptionMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualExceptionMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void whenTryToBuyTicketThenRedirectSuccessPage() {
        Ticket ticket = new Ticket();
        ticket.setId(9);
        User user = new User();
        user.setId(9);

        ArgumentCaptor<Ticket> ticketArgumentCaptor = ArgumentCaptor.forClass(Ticket.class);

        when(ticketService.addTicket(ticketArgumentCaptor.capture()))
                .thenReturn(Optional.of(ticket));
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);

        Model model = new ConcurrentModel();
        String view = ticketController.tryToBuyTicket(model, ticket, request);
        Ticket actualTicket = ticketArgumentCaptor.getValue();

        assertThat(view).isEqualTo("/tickets/buy_success");
        assertThat(actualTicket).isEqualTo(ticket);
    }

    @Test
    public void whenUnsuccessTryingToBuyTicketThenRedirectFailPage() {
        Ticket ticket = new Ticket();
        ticket.setId(9);
        User user = new User();
        user.setId(9);

        String expectedMessage
                = """
                Не удалось забронировать выбранное место.\n
                Возможно оно уже занято.\n
                Попробуйте выбрать другое место.""";

        when(ticketService.addTicket(any())).thenReturn(empty());
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);

        Model model = new ConcurrentModel();
        String view = ticketController.tryToBuyTicket(model, ticket, request);
        Object actualExceptionMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("/tickets/buy_fail");
        assertThat(actualExceptionMessage).isEqualTo(expectedMessage);
    }
}