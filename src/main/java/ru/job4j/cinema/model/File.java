package ru.job4j.cinema.model;

import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

public class File {

    public static final Map<String, String> COLUMN_MAPPING = Map.of(
            "id", "id",
            "name", "name",
            "path", "path"
    );

    private int id;

    private String name;

    private String path;

    public File() {
    }

    public File(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public File(int id, String name, String path) {
        this.id = id;
        this.name = name;
        this.path = path;
    }

    public File(int id, String test, Path first) {
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        File file = (File) o;
        return id == file.id && Objects.equals(path, file.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, path);
    }
}
