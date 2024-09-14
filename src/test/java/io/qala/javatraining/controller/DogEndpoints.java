package io.qala.javatraining.controller;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import io.qala.javatraining.domain.Dog;

import java.util.List;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static java.util.Arrays.asList;
import static org.testng.Assert.assertEquals;

public class DogEndpoints {
    DogEndpoints(MockMvc mvc) {
        RestAssuredMockMvc.mockMvc(mvc);
        RestAssuredMockMvc.requestSpecification = new MockMvcRequestSpecBuilder().setContentType(ContentType.JSON).build();
    }

    Dog createDog(Dog dog) {
        return given().body(dog).post("/dog").andReturn().as(Dog.class);
    }
    ValidationRestError[] createDogAndFailValidation(Dog dog) {
        return given().body(dog).post("/dog").andReturn().as(ValidationRestError[].class);
    }
    Dog createDog() {
        return given().body(Dog.random()).post("/dog").andReturn().as(Dog.class);
    }

    Dog findDog(String id) {
        MockMvcResponse response = given().get("/dog/{id}", id);
        throwIfStatusIsNotSuccess(response);
        return response.andReturn().as(Dog.class);
    }

    List<Dog> listDogs() {
        MockMvcResponse response = given().get("/dog");
        throwIfStatusIsNotSuccess(response);
        return asList(response.andReturn().as(Dog[].class));
    }

    void deleteDog(String id) {
        MockMvcResponse response = given().delete("/dog/{id}", id).andReturn();
        throwIfStatusIsNotSuccess(response);
        assertEquals(response.body().asString(), "");
        assertEquals(response.statusCode(), HttpStatus.NO_CONTENT.value());
    }

    void deleteAll() {
        for(Dog dog: listDogs()) deleteDog(dog.getId());
    }

    private void throwIfStatusIsNotSuccess(MockMvcResponse response) {
        if (response.statusCode() == HttpStatus.NOT_FOUND.value()) throw new Error404Exception(response);
        if(response.statusCode() >= 300) throw new RuntimeException("Error: " + response.statusLine());
    }
}
