package ru.job4j.cinema.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import ru.job4j.cinema.model.User;

import java.util.Optional;

public interface UserService {

    Optional<User> save(User user);

    Optional<User> findByEmailAndPassword(String email, String password);

    void addUserAsAttributeToModel(Model model, HttpSession session);

}
