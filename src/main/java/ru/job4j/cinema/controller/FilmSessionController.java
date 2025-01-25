package ru.job4j.cinema.controller;

import jakarta.servlet.http.HttpSession;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.service.FilmSessionService;
import ru.job4j.cinema.service.UserService;

@Controller
@RequestMapping("/film_sessions")
@ThreadSafe
public class FilmSessionController {

    private final UserService userService;

    private final FilmSessionService filmSessionService;

    public FilmSessionController(UserService userService,
                                 FilmSessionService filmSessionService) {
        this.userService = userService;
        this.filmSessionService = filmSessionService;
    }

    @GetMapping("/list")
    public String getAllSessions(Model model, HttpSession session) {
        userService.addUserAsAttributeToModel(model, session);
        model.addAttribute("filmSessionDtos", filmSessionService.findAll());
        return "/film_sessions/list";
    }
}
