package ru.job4j.cinema.controller;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.service.FilmSessionService;
import ru.job4j.cinema.service.UserService;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FilmSessionControllerTest {

    private FilmSessionController filmSessionController;

    private UserService userService;

    private FilmSessionService filmSessionService;

    private HttpSession session;

    @BeforeEach
    public void initServices() {
        userService = mock(UserService.class);
        filmSessionService = mock(FilmSessionService.class);
        session = mock(HttpSession.class);
        filmSessionController
                = new FilmSessionController(userService, filmSessionService);
    }

    @Test
    public void whenRequestFilmSessionListPageThenGetListPage() {
        FilmSessionDto filmSessionDto1 = new  FilmSessionDto();
        FilmSessionDto filmSessionDto2 = new  FilmSessionDto();
        filmSessionDto1.setId(1);
        filmSessionDto2.setId(2);
        List<FilmSessionDto> expectedFilmSessionDtos
                = List.of(filmSessionDto1, filmSessionDto2);

        when(filmSessionService.findAll()).thenReturn(expectedFilmSessionDtos);

        Model model = new ConcurrentModel();
        String view = filmSessionController.getAllSessions(model, session);
        Object actualFilmDtos = model.getAttribute("filmSessionDtos");

        assertThat(view).isEqualTo("/film_sessions/list");
        assertThat(actualFilmDtos).isEqualTo(expectedFilmSessionDtos);
    }

}