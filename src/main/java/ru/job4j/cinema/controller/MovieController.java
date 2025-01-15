package ru.job4j.cinema.controller;

import jakarta.servlet.http.HttpSession;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.MovieService;

import java.util.Optional;

@Controller
@RequestMapping("/movies")
@ThreadSafe
public class MovieController {

    private final MovieService movieService;

    private final UserController userController;

    public MovieController(MovieService movieService,
                           UserController userController) {
        this.movieService = movieService;
        this.userController = userController;
    }

    @GetMapping("/all_films")
    public String getAllFilms(Model model, HttpSession session) {
        userController.addUserAsAttributeToModel(model, session);
        model.addAttribute("filmDtos", movieService.findAllFilms());
        return "movies/all_films";
    }

    @GetMapping("/{id}")
    public String getFilmById(Model model, @PathVariable int id, HttpSession session) {
        Optional<FilmDto> filmDtoOptional = movieService.findFilmById(id);
        if (filmDtoOptional.isEmpty()) {
            model.addAttribute("message",
                    "Фильм с указанным идентификатором не найден");
            return "errors/404";
        }

        userController.addUserAsAttributeToModel(model, session);
        model.addAttribute("filmDto", filmDtoOptional.get());
        return "movies/film";
    }

    protected void addUserAsAttributeToModel(Model model,
                                             HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setFullName("Гость");
        }
        model.addAttribute("user", user);
    }

}
