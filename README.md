Java Dev Training
-----------------

# About the course
This is a program to train Java Devs to be ready to work in real-life projects with one of the most popular stacks 
that includes: 

* Maven
* H2, Hibernate (via hbm.xml files), HibernateValidator
* Spring IoC
* Tomcat, Spring MVC, Jackson2
* Testing: TestNG, Unitils, Datagen, MockMvc, RestAssured
* Git, GitHub
* CI/CD: Jenkins

By the end of this course a trainee would need to create a small RESTful app that allows to manage Dogs with their 
properties in the database.

# The app to implement throughout the course
Create an app that has CRUD (create/read/update/delete) operations implemented for object called Dog via REST. 

Properties of a dog: 

* name - 1-100 symbols 
* date of birth - must be before NOW, can be null
* height, weight - must be greater than 0

Protocol:

* `POST /dog` - create a new dog. 
* `GET /dog/{id}` - shows the dog’s props in JSON format
* `PUT /dog/{id}` - updates the dog. Whole dog JSON is sent in request body - it overrides all the fields in DB. 
* `DELETE /dog/{id}` - removes the dog from DB

The app needs to be tested on all levels:

* Unit tests to test validation
* DAO tests to test how the entities are saved into DB (H2 can be instantiated during test start)
* Component REST tests - use MockMVC
* System REST tests - run against fully deployed app, send actual HTTP requests

# Step 1

On the first meeting we cover all the technologies - their purpose and general overview.

Homework - Maven:

* Read about dependencies, transitive dependencies
* Read about Super POM, Effective POM
* Should be able to run diagnostics (mvn dependency:tree, mvn help:effective-pom)
* Build war file with Maven

Homework - Web:

* Purpose of web.xml, Spring MVC
* Deploy a WAR file on Tomcat
* Add Hello World servlet that accepts name from parameters and prints it on the screen

# Step 2

Homework - Maven

* Read about Maven Lifecycles, Phases, Plugins
* Read about dependency scopes

Homework - Spring IoC:

* Read Spring Reference docs on Container Overview.
* Homework - Practice:
* Spring MVC GET, DELETE, POST, PUT /dogs
* Create a static collection of dogs that is shown in JSON format when we access /dog URL
* Create a TestNG + RestAssured tests that accesses those endpoints, parses JSON and compares expected to the actual
* Set up Jenkins and configure job that kicks off the tests

# Step 3

Homework - Maven

* Repeat Super/Effective POM
* Lifecycles, Phases, Plugins
* Dependencies and their scopes, transitive dependencies, etc.

Homework - Testing

* Read about TDD, BDD
* Read Spring Reference docs on 
[Bean Overview](http://docs.spring.io/spring/docs/4.3.0.BUILD-SNAPSHOT/spring-framework-reference/htmlsingle/#beans-definition), 
[Dependency Injection](http://docs.spring.io/spring/docs/4.3.0.BUILD-SNAPSHOT/spring-framework-reference/htmlsingle/#beans-dependencies).

Homework - Practice

* Add a unique integer identifier to Dog
* Create DAO layer and move all static collections into that layer. Inject DAO objects into services with Spring IoC.
* Implement a unit test for DAO layer - it checks static collections against expected result
* Implement Component REST tests by using Spring’s MockMVC
* Ensure the test names follow BDD

# Step - JDBC

- Implement a JDBC DAO that creates a DataSource in the constructor and uses it to create connections in DAO
- Move DataSource creation and configuration to Spring Context and inject it into DAO
- Implement transactions that span multiple method calls
- Read about ThreadLocal and now implement these using it
- See emerging patterns? Looks like the code is cluttered with a lot of repeating code. How can we separate our logic 
from the transaction management using a Proxy Pattern?
- Make sure your DAO tests begin and rollback transactions. Otherwise your DAO are still working in autoCommit mode.
- Add AOP to make transactions declarative
- Use Spring Tx and Spring AOP to implement transactions
- Read about Transaction Propagation
- Implement DB migrations with Flyway
- Add DB Pool

# Step 4

Homework - Maven

* Read and start using dependencyManagement

Homework - Hibernate

* Read about Hibernate Proxies
* Read about Entity Lifecycle in Hibernate (PERSISTED, DETACHED, etc)
* Read about Identity Generators (ways to generate IDs)

Homework - Practice

* Implement an alternative implementation of DAO. Instead of re-writing the previous one, create interfaces that are implemented both by InMemory DAO and by Hibernate DAO. It should be possible to switch between DAO implementations between startups via Spring means (find your own way).
* Use embedded H2 when starting the app
* Implement Hibernate Dao Tests - they need to import spring context and start the embedded H2 DB
* Use Randomized Testing to isolate the tests

# Step 5


Homework - Practice

* Implement Transactions in the app
* Make the tests to rollback the transactions after each test
* You need to be able to prove that transactions are working

# Step 6 

Final step - check that everything is perfect. Homework:

* Implement validation with Hibernate Validator (see the validation rules at the top), test the validation
* Read about and configure DB Pool
* Read about and configure Thread Pools in the app server
* Read about and configure nginx to serve traffic at some different port. 
