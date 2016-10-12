package training.java.domain;

import java.time.ZonedDateTime;
import java.util.UUID;

import static io.qala.datagen.RandomDate.beforeNow;
import static io.qala.datagen.RandomShortApi.*;

public class Dog {
    private String id = UUID.randomUUID().toString();
    private String name;
    private ZonedDateTime timeOfBirth;
    private double weight;
    private double height;

    public static Dog random() {
        Dog dog = new Dog();
        dog.name = alphanumeric(1, 100);
        dog.timeOfBirth = nullOr(beforeNow().zonedDateTime());
        dog.weight = positiveDouble();
        dog.height = positiveInteger();
        return dog;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getTimeOfBirth() {
        return timeOfBirth;
    }

    public void setTimeOfBirth(ZonedDateTime timeOfBirth) {
        this.timeOfBirth = timeOfBirth;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
