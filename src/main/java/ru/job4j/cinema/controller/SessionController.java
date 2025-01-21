package ru.job4j.cinema.controller;

import jakarta.servlet.http.HttpSession;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.service.SessionService;
import ru.job4j.cinema.service.UserService;

@Controller
@RequestMapping("/sessions")
@ThreadSafe
public class SessionController {

    private final UserService userService;

    private final SessionService sessionService;

    public SessionController(UserService userService,
                             SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @GetMapping("/list")
    public String getAllSessions(Model model, HttpSession session) {
        userService.addUserAsAttributeToModel(model, session);
        model.addAttribute("filmSessionDtos", sessionService.findAll());
        return "/sessions/list";
    }
}
