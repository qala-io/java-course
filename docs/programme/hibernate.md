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

# Hibernate vs JPA

Among different ORMs historically Hibernate won the battle and became the most used one. Its success impacted the way
further ORMs were implemented in Java community. Hibernate developers participated in the development of JPA (Java 
Persistence API) standard and effectively made Hibernate its reference implementation. So now we have different ways of
working with Hibernate:

* Hibernate XML
* Hibernate Annotations - most of these were deprecated in favour of JPA Annotations but some are still in 
use when a more fine-grained control is required.
* JPA Annotations
* JPA XML (haven't seen these in practice at all)

All these approaches do the same and they operate with the same basic principles so if you know one it's not hard to
switch to another. The terminology though could be different. E.g. you can work with things like Session, Components 
when you work with Hibernate, but they are going to be called EntityManager and Embedded Object in JPA.

JPA Annotations currently is the most widespread way of working with Hibernate. My personal preference though is
Hibernate XML:

* This is the most functionally-rich way of mapping
* It doesn't bloat Entity classes with lots of persistence annotations

# Step 1 - How it works

* Read about Hibernate Proxies
* Read about Entity Lifecycle in Hibernate (PERSISTED, DETACHED, etc)
* Read about Identity Generators (ways to generate IDs). Which one would you prefer and why? Which of them require 
communication with DB and which don't?
* Add Hibernate dependencies and look into classes `SessionImpl` and `StatefulPersistenceContext`

# Step 2 - Re-write DAO

* Implement an alternative implementation of DAO. Instead of re-writing the previous one, create interfaces that are 
implemented both by JDBC DAO and by Hibernate DAO. It should be possible to switch between DAO implementations 
between startups via Spring means (there are multiple ways of doing this - which one do you choose?).
* Implement Hibernate Dao Tests - they need to import spring context and start the embedded H2 DB
* Play with `get()` vs `load()` methods and explore in debug how Hibernate Proxy looks

# Step 3 - Persisting Entities

* Read about Flush Mode. When in the app do you think the SQL statements are flushed? Configure SQL logging and check 
your guess.
* After the entity is persisted it must have an ID. Return to the ID Generation Strategies and find those that require
`INSERT` statements for the ID to be generated. Will Flush Mode impact these `INSERT` statements in any way? Would that
change if you choose a different strategy?


# Alternatives

While Hibernate is a full blown ORM that both generates SQL statements and keeps track of Persisted entities, there are
some lightweight alternatives. They implement ORM only partially and therefore move the balance between low-level and
high-level control over the DB-related code. Take a look at these: Spring JDBC (we've touched it already), JOOQ, MyBatis.

Another framework to mention is *Spring Data*. It's an even higher level of abstraction than JPA - you use the very
same mapping, but instead of manually creating EntityManager/Session, creating Query object and casting the results you
simply create interfaces and mark them with the appropriate annotations. And the rest is done for you. But before 
rushing into using it remember - the higher the level of abstraction the more you have to know and the slower it works.

# Puzzles

- There is a bug in Hibernate mapping in branch `puzzle-wG3JaxG4`. But the test actually passes. Find the reason and fix
the test.

# Literature

* [POJOs in Action](https://www.manning.com/books/pojos-in-action) - one of the fundamental books on ORMs. Explains
the principles instead of just going through the feature list of ORMs.
* [Java Persistence with Hibernate](https://www.manning.com/books/java-persistence-with-hibernate) - a deep book that
goes over the features of Hibernate and the reasons it works that way. It's better to be read after POJOs in Action as
it's pretty complicated.