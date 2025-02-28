package ru.job4j.cinema.dto;

import ru.job4j.cinema.model.Ticket;

import java.util.*;

public class FilmSessionDto {

    private int id;

    private String filmTitle;

    private String hallTitle;

    private String startTime;

    private int price;

    private final List<Ticket> tickets;

    public FilmSessionDto() {
        this.tickets = null;
    }

    public FilmSessionDto(int id,
                          String filmTitle,
                          String hallTitle,
                          String startTime,
                          int price) {
        this.id = id;
        this.filmTitle = filmTitle;
        this.hallTitle = hallTitle;
        this.startTime = startTime;
        this.price = price;
        this.tickets = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilmTitle() {
        return filmTitle;
    }

    public void setFilmTitle(String filmTitle) {
        this.filmTitle = filmTitle;
    }

    public String getHallTitle() {
        return hallTitle;
    }

    public void setHallTitle(String hallTitle) {
        this.hallTitle = hallTitle;
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
                && Objects.equals(hallTitle, that.hallTitle)
                && Objects.equals(startTime, that.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hallTitle, startTime);
    }
}





