package io.qala.javatraining.service.person;

class PersonStatistics {
    private Person person;
    private Integer numOfProjects;
    private Integer numOfRelatives;

    PersonStatistics(Person p) {
        this.person = p;
    }

    Person getPerson() {
        return person;
    }

    Integer getNumOfProjects() {
        return numOfProjects;
    }

    PersonStatistics setNumOfProjects(Integer numOfProjects) {
        this.numOfProjects = numOfProjects;
        return this;
    }

    Integer getNumOfRelatives() {
        return numOfRelatives;
    }

    PersonStatistics setNumOfRelatives(Integer numOfRelatives) {
        this.numOfRelatives = numOfRelatives;
        return this;
    }
}
