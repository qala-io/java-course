JDBC
----

JDBC is a Java API to work with databases. As any other Java specification, JDBC provides a number of interfaces and
rules that every implementation must follow. Working with JDBC looks like this:

- You work with JDBC interfaces and classes that ship with any Java installation. Examples: Connection, Statement, 
ResultSet.
- You add an implementation lib which is called a JDBC Driver (could be mysql, oracle, postgre or any other driver) and
you tell to JDBC that you want to use that implementation. 
- JDBC loads the classes from that driver and while you continue working with Connection, Statement, ResultSet 
interfaces you actually work with the implementations from the driver you chose.

JDBC and databases is one of the most critical and complicated parts of the enterprise apps. There are technologies
built on top of JDBC (ORMs like Hibernate, simpler wrappers like SpringJDBC and MyBatis) but there is no way you can 
understand and use them effectively without understanding JDBC first.

# NoSQL Storage

JDBC is used to access RDBMS's (relational databases) that utilize tables, columns, rows. But there is another group 
of databases - NoSQL DBs. These include:
- Document-oriented DBs that can store unstructured hierarchical data 
- Graph-oriented DBs that can store relations between lots of interconnected objects. 
- ... 

For several years (somewhere around 2010-2015) there was a lot of hubbub around NoSQL storage but eventually it all 
calmed down. Today most companies utilize both traditional RDBMS and NoSQL. So JDBC continues to play a major role in 
most of the modern apps.

# Step 1