Database Pools
--------------

Previously we were creating a new Connection every time we needed it. But this is a heavy operation as it needs to make
some TCP handshake (we mostly communicate with DBs over network) and allocate resources on DB side. Then we do a small
SQL operation that's 100x times faster than the whole set up. And what do we do afterwards? We just close the 
connection.

To make it more effective we can re-use that Connection - finish the transaction and just store the Connection for the
next operations. But this speed comes with complications that we're going to cover in the next steps.

# Step 1 - General Approach

- Add C3P0 as a compile-time dependency 
- Create a `ComboPooledDataSource` that takes your original `DataSource`. From now on pass this new `DataSource` 
instead of the original one everywhere. 
- Debug and research the classes that now are returned by `ComboPooledDataSource` - is this the same H2 Connection 
that was before? Is that the same H2 `PreparedStatement` that was before? Does this remind you the design pattern we
talked before?
- Research and debug: what happens when you invoke `connection.close()`? 

# Step 2 - Basic Configuration

- Configure `maxPoolSize`, `minPoolSize`. What values would be most appropriate here? How can you correlate them with 
the thread pool configuration?
- Configure `maxStatements` and `maxStatementsPerConnection`. Why do you need these? How do you decide which values
to put there? How do these correlate with the number of dynamically generated SQL statements and static SQL statements?
 
# Step 3 - Testing Connections

We've been working with embedded DB so far - it doesn't use network and instead JDBC Driver invokes Java classes
of the DB Server directly. But in the majority of cases we use standalone DBs like MySQL, Postgre, Oracle that are 
located on remote hosts. To communicate with them a client-server interaction must happen. 

When such an interaction is initiated multiple things happen:

- A TCP connection is established
- A Server Connection (a.k.a. Session) is created on DB Side (in this case it's the server side). Such 
connections have settings, user information, they begin and end transactions, etc. 
- A Client Connection (`java.sql.Connection`) object is created in your code.

So what would happen if any of these die (e.g. TCP connection is interrupted or Server Connection closed)?

1. Client side does _not_ get notified. And even if it would, how could it remove itself from the DB Pool? Thus DB Pool 
can't know that the connection died and will return the broken `java.sql.Connection` to your code.
2. When your code receives a `java.sql.Connection` from the pool, it tries to execute some statement and it fails
with a generic `SQLException`. 
3. You can't do much with it so you invoke `close()` which _returns it back to the pool_. 
4. And the cycle repeats again. Even worse - only 1 connection of 50 could die so it's only 1/50 times when user gets 
an error. So it may be hard to notice and debug the problem.

To handle broken connections gracefully we need to configure the pool to test them.
 
- Experiment: what happens if the DB is forcibly restarted? Would the already established connections be valid after 
that?
- Think & research: what do network firewalls do with a stalled connection when there is no traffic for some time?
- Play with `testConnectionOnCheckout`, `testConnectionOnCheckin`, `idleConnectionTestPeriod`
- Think & research: which of the above would you choose and why? Does the choice depend on the type of app that you're
writing? Would the choice be the same for apps that require low latency? Would it be different for apps that have 
zero tolerance to errors?
- Think & research: how does idle connection testing work? Wouldn't a pool require separate thread to do some 
background work?
- Research: `Connection#isValid()` - will that work for all the JDBC Drivers? If not, what are other options?

*Tip:* some DB Pools actually recognize death specific SQLExceptions (after all you work with pool's `Statement` that 
can catch exceptions first) and can remove the connection even without testing. C3P0 is one these smart pools. Though
client code would receive exception anyway.

# Step 4 - Timeouts

- Think & research: what are `Deny` and `Reject` policies of the network firewalls? How do they differ and do clients 
  recognize the the connection cannot be established? _When_ do clients find that out in case of each of the policy? 
- Think & research: what's going to happen when the pool is exhausted but you asked for yet another Connection from it?
- Read about and configure `checkoutTimeout`. Can this value be large like 10min? Can it be small like 100ms?
- Think & research: if you borrow the connection from the pool and don't return it back, what does it mean?
- Read about and configure `unreturnedConnectionTimeout`
- Find an option in C3P0 to debug which code borrowed and didn't return the connection back?

*Tip*: issues related to firewalls `reject`ing packages are valid not only for databases but for any integration that
collaborates over network. Read about Connection Timeout and Socket Timeout to dive into the topic. 

# Step 5 - DB Pools in App Servers

App Servers come with their own DB Pool implementation. Tomcat is not an exception. But to configure it
we would need to use JNDI. 

JNDI (java naming & directory interface) is a way to place objects and data into directory-like manner and retrieve 
them. E.g. a dataSource object could be configured and created by AppServer itself and then placed under 
`db/dataSource` name. Our code can then get this through JNDI.

Why don't you simply configure DataSource inside the app like we did it before? In some organizations separate
people (Operations a.k.a. Ops) prefer to manage things like DB Connections. They can change the settings in runtime 
and publish the changes to our apps. This is not too common, but it's not too rare either.

- Configure a Tomcat's DataSource via JNDI
- Write a code that gets the DataSource via JNDI. For this you will need to use Spring's FactoryBean approach. Read 
about it if you don't know it.
- Replace C3P0 DataSource with the Tomcat's one. Deploy the app to Tomcat and make sure everything works.
- Research Tomcat Pool options - could you find all the options that you configured in C3P0? The names are different, 
but it's very similar.
- Replace your FactoryBean with Spring's `<jee:jndi-lookup>` utility

Try running tests. Now they fail because in tests nothing starts up a JNDI Context. So there is no DataSource 
configured that can be looked up. So what can we do? There are 2 choices: either hook up some simple JNDI implementation
as a lib for testing and create it there or.. get rid of DataSource JNDI :) 

The fact that you don't use JNDI doesn't mean you can't use Tomcat's DB Pool - it's in the classpath of Tomcat so 
your app has access to those classes. But only if it's deployed and not if you're running tests. To fix this you can
add a to `org.apache.tomcat:tomcat-jdbc`. This way it's going to be accessible both to tests and deployed app. But
we don't want to duplicate the same classes and jar's - it's present in Tomcat itself and it's present in war file.
Find a way to add so that the code compiles, tests pass but extra lib is not included into war file.

# Literature

Yet another best-of-breed book that covers architectural questions is 
[Release it!](https://www.amazon.com/Release-Production-Ready-Software-Pragmatic-Programmers/dp/0978739213). It
explains many approaches and hazzards related to building distributed systems. 
