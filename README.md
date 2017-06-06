Java Professionals Course
-----------------

This is a puzzle that should be easy to crack after 
[Transactions & AOP](https://github.com/qala-io/java-course/blob/master/docs/programme/transactions-n-aop.md) part of 
the course. There is a failing test, please fix it and find out the root cause of the problem.

The test uses MockMvc to simulate HTTP requests to the app, but for some reason NPE happens. The exception happens in
the place where we wouldn't expect it to be - at the first glance it would appear that Spring Bean didn't get its
dependency injected. Find out if this is true.