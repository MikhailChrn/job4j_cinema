package ru.job4j.cinema.repository;

import org.junit.jupiter.api.*;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.*;

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

class Sql2oTicketRepositoryTest {

    private static Sql2oTicketRepository sql2oTicketRepository;

    private static Sql2oFilmSessionRepository sql2oFilmSessionRepository;

    private static Sql2oFileRepository sql2oFileRepository;

    private static File file;

    private static Film film;

    private static FilmSession filmSession;

    private static Sql2oFilmRepository sql2oFilmRepository;

    @BeforeAll
    public static void initRepositories() throws Exception {
        Properties properties = new Properties();
        try (InputStream inputStream = Sql2oTicketRepositoryTest.class.getClassLoader()
                .getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        String url = properties.getProperty("datasource.url");
        String username = properties.getProperty("datasource.username");
        String password = properties.getProperty("datasource.password");

        DatasourceConfiguration configuration = new DatasourceConfiguration();
        DataSource datasource = configuration.connectionPool(url, username, password);
        Sql2o sql2o = configuration.databaseClient(datasource);

        sql2oTicketRepository = new Sql2oTicketRepository(sql2o);
        sql2oFilmSessionRepository = new Sql2oFilmSessionRepository(sql2o);
        sql2oFileRepository = new Sql2oFileRepository(sql2o);
        sql2oFilmRepository = new Sql2oFilmRepository(sql2o);

        file = sql2oFileRepository.save(
                new File("test", "path-test")).get();

        film = new Film("name_test", "description_test",
                2025, 3, 16, 90, file.getId());

        sql2oFilmRepository.save(film);

        LocalDateTime startTime = LocalDateTime.of(2025, 1, 20,
                15, 15);
        LocalDateTime endTime = startTime.plusMinutes(film.getDurationInMinutes());

        filmSession = sql2oFilmSessionRepository.save(
                new FilmSession(99, film.getId(), 3, startTime, endTime, 200));

    }

    @AfterAll
    public static void clearRepositories() {
        sql2oFilmSessionRepository.findAll().forEach(
                filmSession -> sql2oFilmSessionRepository
                        .deleteById(filmSession.getId())
        );

        sql2oFilmRepository.findAll().forEach(
                film -> sql2oFilmRepository.deleteById(film.getId())
        );

        sql2oFileRepository.findAll().forEach(
                file -> sql2oFileRepository.deleteById(file.getId())
        );
    }

    @AfterEach
    public void clearTickets() {
        sql2oTicketRepository.findAll().forEach(
                ticket -> sql2oTicketRepository
                        .deleteById(ticket.getId())
        );
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(sql2oTicketRepository.deleteById(0))
                .isFalse();
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        assertThat(sql2oTicketRepository.findAll())
                .isEqualTo(emptyList());
        assertThat(sql2oTicketRepository.findById(0))
                .isEqualTo(empty());
    }

    @Test
    public void whenSaveThenGetSame() {
        Ticket ticket = sql2oTicketRepository.save(
                new Ticket(1, filmSession.getId(), 2, 2, 0));

        Ticket savedTicket = sql2oTicketRepository
                .findById(ticket.getId()).get();

        assertThat(savedTicket).usingRecursiveComparison()
                .isEqualTo(ticket);
    }

    @Test
    public void whenSaveSeveralThenGetAll() {
        Ticket ticket1 = sql2oTicketRepository.save(
                new Ticket(1, filmSession.getId(), 1, 1, 0));
        Ticket ticket2 = sql2oTicketRepository.save(
                new Ticket(2, filmSession.getId(), 2, 2, 0));
        Ticket ticket3 = sql2oTicketRepository.save(
                new Ticket(3, filmSession.getId(), 3, 3, 0));

        Collection<Ticket> result = sql2oTicketRepository.findAll();

        assertThat(result).isEqualTo(List.of(ticket1, ticket2, ticket3));
    }

    @Test
    public void whenDeleteThenGetEmptyOptional() {
        Ticket ticket = sql2oTicketRepository.save(
                new Ticket(1, filmSession.getId(), 2, 2, 0));

        boolean isDeleted = sql2oTicketRepository
                .deleteById(ticket.getId());

        Optional<Ticket> savedTicket = sql2oTicketRepository
                .findById(ticket.getId());

        assertThat(isDeleted).isTrue();
        assertThat(savedTicket).isEqualTo(empty());
    }

    @Test
    public void whenUpdateThenGetUpdated() {
        Ticket ticket = sql2oTicketRepository.save(
                new Ticket(1, filmSession.getId(), 1, 1, 0));

        Ticket updatedTicket =
                new Ticket(ticket.getId(), filmSession.getId(), 3, 3, 0);

        boolean isUpdated = sql2oTicketRepository
                .update(updatedTicket);
        Ticket savedTicket = sql2oTicketRepository
                .findById(updatedTicket.getId()).get();

        assertThat(isUpdated).isTrue();
        assertThat(savedTicket).usingRecursiveComparison()
                .isEqualTo(updatedTicket);
    }

    @Test
    public void whenUpdateUnExistingVacancyThenGetFalse() {
        Ticket ticket =
                new Ticket(3, filmSession.getId(), 3, 3, 0);

        boolean isUpdated = sql2oTicketRepository.update(ticket);

        assertThat(isUpdated).isFalse();
    }
}