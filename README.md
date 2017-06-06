Java Professionals Course
-----------------

This is a puzzle that should be easy to crack after 
[Transactions & AOP](https://github.com/qala-io/java-course/blob/master/docs/programme/transactions-n-aop.md) part of 
the course. There is a failing test, please fix it and find out the root cause of the problem.

The test checks whether `@Tansactional` works. If exception happens in the service Spring Tx has to rollback the 
transaction along with all the SQL statements that happened within its boundaries. But looks like something's wrong - 
one of the objects gets actually `INSERT`ed and.. committed? So what's up with our transactions, is there is a bug somewhere?