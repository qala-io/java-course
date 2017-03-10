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

# Step 2 - Basic Configuration

- Configure `maxPoolSize`, `minPoolSize`. What values would be most appropriate here? How can you correlate them with 
the thread pool configuration?
- What's going to happen when the pool is exhausted but you asked for yet another Connection from it?
- Configure `maxStatements` and `maxStatementsPerConnection`. Why do you need these? How do you decide which values
to put there? How do these correlate with the number of dynamically generated SQL statements and static SQL statements?
 
# Step 3 - Testing Connections

We've been working with embedded DB so far - we don't use network traffic and instead JDBC Driver uses Java classes of 
the DB Server directly. But in the majority of cases we use standalone DBs like MySQL, Postgre, Oracle. To communicate
with them we can use sockets (in case DB is installed on the same machine) but even more widespread is the case when 
DB is installed remotely and thus there's going to be some client-server interaction of TCP/IP. 

When such an interaction is initiated multiple things happen:

- A TCP connection is established
- A Connection (a.k.a. Session) object is created on DB Side (in this case it's the server side). Such connections
have settings, user information, they begin and end transactions, etc. 
- A Java object of type `java.sql.Connection` is created to represent that connection from Java side (client side in 
this case).

If any of these die (e.g. TCP connection is interrupted or DB Connection )
 
`java.sql.Connection` is just a representation of physical DB Connection. Each DB Server would keep track    

- Think & research: how does a Connection object represents a physical DB Connection? What happens if the DB is 
forcibly restarted? How would a Connection now that it's broken now?
- Think & research: what are Deny and Reject policies of the network firewalls? How do they differ and do clients 
recognize the the connection cannot be established? _When_ do clients find that out in case of each of the policy? 
- Think & research: what do network firewalls do with a stalled connection when there is no traffic for some time?
- 

*Tip*: most App Servers come with their own DB Pool implementation. Tomcat is not an exception. But to configure it
we would need to use JNDI


- LazyConnectionDataSourceProxy
