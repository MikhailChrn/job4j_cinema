package ru.job4j.cinema.controller;

import jakarta.servlet.http.HttpSession;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.service.FilmService;
import ru.job4j.cinema.service.UserService;

import java.util.Optional;

@Controller
@RequestMapping("/films")
@ThreadSafe
public class FilmController {

    private final FilmService filmService;

    private final UserService userService;

    public FilmController(FilmService filmService,
                          UserService userService) {
        this.filmService = filmService;
        this.userService = userService;
    }

    @GetMapping("/list")
    public String getAllFilms(Model model, HttpSession session) {
        userService.addUserAsAttributeToModel(model, session);
        model.addAttribute("filmDtos", filmService.findAll());

        return "/films/list";
    }

    @GetMapping("/{id}")
    public String getFilmById(Model model, @PathVariable int id, HttpSession session) {
        Optional<FilmDto> filmDtoOptional = filmService.findById(id);
        if (filmDtoOptional.isEmpty()) {
            model.addAttribute("message",
                    "Фильм с указанным идентификатором не найден");
            return "errors/404";
        }

        userService.addUserAsAttributeToModel(model, session);
        model.addAttribute("filmDto", filmDtoOptional.get());

        return "/films/one";
    }
}
