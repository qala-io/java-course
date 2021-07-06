Spring & REST
-------------

# Spring IoC

Historically Spring played a huge role in Java community and it all started with IoC implementation. Suppose your Dog 
has a House. IoC (Inversion of Control) is an approach of moving the responsibility for creating dependencies (House) 
from owning objects (Dog) to some other objects so that you don't need to put `new House(...)` right in the Dog code 
(which leads to all sorts of problems). Most popular implementations of this approach are:

* **Factories**. The key here is that the owning object (Dog) would explicitly ask a factory (HouseFactory) to create 
him a House: `houseFactory.create(...)`. This implementation has limitations that are hard to overcome - sometimes 
you need to provide the _same_ instance of the House to multiple owning objects.
* **Dependency Injection** (DI). This represents basically a factory for all the objects in the application. And this
time it's the DI mechanism who proactively injects the dependencies (House) into the owning objects (Dogs). This 
way it's very trivial to provide the same dependency to multiple objects. Additionally it becomes trivial to test 
the owning objects as you may override its dependencies in the same way that DI mechanism uses - passing them into
 constructors or via setters. This is the approach that Spring IoC took.

## Some History

Why SpringSource became so popular? Back in the days there was this monster that was eating people's brains - 
Enterprise Java Beans (EJB2 to be precisely - today we have EJB3 that fixed most of the problems its ancestor had). 
The monster was making developers implement complicated interfaces and even worse - 
you couldn't initialize your code outside of its container (J2EE App Servers). That effectively meant that oftentimes 
you couldn't write unit tests for the classes that you create because there was no way of creating them in the 
tests.

