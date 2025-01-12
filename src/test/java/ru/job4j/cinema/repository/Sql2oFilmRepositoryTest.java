package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.File;
import ru.job4j.cinema.model.Film;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oFilmRepositoryTest {

    private static Sql2oFilmRepository sql2oFilmRepository;

    private static Sql2oFileRepository sql2oFileRepository;

    private static File file;

    @BeforeAll
    public static void initRepositories() throws Exception {
        Properties properties = new Properties();
        try (InputStream inputStream = Sql2oFilmRepositoryTest.class.getClassLoader()
                .getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        String url = properties.getProperty("datasource.url");
        String username = properties.getProperty("datasource.username");
        String password = properties.getProperty("datasource.password");

        DatasourceConfiguration configuration = new DatasourceConfiguration();
        DataSource datasource = configuration.connectionPool(url, username, password);
        Sql2o sql2o = configuration.databaseClient(datasource);

        sql2oFilmRepository = new Sql2oFilmRepository(sql2o);
        sql2oFileRepository = new Sql2oFileRepository(sql2o);

        file = sql2oFileRepository.save(
                new File(7,
                        "test",
                        "path_test")
        );
    }

    @AfterAll
    public static void clearRepositories() {
        sql2oFileRepository.findAll().forEach(
                file -> sql2oFileRepository.deleteById(file.getId())
        );
    }

    @AfterEach
    public void clearFilms() {
        sql2oFilmRepository.findAll().forEach(
                film -> sql2oFilmRepository.deleteById(film.getId())
        );
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(sql2oFileRepository.deleteById(0))
                .isFalse();
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        assertThat(sql2oFilmRepository.findAll())
                .isEqualTo(emptyList());
        assertThat(sql2oFilmRepository.findById(0))
                .isEqualTo(empty());
    }

    @Test
    public void whenSaveThenGetSame() {
        Film film = new Film("name_test", "description_test",
                2025, 3, 16, 90, file.getId());
        film.setId(7);
        sql2oFilmRepository.save(film);

        Film savedFilm = sql2oFilmRepository
                .findById(film.getId()).get();
        assertThat(savedFilm).usingRecursiveComparison()
                .isEqualTo(film);
    }

    @Test
    public void whenSaveSeveralThenGetAll() {
        Film film1 = new Film("name_1", "description_1",
                2024, 3, 12, 60, file.getId());
        film1.setId(3);
        Film film2 = new Film("name_2", "description_2",
                2025, 4, 15, 90, file.getId());
        film2.setId(5);
        Film film3 = new Film("name_3", "description_3",
                2026, 5, 17, 120, file.getId());
        film2.setId(7);

        List.of(film1, film2, film3).forEach(
                film -> sql2oFilmRepository.save(film)
        );

        Collection<Film> result = sql2oFilmRepository.findAll();

        assertThat(result).isEqualTo(List.of(film1, film2, film3));
    }

    @Test
    public void whenDeleteThenGetEmptyOptional() {
        Film film = new Film("name_test", "description_test",
                2025, 3, 16, 90, file.getId());
        film.setId(7);
        sql2oFilmRepository.save(film);

        boolean isDeleted = sql2oFilmRepository.deleteById(film.getId());
        Optional<Film> savedFilm = sql2oFilmRepository.findById(film.getId());

        assertThat(isDeleted).isTrue();
        assertThat(savedFilm).isEqualTo(empty());
    }

    @Test
    public void whenUpdateThenGetUpdated() {
        Film film = new Film("name_test", "description_test",
                2024, 3, 12, 60, file.getId());
        film.setId(7);
        sql2oFilmRepository.save(film);

        Film updatedFilm = new Film("new_test", "new_description",
                2025, 5, 16, 90, file.getId());
        updatedFilm.setId(film.getId());
        boolean isUpdated = sql2oFilmRepository.update(updatedFilm);
        Film savedFilm = sql2oFilmRepository.findById(updatedFilm.getId()).get();

        assertThat(isUpdated).isTrue();
        assertThat(savedFilm).usingRecursiveComparison().isEqualTo(updatedFilm);
    }

    @Test
    public void whenUpdateUnExistingVacancyThenGetFalse() {
        Film film = new Film("name_test", "description_test",
                2024, 3, 12, 60, file.getId());
        film.setId(7);

        boolean isUpdated = sql2oFilmRepository.update(film);

        assertThat(isUpdated).isFalse();
    }
}
