Hibernate
---------

Working with SQL statements and ResultSets can be cumbersome:

- There could be multiple places in the app where you transform between SQL columns and classes. If a column changes
you'd need to update all the places.
- SQL statements themselves could be pretty large and complicated especially when you need to fetch many related
entities

ORM (Object-Relational Mapping) tools allow to map the fields of the classes to the DB columns and then will generate
SQL statements for you. Which is awesome since now you can concentrate on the business logic and OOP instead of DB.  

This also gives you a possibility to switch between DBMS easily since you don't write native SQLs anymore which could
differ from DBMS to DBMS. Such feature is very convenient because it's possible to write DAO tests that could be run
on in-memory DB instead of the real one. Of course in our case H2 is already an in-memory DB, but this is a very rare
case in real life.

Additionally ORMs try to optimize the work with DB in different ways:

- They delay the actual execution of SQL as much as possible. This decreases the amount of time the rows and tables are
locked.
- They can (if configured) group INSERT and UPDATE statements together and issue bulk statements instead of executing
them one by one.
- They can transparently use process-level cache that will prevent some of the SELECT statements from happening.
 
# Dark Side of ORM

But ORMs have issues as well:

- They are _very_ complicated. While working with DB itself is not trivial, ORMs add yet another layer of complexity 
which makes it challenging for many people. So while they may simplify the app, to use them effectively you should know
a lot.
- Not optimal SQL. Since you create one mapping and then re-use it in many places the mapping can be optimal not for
every place. Sometimes you just need to select one column, other times you need to update only one column. ORMs don't
allow this - they work with objects and their hierarchy.

So instead of using ORMs as a universal solution you should understand where it's more applicable. You can start at the
beginning of the project with ORM since a lot of changes and refactorings are expected at that stage. And then if the
app is being developed long enough you'll probably start moving towards native SQL.

# JPA, Hibernate

Amongst different ORMs historically Hibernate won the battle and became the most used one 

# Step 1 - How it works

* Read about Hibernate Proxies
* Read about Entity Lifecycle in Hibernate (PERSISTED, DETACHED, etc)
* Read about Identity Generators (ways to generate IDs)
* Add Hibernate dependencies and look into classes `Session` and `StatefulPersistenceContext`

# Step 2 - Re-write DAO

* Implement an alternative implementation of DAO. Instead of re-writing the previous one, create interfaces that are 
implemented both by JDBC DAO and by Hibernate DAO. It should be possible to switch between DAO implementations 
between startups via Spring means (there are multiple ways of doing this - which one do you choose?).
* Implement Hibernate Dao Tests - they need to import spring context and start the embedded H2 DB
* Play with `get()` vs `load()` methods and explore how Hibernate Proxy looks

# Step - Alternatives

MyBatis, Spring JDBC, JOOG