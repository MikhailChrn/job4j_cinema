package ru.job4j.cinema.controller;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.service.FilmService;
import ru.job4j.cinema.service.UserService;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FilmControllerTest {

    private FilmController filmController;

    private FilmService filmService;

    private UserService userService;

    private HttpSession session;

    @BeforeEach
    public void initServices() {
        filmService = mock(FilmService.class);
        userService = mock(UserService.class);
        session = mock(HttpSession.class);
        filmController = new FilmController(filmService, userService);
    }

    @Test
    public void whenRequestFilmListPageThenGetListPage() {
        FilmDto film1 = new FilmDto();
        FilmDto film2 = new FilmDto();
        film1.setId(1);
        film2.setId(2);
        List<FilmDto> expectedFilmDtos = List.of(film1, film2);

        when(filmService.findAll()).thenReturn(expectedFilmDtos);

        Model model = new ConcurrentModel();
        String view = filmController.getAllFilms(model, session);
        Object actualFilmDtos = model.getAttribute("filmDtos");

        assertThat(view).isEqualTo("/films/list");
        assertThat(actualFilmDtos).isEqualTo(expectedFilmDtos);
    }

    @Test
    public void whenRequestSingleFilmByIdThenGetPageWithFilm() {
        FilmDto filmDto = new FilmDto();
        filmDto.setId(9);

        when(filmService.findById(filmDto.getId())).thenReturn(Optional.of(filmDto));

        Model model = new ConcurrentModel();
        String view = filmController.getFilmById(model, filmDto.getId(), session);
        Object actualFilmdto = model.getAttribute("filmDto");

        assertThat(view).isEqualTo("/films/one");
        assertThat(actualFilmdto).isEqualTo(filmDto);
    }

    @Test
    public void whenNotFoundSingleFilmByIdThenGetErrorPageWithMessage() {
        String expectedMessage
                = "Фильм с указанным идентификатором не найден";

        when(filmService.findById(anyInt())).thenReturn(empty());

        Model model = new ConcurrentModel();
        String view = filmController.getFilmById(model, -1, session);
        Object actualExceptionMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualExceptionMessage).isEqualTo(expectedMessage);
    }

}