Spring IoC came to rescue. Its main goal was to provide a **non-intrusive** container - you just write your POJOs 
(simple class that didn't depend on the container) and create them in any way you want - either by Spring IoC or 
manually (mostly in tests). 

But today's SpringSource is different. While originally it strove for simplicity today it's becoming more and more
like the heavy technologies it was fighting against. It still provides an ability to be non-intrusive, but in the same
time it _encourages_ the opposite - `@Autowired` on private members, package scans, Spring Boot, etc. So nowadays we
may already be in need for better solutions.

## Other IoC implementations

**Google Guice** - is an alternative but a more lightweight DI implementation. By the time it was introduced the world
was already using Spring IoC extensively so it never became as popular.

**CDI** - recent JavaEE spec that was inspired by Spring IoC. It uses a different set of annotations (e.g. `@Inject` 
instead of `@Autowired`). Today's App Servers implement this spec and allow for DI without Spring. The latter also
implements this spec and recognizes CDI annotations.

# REST

REST is an architecture based on several simple principles:

* Application provides resources - client can invoke simple operations on them like creating, getting, deleting, 
updating.
* It leverages HTTP methods (strictly saying it can be based on other protocols, but I haven't seen it in the wild) to
trigger those operations
* It tries to be as stateless as possible
* It uses hypermedia to guide clients to the API instead of making clients guess

There are 2 kinds of frameworks in Java for REST - JAX-RS spec implementations (Jersey, RestEasy) and others (Spring MVC). 
Due to its popularity and integration with Spring IoC we will use Spring MVC framework for the course.
 
# Step 1 - HTTP

* Learn how network protocols are built on top of lower-layer protocols (HTTP over TCP, TCP over IP) 
([link](./articles/networking-layers.md))
* Get the idea of how domain names work ([link](./articles/dns.md))
* Learn the most widespread HTTP methods: GET, POST, PUT, DELETE, PATCH, HEAD.
* Learn what's the Mime Type
* Read about `Content-Type` and `Accept` headers [link](https://webmasters.stackexchange.com/questions/31212/difference-between-the-accept-and-content-type-http-headers)
* Get a general idea of 2xx, 3xx, 4xx and 5xx response codes (we'll dive deeper into them later) [link](https://www.yeahhub.com/1xx-2xx-3xx-4xx-5xx-http-status-codes/)

# Step 2 - REST Basics

* Learn how RPC is different from REST ([link](./articles/rpc-vs-rest.md))
* Learn what a Resource is and how it's identified
* Use a pen and paper to draft URIs and methods for creating, fetching single, listing many, deleting and updating dogs.
* Take a closer look at PUT and POST - what's the difference between them? How would you apply that difference to the
Dogs API?
* Take a closer look at PUT and PATCH - what's the difference between them? How would you apply that difference to the
Dogs API? [link](https://fullstack-developer.academy/restful-api-design-post-vs-put-vs-patch/)

*Tip*: make sure you know by heart which of the popular HTTP methods are idempotent. Usually when you don't know which 
method to choose for operations - idempotency solves it for you. 

# Step 3 - Spring IoC

- Learn different bean scopes (singleton, prototype, thread, request, session)
- Try to create Spring Contexts via XML and Java configuration
- Learn what autowired is and how it's defined in XML and Java configurations
- Read about package-scan and annotation-marked beans
- Try creating multiple contexts and importing one in another
- Learn about profiles. Research and think about why this is typically a bad practice to use them. Compare them to 
importing contexts.
- Spring Core Tutorial [link](https://www.journaldev.com/2888/spring-tutorial-spring-core-tutorial)
- Logging dependencies in Spring [link](https://spring.io/blog/2009/12/04/logging-dependencies-in-spring/)
- Web Application Flow [link](https://stackoverflow.com/questions/25904298/why-jsp-files-inside-web-inf-folder-works-but-placed-under-a-folder-under-web)

# Step 4 - Spring MVC

* Learn what Front Controller design pattern is
* Learn what `org.springframework.web.servlet.DispatcherServlet` is and how it's defined in web.xml
* Make sure you understand the difference between `DispatcherServlet` and 
`org.springframework.web.context.ContextLoaderListener`
* Learn the difference between `@RestController` and `@Controller`
* Difference between AppContext and WebAppContext [link](https://www.dineshonjava.com/difference-between-applicationcontext-webapplicationcontext-in-spring-mvc/)
* [Tutorial](https://www.dineshonjava.com/difference-between-applicationcontext-webapplicationcontext-in-spring-mvc/)
* [Spring MVC](https://www.journaldev.com/2433/spring-mvc-tutorial)

# Step 5 - Create REST app 

* Define `DispatcherServlet`, create a class that's going to be your `@RestController`, create an XML Spring context 
with a bean of that class
* Create methods for creating, getting, updating, deleting dogs. All the messages must be in JSON. All the data is kept 
in static collection in the controller itself. See the [list of fields](../reqs.md).
* Write TestNG + RestAssured tests that cover all straight forward and corner cases
* Jackson Exceptions [link](https://www.baeldung.com/jackson-exception)

# Step 6 - Validation

* Read about Bean Validation and HibernateValidator [java-doc](http://docs.jboss.org/hibernate/validator/4.1/api/org/hibernate/validator/constraints/impl/package-summary.html)
* Add [validation rules](../reqs.md) to your classes, make sure your API returns correct error codes (see previous steps)
* Write unit tests to check that validation rules are set up correctly
* Write RestAssured tests to check that validation rules are getting invoked [Usage](https://github.com/rest-assured/rest-assured/wiki/Usage)
* Write MockMvc tests - these should duplicate your RestAssured tests
* Think about the difference between RestAssured and MockMvc - which tests should test what. Can we eliminate the 
duplication?
* Seperating test cases [link](https://semaphoreci.com/community/tutorials/how-to-split-junit-tests-in-a-continuous-integration-environment)

# Step 7 - Advanced REST

- Read about cache and the respective headers
- Read about Publish-Subscribe notifications
- Read about Atom Syndicate Protocol and how it solves Pub-Sub architecture for REST
- Learn what HATEOAS is and think of how this can be used in your app

# Literature

[REST in Practice](http://shop.oreilly.com/product/9780596805838.do) - a great book that shows how to build a high 
quality REST API with caching, events, hypermedia, black jack, hookers.

# Other links
- CDI [link](https://www.baeldung.com/java-ee-cdi)
- Microservices [link](https://microservices.io)
- Slf4J vs JCL [link](https://v4forums.wordpress.com/2008/12/27/slf4j-vs-jcl-dynamic-binding-vs-static-binding/)
- Commons Logging [link](https://www.logicbig.com/tutorials/misc/java-logging/apache-commons-logging.html)
- SLF4J [link](https://www.logicbig.com/tutorials/misc/java-logging/slf4j.html)
- Apache CFX vs Spring REST [link](https://stackoverflow.com/questions/25083045/spring-boot-vs-apache-cxf-for-restful-web-services)
- Spring REST Basics [link](https://dzone.com/articles/lifecycle-of-a-request-response-process-for-a-spri)
- [High Performance Browser Networking](https://hpbn.co/) - describes how web works, which protocols exist, their 
strengths, weaknesses, pitfalls. Shows how to get everything out of your web infrastructure.
