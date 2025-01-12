package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.File;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.FilmSession;

import javax.sql.DataSource;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oFilmSessionRepositoryTest {

    private static Sql2oFilmSessionRepository sql2oFilmSessionRepository;

    private static Sql2oFileRepository sql2oFileRepository;

    private static File file;

    private static Film film;

    private static Sql2oFilmRepository sql2oFilmRepository;

    @BeforeAll
    public static void initRepositories() throws Exception {
        Properties properties = new Properties();
        try (InputStream inputStream = Sql2oFilmSessionRepositoryTest.class.getClassLoader()
                .getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        String url = properties.getProperty("datasource.url");
        String username = properties.getProperty("datasource.username");
        String password = properties.getProperty("datasource.password");

        DatasourceConfiguration configuration = new DatasourceConfiguration();
        DataSource datasource = configuration.connectionPool(url, username, password);
        Sql2o sql2o = configuration.databaseClient(datasource);

        sql2oFilmSessionRepository = new Sql2oFilmSessionRepository(sql2o);

        sql2oFileRepository = new Sql2oFileRepository(sql2o);

        file = sql2oFileRepository.save(
                new File(7,
                        "test",
                        "path_test")
        );

        sql2oFilmRepository = new Sql2oFilmRepository(sql2o);

        film = new Film("name_test", "description_test",
                2025, 3, 16, 90, file.getId());
        film.setId(7);

        sql2oFilmRepository.save(film);
    }

    @AfterAll
    public static void clearRepositories() {
        sql2oFilmRepository.findAll().forEach(
                film -> sql2oFilmRepository.deleteById(film.getId())
        );

        sql2oFileRepository.findAll().forEach(
                file -> sql2oFileRepository.deleteById(file.getId())
        );
    }

    @AfterEach
    public void clearFilmSessions() {
        sql2oFilmSessionRepository.findAll().forEach(
                filmSession -> sql2oFilmSessionRepository
                        .deleteById(filmSession.getId())
        );
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(sql2oFilmSessionRepository.deleteById(0))
                .isFalse();
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        assertThat(sql2oFilmSessionRepository.findAll())
                .isEqualTo(emptyList());
        assertThat(sql2oFilmSessionRepository.findById(0))
                .isEqualTo(empty());
    }

    @Test
    public void whenSaveThenGetSame() {
        LocalDateTime startTime = LocalDateTime.of(2024, 1, 20,
                15, 15);
        LocalDateTime endTime = startTime.plusMinutes(film.getDurationInMinutes());
        FilmSession filmSession = sql2oFilmSessionRepository.save(
                new FilmSession(7, film.getId(), 3, startTime, endTime, 200));

        FilmSession savedFilmSession = sql2oFilmSessionRepository
                .findById(filmSession.getId()).get();
        assertThat(savedFilmSession).usingRecursiveComparison()
                .isEqualTo(filmSession);
    }

    @Test
    public void whenSaveSeveralThenGetAll() {
        LocalDateTime startTime = LocalDateTime.of(2024, 1, 20,
                15, 15);
        LocalDateTime endTime = startTime.plusMinutes(film.getDurationInMinutes());
        FilmSession filmSession1 = sql2oFilmSessionRepository.save(
                new FilmSession(1, film.getId(), 1, startTime, endTime, 200));
        FilmSession filmSession2 = sql2oFilmSessionRepository.save(
                new FilmSession(2, film.getId(), 2, startTime, endTime, 200));
        FilmSession filmSession3 = sql2oFilmSessionRepository.save(
                new FilmSession(3, film.getId(), 3, startTime, endTime, 200));

        Collection<FilmSession> result = sql2oFilmSessionRepository.findAll();

        assertThat(result).isEqualTo(List.of(filmSession1, filmSession2, filmSession3));
    }

    @Test
    public void whenDeleteThenGetEmptyOptional() {
        LocalDateTime startTime = LocalDateTime.of(2024, 1, 20,
                15, 15);
        LocalDateTime endTime = startTime.plusMinutes(film.getDurationInMinutes());
        FilmSession filmSession = sql2oFilmSessionRepository.save(
                new FilmSession(7, film.getId(), 3, startTime, endTime, 200));

        boolean isDeleted = sql2oFilmSessionRepository
                .deleteById(filmSession.getId());

        Optional<FilmSession> savedFilmSession = sql2oFilmSessionRepository
                .findById(filmSession.getId());

        assertThat(isDeleted).isTrue();
        assertThat(savedFilmSession).isEqualTo(empty());
    }

    @Test
    public void whenUpdateThenGetUpdated() {
        LocalDateTime startTime = LocalDateTime.of(2024, 1, 20,
                15, 15);
        LocalDateTime endTime = startTime.plusMinutes(film.getDurationInMinutes());
        FilmSession filmSession = sql2oFilmSessionRepository.save(
                new FilmSession(1, film.getId(), 1, startTime, endTime, 200));

        FilmSession updatedFilmSession = new FilmSession(filmSession.getId(), film.getId(),
                3, startTime, endTime, 200);

        boolean isUpdated = sql2oFilmSessionRepository
                .update(updatedFilmSession);
        FilmSession savedFilmSession = sql2oFilmSessionRepository
                .findById(updatedFilmSession.getId()).get();

        assertThat(isUpdated).isTrue();
        assertThat(savedFilmSession).usingRecursiveComparison()
                .isEqualTo(updatedFilmSession);
    }

    @Test
    public void whenUpdateUnExistingVacancyThenGetFalse() {
        LocalDateTime startTime = LocalDateTime.of(2024, 1, 20,
                15, 15);
        LocalDateTime endTime = startTime.plusMinutes(film.getDurationInMinutes());
        FilmSession filmSession =
                new FilmSession(1, film.getId(), 1, startTime, endTime, 200);

        boolean isUpdated = sql2oFilmSessionRepository.update(filmSession);

        assertThat(isUpdated).isFalse();
    }
}