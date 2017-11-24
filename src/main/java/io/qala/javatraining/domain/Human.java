package io.qala.javatraining.domain;

import java.util.Objects;

public class Human {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public Human setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Human setName(String name) {
        this.name = name;
        return this;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Human human = (Human) o;
        return id == human.id;
    }

    @Override public int hashCode() {
        return Objects.hash(id);
    }
}
