package io.qala.javatraining.controller;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

class MockMvcFactoryBean implements FactoryBean<MockMvc> {
    private final WebApplicationContext applicationContext;

    MockMvcFactoryBean(WebApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public MockMvc getObject() throws Exception {
        return MockMvcBuilders.webAppContextSetup(applicationContext).build();
    }

    @Override
    public Class<?> getObjectType() {
        return MockMvc.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
