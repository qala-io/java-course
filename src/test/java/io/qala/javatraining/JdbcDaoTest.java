package io.qala.javatraining;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:/dao-context.xml") @ActiveProfiles("jdbc-dao")
@Target(ElementType.TYPE) @Retention(RetentionPolicy.RUNTIME)
public @interface JdbcDaoTest {}
