package ru.job4j.cinema.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.dto.TicketMyDto;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.*;

import java.util.Collection;
import java.util.Optional;

@Controller
@RequestMapping("/tickets")
@ThreadSafe
public class TicketController {

    private final UserService userService;

    private final FilmSessionService filmSessionService;

    private final TicketService ticketService;

    private final MyTicketService myTicketService;

    private final String nextLine = System.lineSeparator();

    public TicketController(UserService userService,
                            FilmSessionService filmSessionService,
                            TicketService ticketService,
                            MyTicketService myTicketService) {
        this.userService = userService;
        this.filmSessionService = filmSessionService;
        this.ticketService = ticketService;
        this.myTicketService = myTicketService;
    }

    @GetMapping("/my_tickets")
    public String getMyTicketsPageByUserId(Model model,
                                           HttpServletRequest request) {

        HttpSession session = request.getSession();
        Optional<User> optionalUser = Optional.of(
                (User) session.getAttribute("user")
        );

        Collection<TicketMyDto> ticketMyDtos = myTicketService
                .findTicketDtosByUserId(optionalUser.get().getId());

        userService.addUserAsAttributeToModel(model, session);
        model.addAttribute("ticketMyDtos", ticketMyDtos);

        return "/tickets/my_tickets";
    }

    @GetMapping("/buy_request/{sessionId}")
    public String getBuyTicketsPageBySessionId(Model model,
                                               @PathVariable int sessionId,
                                               HttpSession session) {

        Optional<FilmSessionDto> filmSessionDtoOptional = filmSessionService.findById(sessionId);

        if (filmSessionDtoOptional.isEmpty()) {
            model.addAttribute("message",
                    "Сеанс с указанным идентификатором не найден");
            return "errors/404";
        }

        userService.addUserAsAttributeToModel(model, session);
        model.addAttribute("filmSessionDto", filmSessionDtoOptional.get());

        return "/tickets/buy_request";
    }

    @PostMapping("/buy_request")
    public String tryToBuyTicket(Model model,
                                 @ModelAttribute Ticket ticket,
                                 HttpServletRequest request) {

        HttpSession session = request.getSession();
        Optional<User> optionalUser = Optional.of(
                (User) session.getAttribute("user")
        );
        ticket.setUserId(optionalUser.get().getId());

        Optional<Ticket> ticketOptional = ticketService.addTicket(ticket);

        if (ticketOptional.isEmpty()) {
            model.addAttribute("message",
                    "Не удалось забронировать выбранное место." + nextLine
                            + "Возможно оно уже занято." + nextLine
                            + "Попробуйте выбрать другое место.");

            return "/tickets/buy_fail";
        }

        String messageSuccess = String.format("Вы забронировали билет на %s ряд %s место",
                ticketOptional.get().getRowNumber(),
                ticketOptional.get().getPlaceNumber());
        model.addAttribute("message", messageSuccess);

        return "/tickets/buy_success";
    }
}
