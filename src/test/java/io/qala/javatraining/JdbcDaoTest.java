package io.qala.javatraining;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ContextConfiguration("classpath:/dao-context.xml") @ActiveProfiles("jdbc-dao")
@Target(ElementType.TYPE) @Retention(RetentionPolicy.RUNTIME)
public @interface JdbcDaoTest {}
