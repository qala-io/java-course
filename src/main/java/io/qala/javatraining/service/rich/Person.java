package io.qala.javatraining.service.rich;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

class Person {
    private List projects;
    private List relatives;
    private long id;

    PersonStatistics buildStatistics() {
        PersonStatistics result = new PersonStatistics(this);
        if(CollectionUtils.isNotEmpty(getRelatives())) result.setNumOfRelatives(getRelatives().size());
        if(CollectionUtils.isNotEmpty(getProjects( ))) result.setNumOfProjects(getProjects().size());
        return result;
    }

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
