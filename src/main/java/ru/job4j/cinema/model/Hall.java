package ru.job4j.cinema.model;

import java.util.Map;
import java.util.Objects;

public class Hall {

    public static final Map<String, String> COLUMN_MAPPING = Map.of(
            "id", "id",
            "name", "name",
            "row_count", "row_count",
            "place_count", "place_count",
            "description", "description"
    );

    private int id;

    private String name;

    private int rowCount;

    private int placeCount;

    private String description;

    public Hall() {
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

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getPlaceCount() {
        return placeCount;
    }

    public void setPlaceCount(int placeCount) {
        this.placeCount = placeCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Hall hall = (Hall) o;
        return id == hall.id
                && rowCount == hall.rowCount
                && placeCount == hall.placeCount
                && Objects.equals(name, hall.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, rowCount, placeCount);
    }
}