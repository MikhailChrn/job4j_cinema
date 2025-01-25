package ru.job4j.cinema.dto;

import ru.job4j.cinema.model.Ticket;

import java.util.*;

public class FilmSessionDto {

    private int id;

    private String name;

    private String hall;

    private String startTime;

    private int price;

    private int filmId;

    private final List<Ticket> tickets;

    public FilmSessionDto() {
        this.tickets = null;
    }

    public FilmSessionDto(int id,
                          String name,
                          String hall,
                          String startTime,
                          int price,
                          int filmId) {
        this.id = id;
        this.name = name;
        this.hall = hall;
        this.startTime = startTime;
        this.price = price;
        this.filmId = filmId;
        this.tickets = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHall() {
        return hall;
    }

    public void setHall(String hall) {
        this.hall = hall;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getFilmId() {
        return filmId;
    }

    public void setFilmId(int filmId) {
        this.filmId = filmId;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FilmSessionDto that = (FilmSessionDto) o;
        return id == that.id
                && Objects.equals(hall, that.hall)
                && Objects.equals(startTime, that.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hall, startTime);
    }
}





