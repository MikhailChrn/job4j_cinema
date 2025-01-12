package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.File;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.Collection;
import java.util.Optional;
import java.util.Properties;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oFileRepositoryTest {

    private static Sql2oFileRepository sql2oFileRepository;

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

        sql2oFileRepository = new Sql2oFileRepository(sql2o);
    }

    @AfterEach
    public void clearFiles() {
        Collection<File> files = sql2oFileRepository.findAll();
        for (File file : files) {
            sql2oFileRepository.deleteById(file.getId());
        }
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        assertThat(sql2oFileRepository.findAll())
                .isEqualTo(emptyList());
        assertThat(sql2oFileRepository.findById(0))
                .isEqualTo(empty());
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(sql2oFileRepository.deleteById(0))
                .isFalse();
    }

    @Test
    public void whenSaveThenGetSame() {
        File file = sql2oFileRepository.save(
                new File(7,
                        "test",
                        "test_path")
        );
        File savedFile = sql2oFileRepository
                .findById(file.getId())
                .get();
        assertThat(savedFile).usingRecursiveComparison()
                .isEqualTo(file);
    }

    @Test
    public void whenDeleteThenGetEmptyOptional() {
        File file = sql2oFileRepository.save(
                new File(7,
                        "test",
                        "test_path")
        );
        boolean isDeleted = sql2oFileRepository
                .deleteById(file.getId());
        Optional<File> savedVacancy = sql2oFileRepository
                .findById(file.getId());
        assertThat(isDeleted).isTrue();
        assertThat(savedVacancy).isEqualTo(empty());
    }
}