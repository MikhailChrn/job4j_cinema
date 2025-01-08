package ru.job4j.cinema.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Genre;

import java.util.Collection;
import java.util.Optional;

@Repository
public class Sql2OGenreRepository implements GenreRepository {

    private final Sql2o sql2o;

    public Sql2OGenreRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<Genre> findById(int id) {
        try (Connection connection = sql2o.open()) {
            Query query = connection.createQuery(
                    "SELECT * FROM genres WHERE id = :id"
            );
            query.addParameter("id", id);
            Genre genre = query.setColumnMappings(Genre.COLUMN_MAPPING)
                    .executeAndFetchFirst(Genre.class);
            return Optional.ofNullable(genre);
        }
    }

    @Override
    public Optional<Genre> findByName(String name) {
        try (Connection connection = sql2o.open()) {
            Query query = connection.createQuery(
                    "SELECT * FROM genres WHERE name = :name"
            );
            query.addParameter("name", name);
            Genre genre = query.setColumnMappings(Genre.COLUMN_MAPPING)
                    .executeAndFetchFirst(Genre.class);
            return Optional.ofNullable(genre);
        }
    }

    @Override
    public Collection<Genre> findAll() {
        try (Connection connection = sql2o.open()) {
            Query query = connection.createQuery(
                    "SELECT * FROM genres"
            );
            return query.setColumnMappings(Genre.COLUMN_MAPPING)
                    .executeAndFetch(Genre.class);
        }
    }
}
