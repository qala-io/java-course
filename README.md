Java Professionals Course
-----------------

This is a puzzle! Find the failing test and fix it.

The test checks whether `@Tansactional` works. If exception happens in the service Spring Tx has to rollback the 
transaction along with all the SQL statements that happened in it. But looks like something's wrong - one of the objects 
gets actually `INSERT`ed and.. committed? So what's up with our transactions, is there is a bug somewhere?