package io.qala.javatraining.controller;

import com.jayway.restassured.module.mockmvc.response.MockMvcResponse;

class Error404Exception extends RuntimeException {
    Error404Exception(MockMvcResponse response) {
        super(response.statusLine() + ". " + response.asString());
    }
}
