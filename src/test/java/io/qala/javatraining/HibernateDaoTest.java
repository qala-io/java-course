package io.qala.javatraining;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ContextConfiguration("classpath:/dao-context.xml") @ActiveProfiles("hibernate-dao")
@Target(ElementType.TYPE) @Retention(RetentionPolicy.RUNTIME)
public @interface HibernateDaoTest {}
