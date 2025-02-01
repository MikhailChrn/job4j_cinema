package ru.job4j.cinema.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.File;

import java.util.Collection;
import java.util.Optional;

@Repository
public class Sql2oFileRepository implements FileRepository {

    private final Sql2o sql2o;

    public Sql2oFileRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<File> save(File file) {
        Optional<File> result = Optional.empty();
        try (Connection connection = sql2o.open()) {
            Query query = connection.createQuery(
                            "INSERT INTO files (name, path) VALUES (:name, :path)",
                            true)
                    .addParameter("name", file.getName())
                    .addParameter("path", file.getPath());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            file.setId(generatedId);
            result = Optional.ofNullable(file);
        }

        return result;
    }

    @Override
    public boolean deleteById(int id) {
        boolean isDeleted;
        try (Connection connection = sql2o.open()) {
            Query query = connection.createQuery(
                    "DELETE FROM files WHERE id = :id"
            );
            query.addParameter("id", id);
            query.executeUpdate();
            isDeleted = connection.getResult() != 0;
        }
        return isDeleted;
    }

    @Override
    public Optional<File> findById(int id) {
        try (Connection connection = sql2o.open()) {
            Query query = connection.createQuery(
                    "SELECT * FROM files WHERE id = :id");
            File file = query.addParameter("id", id).executeAndFetchFirst(File.class);
            return Optional.ofNullable(file);
        }
    }

    @Override
    public Collection<File> findAll() {
        try (Connection connection = sql2o.open()) {
            Query query = connection.createQuery(
                    "SELECT * FROM files"
            );
            return query.setColumnMappings(File.COLUMN_MAPPING)
                    .executeAndFetch(File.class);
        }
    }
}
