Transactions & AOP
------------------

By now your transaction management looks pretty cumbersome, should be something like this:

```
connection.setAutoCommit(false);
try {
  //some SQL related logic
  connection.commit();
} catch(Throwable t) {
  connection.rollback();
} finally {
  closeConnection(connection);
}
```

This is called Programmatic Transaction Management. Fortunately there are ways to make it look shorter and nicer. 

# Step 1 - Introducing Service Facade

While DAO stores the logic to retrieve and write data to the storage, one business transaction can span multiple
storage related operations. E.g. if our Dog had an Owner and we were to write information both to `OWNER` and `DOG`
tables we would've used `DogDao` and `OwnerDao` for that. But that would mean that our transactions cannot run on DAO 
level - we need to begin them somewhere higher to be able to rollback both SQL operations as one transaction.
 
We could do that in Controllers but they already are responsible to handle HTTP-related logic - we don't want to 
overload them with too much of unrelated code. So we need to introduce a new concept - Service Facade 
(Service for short). Facade is a GoF design pattern - it's an object that doesn't have heavy logic of its own but 
instead it orchestrates calls to other classes and methods. By doing that it joins together a lot of complicated logic 
forming a single workflow that's invoked altogether by the client code. In enterprise apps it usually invokes multiple 
DAOs, sends Emails, initiates its own requests to other systems. But in our tiny app it will just invoke DAO methods. 

- Create a class `DogService`, inject DAO into that class
- Add all the CRUD methods that you have in DAO and instead of having DAO in the Controller - inject this new service 
there. Now you have additional layer in the app. All the tests must keep passing.
- Think: now you need to move transaction-related logic to the services. That means that both Service and DAO would 
need to reference the same Connection in their methods. So should we begin a transaction in Service and then pass that
connection to the DAO methods? Wouldn't such API be ugly? Now we need to pass Connection to all the methods in DAO.
Could you come up with a better way to share the connection?
- Read & research: what's a `ThreadLocal`? Create your own ThreadLocal variables for fun before continuing. Look 
through examples on the Internet.
- Create class `JdbcConnectionHolder` and add methods to get thread-local connections. Inject it to DAO and use it
instead of directly working with DataSource.
- Now you're ready to move the transaction-related logic into the service. Inject your `JdbcConnectionHolder` to your
service and migrate all the transaction-related logic there as well. See how you can work with the same connection per
thread and not pass it from the service to the DAO? This is a common way of creating a global state per thread and
share it between many layers in the app.

# Step 2 - Proxy

Proxy is a general term for something that takes control and just passes it to other object. In programming there is
a GoF's Proxy Design Pattern that does just that. If I were to name single most important pattern to know - it would be
Proxy. 

- Explore Proxy Design Pattern. Implement some examples for fun before continuing.
- Create a Proxy for your `DogService`, call it `TransactionalDogService`. Make sure that this is what's used
in the Controller from now on.
- Migrate all the transaction-related logic to your proxy keeping your old DogService small and clean.

See how simple the service became now? This helps a lot since services often have a lot of logic - cluttering it with
try-catch-finally won't help with readability.

# Step 3 - JDK Dynamic Proxy

While we separated our business-related logic of service from transaction-related logic there is still a lot of 
repeating code in `TransactionalDogService` that would be nice to get rid of. But it gets worse when you have many
services that need to be wrapped with transactions. So the amount of duplication is going to increase with time.

- Read about JDK Dynamic `Proxy` and try some of code for fun before continuing.
- Implement a `TransactionalProxy` class that has the repeating logic from the `TransactionalDogService`. Wrap
`DogService` with this proxy.
- Now make sure that this proxy gets initialized and is injected into Controller and your test instead of previous 
(static) proxy. Feel free to remove it.
- Puzzle: without changing any of the code find a way to prove that your class is really proxied.
- Puzzle: find a way to exclude some methods from being proxied.

# Step 4 - Class Proxy with CGLIB

JDK Proxy is good but it works only when your classes have interfaces. What if they don't?

- Research how to create dynamic proxies with CGLIB
- Add CGLIB to your classpath and create a new `CglibTransactionalDogService`. Try removing your interface and use 
the new proxy for transaction management.
- Think & research: how is the CGLIB mechanism different from JDK Proxy?
- Think & research: what are the pros and cons of JDK Proxy vs. CGLIB Proxy? When would you prefer one over another?

# Step 5 - AspectJ AOP

Aspect Oriented Programming is a way of introducing cross-cutting (not related to the core logic) aspects to the code.
Some of the approaches can use proxies like Spring AOP, others can change the bytecode of the classes (like AspectJ).

- Get a firm grip on terms: Pointcut, Join Point, Advise, Aspect.
- Find the difference between compile-time and load-time weaving.
- Create your own compile-time annotation. Create an aspect that logs the method invocation if annotation is placed on
the invoked method.

# Step 6 - Spring AOP

Spring AOP is a wrapper over JDK Dynamic Proxy and CGLIB. It can use both types of dynamic proxies. In terms of Spring 
one is called _Interface Proxy_ and the other is _Class Proxy_. Spring AOP borrowed the pointcut expressions from
AspectJ and therefore their syntax are similar. But AspectJ provides load-time or compile-time AOP while SpringAOP 
provides runtime AOP. 

- Add Spring AOP as a dependency
- Create an aspect for DogService that adds transactional support. First try Class Proxy and then move to Interface 
Proxy.
- Think & research: when would you pick Spring AOP vs. AspectJ?

# Step 7 - Spring Tx & Spring JDBC

Spring has a lib that adds transaction support via Spring AOP. It already implements a logic around ThreadLocal to share
both Connection as well as information about currently running transactions. It support more than what we implemented
in our holder.

- Use Spring AOP & Sprint Tx to define methods that should be transactional
- Replace custom `JdbcConnectionHolder` with `org.springframework.jdbc.datasource.DataSourceUtils`. Debug and find
how it gets the Connections created by `TransactionManager`
- Remove `TransactionalDogService` proxy as you don't need it anymore 
- Prove that Sprint Tx actually is applied and it works in the app
- Replace Spring AOP config for transactions with `@Transactional` annotations - this is an alternative way of marking 
methods transactional
- Replace pure JDBC code with SpringJDBC (`JdbcTemplate`). Look through its code and find how it gets the connections.
- Read about Transaction Propagation in Spring Tx.

# Puzzles

*Tip*: in real life one of the top skills that a programmer needs is troubleshooting. Too often people spend days
finding a problem in the code and trying to fix it while others could do that within minutes. Among others being a good 
troubleshooter means:

- You understand how things work. If you don't - you learn how they work before guessing where the problems reside.
- You know tools. Most important are: search & grep, git blame, Debug, Thread Dump, Profiler, 
JMX, Heap Dump & Analyzers (e.g. MAT), Wireshark, various Linux utilities.

Learning tools doesn't come naturally - you have to explicitly spend time to get acquainted with them. For the next 
puzzle you'll need Debug and Thread Dumps. Please learn how to use them before approaching the task.

- There is a failing test in branch `puzzle-e3829782`. Find out why it's failing and fix it.