package io.qala.javatraining.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.qala.javatraining.utils.DateUtil;
import io.qala.javatraining.utils.Past;
import io.qala.javatraining.utils.NotBlankSized;

import jakarta.validation.constraints.DecimalMin;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.UUID;

import static io.qala.datagen.RandomShortApi.*;

public class Dog {
    private String id = UUID.randomUUID().toString();
    @NotBlankSized(min = 1, max = 100)
    private String name;
    /**
     * To pass the object over network we usually use ISO8601 format. That's where Offset based time is ideal since
     * ISO8601 relies on the offsets rather than zone ids. Hence the {@code XXXX} that uses the offsets.
     * <p>
     * It's important to use {@code uuuu} and not {@code yyyy} for years as
     * <a href="http://qala.io/blog/your-api-does-not-support-bc-time.html">yyyy supports only positive values</a>.
     * </p>
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "uuuu-MM-dd'T'HH:mm:ss.SSSXXXX")
    @Past
    private OffsetDateTime timeOfBirth;
    @DecimalMin(value = "0", inclusive = false)
    private double weight;
    @DecimalMin(value = "0", inclusive = false)
    private double height;

    public static Dog random() {
        Dog dog = new Dog();
        dog.name = alphanumeric(1, 100);
        Instant randomInstant = Instant.ofEpochMilli(Long(DateUtil.beginningOfTime(), System.currentTimeMillis()));
        dog.timeOfBirth = nullOr(OffsetDateTime.ofInstant(randomInstant, ZoneId.systemDefault()));

        dog.weight = positiveDouble();
        dog.height = positiveInteger();
        return dog;
    }

    public String getId() {
        return id;
    }

    @SuppressWarnings(/*Used by ORM*/"unused")
    public Dog setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Dog setName(String name) {
        this.name = name;
        return this;
    }

    public OffsetDateTime getTimeOfBirth() {
        return timeOfBirth;
    }

    public Dog setTimeOfBirth(OffsetDateTime timeOfBirth) {
        this.timeOfBirth = timeOfBirth;
        return this;
    }

    public double getWeight() {
        return weight;
    }

    public Dog setWeight(double weight) {
        this.weight = weight;
        return this;
    }

    public double getHeight() {
        return height;
    }

    public Dog setHeight(double height) {
        this.height = height;
        return this;
    }

    @Override public String toString() {
        return "Dog{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dog dog = (Dog) o;
        return Objects.equals(id, dog.id);

    }

    @Override public int hashCode() {
        return Objects.hashCode(id);
    }
}
