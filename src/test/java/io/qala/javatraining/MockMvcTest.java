package io.qala.javatraining;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation that you need to put on Tests that simulate HTTP requests. Instead of actually sending an HTTP request
 * (which is slow) it works with Spring MVC directly to invoke the methods of the controllers/endpoints.
 * <p>
 * Annotations is a much better way to share common configuration than extending a common test class:
 * <ul>
 *     <li>This makes it easier to read the code (it's pretty hard to find things out when you have a whole hierarchy
 * of classes)</li>
 *     <li>It's more flexible since you can set multiple annotations on the same test class while there</li>
 * </ul>
 * </p>
 */
@WebAppConfiguration @ContextConfiguration(
        {"classpath:/test-context.xml", "classpath:/dao-context.xml", "classpath:/web-context.xml"})
@ActiveProfiles("jdbc-dao")
@Target(ElementType.TYPE) @Retention(RetentionPolicy.RUNTIME)
public @interface MockMvcTest {}
