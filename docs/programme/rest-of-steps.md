# Step 1

* Set up Jenkins and configure job that kicks off the tests
* Read about TDD, BDD

# Step - JDBC

- Implement transactions that span multiple method calls
- Read about ThreadLocal and now implement these using it
- See emerging patterns? Looks like the code is cluttered with a lot of repeating code. How can we separate our logic 
from the transaction management using a Proxy Pattern?
- Make sure your DAO tests begin and rollback transactions. Otherwise your DAO are still working in autoCommit mode.
- Add AOP to make transactions declarative
- Use Spring Tx and Spring AOP to implement transactions
- Read about Transaction Propagation
- Add DB Pool

# Step 4

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
