package training.java.controller;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import com.jayway.restassured.module.mockmvc.response.MockMvcResponse;
import com.jayway.restassured.module.mockmvc.specification.MockMvcRequestSpecBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import training.java.domain.Dog;

import java.util.List;

import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static java.util.Arrays.asList;

public class DogEndpoints {
    DogEndpoints(MockMvc mvc) {
        RestAssuredMockMvc.mockMvc(mvc);
        RestAssuredMockMvc.requestSpecification = new MockMvcRequestSpecBuilder().setContentType(ContentType.JSON).build();
    }

    Dog findDog(String id) {
        MockMvcResponse response = given().get("/dog/{id}", id);
        if (response.statusCode() == HttpStatus.NOT_FOUND.value()) throw new Error404Exception(response);
        if(response.statusCode() >= 300) throw new RuntimeException("Error: " + response.statusLine());
        return response.andReturn().as(Dog.class);
    }

    List<Dog> listDogs() {
        MockMvcResponse response = given().get("/dog/");
        if (response.statusCode() >= 300) throw new RuntimeException("Error: " + response.statusLine());
        return asList(response.andReturn().as(Dog[].class));
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
}
