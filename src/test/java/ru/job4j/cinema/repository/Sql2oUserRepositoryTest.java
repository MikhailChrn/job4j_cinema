package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.User;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.Collection;
import java.util.Optional;
import java.util.Properties;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oUserRepositoryTest {

    private static Sql2oUserRepository sql2oUserRepository;

    @BeforeAll
    public static void initRepositories() throws Exception {
        Properties properties = new Properties();
        try (InputStream inputStream = Sql2oUserRepositoryTest.class.getClassLoader()
                .getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        String url = properties.getProperty("datasource.url");
        String username = properties.getProperty("datasource.username");
        String password = properties.getProperty("datasource.password");

        DatasourceConfiguration configuration = new DatasourceConfiguration();
        DataSource datasource = configuration.connectionPool(url, username, password);
        Sql2o sql2o = configuration.databaseClient(datasource);

        sql2oUserRepository = new Sql2oUserRepository(sql2o);
    }

    @AfterEach
    public void clearUsers() {
        Collection<User> users = sql2oUserRepository.findAll();
        for (User user : users) {
            sql2oUserRepository.deleteById(user.getId());
        }
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(sql2oUserRepository.deleteById(0))
                .isFalse();
    }

    @Test
    public void whenSaveThenGetSame() {
        Optional<User> user = sql2oUserRepository.save(
                new User(7, "test test",
                        "test@test.ru",
                        "test"));
        User savedUser = sql2oUserRepository
                .findByEmailAndPassword(user.get().getEmail(),
                        user.get().getPassword())
                .get();
        assertThat(savedUser).usingRecursiveComparison()
                .isEqualTo(user.get());
    }

    @Test
    public void whenDeleteThenGetEmptyOptional() {
        Optional<User> user = sql2oUserRepository.save(
                new User(7, "test test",
                        "test@test.ru",
                        "test"));
        boolean isDeleted = sql2oUserRepository
                .deleteById(user.get().getId());
        Optional<User> savedVacancy = sql2oUserRepository
                .findById(user.get().getId());
        assertThat(isDeleted).isTrue();
        assertThat(savedVacancy).isEqualTo(empty());
    }
}