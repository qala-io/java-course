package io.qala.javatraining.service.person;

import java.util.List;

class Person {
    private List projects;
    private List relatives;
    private long id;

    List getProjects() {
        return projects;
    }

    Person setProjects(List projects) {
        this.projects = projects;
        return this;
    }

    List getRelatives() {
        return relatives;
    }

    Person setRelatives(List relatives) {
        this.relatives = relatives;
        return this;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
