package ru.job4j.cinema.dto;

import java.util.Objects;

public class TicketMyDto {

    private int id;

    private String filmTitle;

    private String hallTitle;

    private String startTime;

    private int rowNumber;

    private int placeNumber;

    public TicketMyDto() {
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

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getPlaceNumber() {
        return placeNumber;
    }

    public void setPlaceNumber(int placeNumber) {
        this.placeNumber = placeNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TicketMyDto that = (TicketMyDto) o;
        return id == that.id
                && rowNumber == that.rowNumber
                && placeNumber == that.placeNumber
                && Objects.equals(filmTitle, that.filmTitle)
                && Objects.equals(hallTitle, that.hallTitle)
                && Objects.equals(startTime, that.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,
                filmTitle,
                hallTitle,
                startTime,
                rowNumber,
                placeNumber);
    }
}
