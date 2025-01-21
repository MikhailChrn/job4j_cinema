package ru.job4j.cinema.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Ticket;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class Sql2oTicketRepository implements TicketRepository {

    private final Sql2o sql2o;

    public Sql2oTicketRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<Ticket> findById(int id) {
        try (Connection connection = sql2o.open()) {
            Query query = connection.createQuery(
                    "SELECT * FROM tickets WHERE id = :id"
            );
            query.addParameter("id", id);
            Ticket ticket = query.setColumnMappings(Ticket.COLUMN_MAPPING)
                    .executeAndFetchFirst(Ticket.class);
            return Optional.ofNullable(ticket);
        }
    }

    @Override
    public Collection<Ticket> findAll() {
        try (Connection connection = sql2o.open()) {
            Query query = connection.createQuery(
                    "SELECT * FROM tickets"
            );
            return query.setColumnMappings(Ticket.COLUMN_MAPPING)
                    .executeAndFetch(Ticket.class);
        }
    }

    @Override
    public Collection<Ticket> findBySessionId(int id) {
        try (Connection connection = sql2o.open()) {
            Query query = connection.createQuery(
                    "SELECT * FROM tickets WHERE session_id = :sessionId"
            );
            query.addParameter("sessionId", id);
            return query.setColumnMappings(Ticket.COLUMN_MAPPING)
                    .executeAndFetch(Ticket.class);
        }
    }

    @Override
    public Ticket save(Ticket ticket) {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    INSERT INTO tickets(session_id, row_number, place_number, user_id)
                    VALUES (:sessionId, :rowNumber, :placeNumber, :userId)
                    """;
            Query query = connection.createQuery(sql, true)
                    .addParameter("sessionId", ticket.getSessionId())
                    .addParameter("rowNumber", ticket.getRowNumber())
                    .addParameter("placeNumber", ticket.getPlaceNumber())
                    .addParameter("userId", ticket.getUserId());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            ticket.setId(generatedId);
            return ticket;
        }
    }

    @Override
    public boolean deleteById(int id) {
        boolean isDeleted;
        try (Connection connection = sql2o.open()) {
            Query query = connection.createQuery(
                    "DELETE FROM tickets WHERE id = :id"
            );
            query.addParameter("id", id);
            query.executeUpdate();
            isDeleted = connection.getResult() != 0;
        }
        return isDeleted;
    }

    @Override
    public boolean update(Ticket ticket) {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    UPDATE tickets
                    SET session_id = :sessionId, row_number = :rowNumber,
                    place_number = :placeNumber, user_id = :userId
                    WHERE id = :id
                    """;
            Query query = connection.createQuery(sql)
                    .addParameter("id", ticket.getId())
                    .addParameter("sessionId", ticket.getSessionId())
                    .addParameter("rowNumber", ticket.getRowNumber())
                    .addParameter("placeNumber", ticket.getPlaceNumber())
                    .addParameter("userId", ticket.getUserId());
            int affectedRows = query.executeUpdate().getResult();
            return affectedRows > 0;
        }
    }
}
