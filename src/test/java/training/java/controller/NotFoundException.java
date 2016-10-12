package training.java.controller;

import com.jayway.restassured.module.mockmvc.response.MockMvcResponse;

class NotFoundException extends RuntimeException {
    NotFoundException(MockMvcResponse response) {
        super(response.statusLine() + ". " + response.asString());
    }
}
