package training.java.controller;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import java.lang.annotation.*;

@WebAppConfiguration @ContextConfiguration({"classpath:/test-context.xml", "classpath:/web-context.xml"})
@Target(ElementType.TYPE) @Retention(RetentionPolicy.RUNTIME)
@interface MockMvcTest {}
