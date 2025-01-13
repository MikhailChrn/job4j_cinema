package ru.job4j.cinema.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.UserService;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {

    private UserService userService;

    private UserController userController;

    private HttpSession session;

    private HttpServletRequest request;

    @BeforeEach
    public void initServices() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
        request = mock(HttpServletRequest.class);
        session = mock(HttpSession.class);
    }

    @Test
    public void whenRequestUserRegisterPageThenGetRegisterPage() {
        String view = userController.getRegister();

        assertThat(view).isEqualTo("users/register");
    }

    @Test
    public void whenPostRegisterUserThenRedirectToVacanciesPage() {
        User user = new User(9, "name",
                "email@email.ru", "password");

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        when(userService.save(userArgumentCaptor.capture())).thenReturn(Optional.of(user));

        Model model = new ConcurrentModel();
        String view = userController.register(model, user);
        User actualUser = userArgumentCaptor.getValue();

        assertThat(view).isEqualTo("redirect:/index");
        assertThat(actualUser).isEqualTo(user);
    }

    @Test
    public void whenPostRegisterUserUnsuccessThenGetErrorPageWithMessage() {
        String expectedMessage
                = "Пользователь с такой почтой уже существует";

        when(userService.save(any(User.class))).thenReturn(Optional.empty());

        Model model = new ConcurrentModel();
        String view = userController.register(model, new User());
        Object actualExceptionMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualExceptionMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void whenRequestUserLoginPageThenGetUserLoginPage() {
        String view = userController.getLoginPage();

        assertThat(view).isEqualTo("users/login");
    }

    @Test
    public void whenPostLoginUserThenGetRedirectToVacanciesPage() {
        User user = new User(9, "name",
                "email@email.ru", "password");

        when(userService.findByEmailAndPassword(user.getEmail(), user.getPassword()))
                .thenReturn(Optional.of(user));
        when(request.getSession()).thenReturn(session);

        Model model = new ConcurrentModel();
        String view = userController.loginUser(user, model, request);

        assertThat(view).isEqualTo("redirect:/index");
    }

    @Test
    public void whenPostLoginUserUnsuccessThenGetLoginPageWithMessage() {
        String expectedMessage
                = "Почта или пароль введены неверно";

        when(userService.findByEmailAndPassword(any(String.class), any(String.class)))
                .thenReturn(Optional.empty());

        Model model = new ConcurrentModel();
        String view = userController.loginUser(new User(), model, request);
        Object actualExceptionMessage = model.getAttribute("error");

        assertThat(view).isEqualTo("users/login");
        assertThat(actualExceptionMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void whenRequestLogoutPageThenGetUserLoginPage() {
        String view = userController.logout(session);

        assertThat(view).isEqualTo("redirect:/users/login");
    }
}