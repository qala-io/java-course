Basic transactions
------------------

# Locks 

2 transactions will lock each other when they change the same record (UPDATE or DELETE). 2nd transaction will be
unlocked upon commit or rollback of 1st transaction. You can set up timeouts
for statements or transactions which would limit how much time a request can take (which automatically puts a 
limit on how long a locking can take).

It is possible to lock SELECT as well - you need to use SELECT FOR UPDATE statement. This is called Pessimistic Locking.
You lock the access to a record because you don't allow multiple threads/users to work with it simultaneously.

# Deadlocks

It's very easy to create a deadlock - you just modify records in opposite directions. So TX1 updates R1, R2 while
TX2 updates R2, R1. Each transaction locks its first row (TX1 locks R1, TX2 locks R2) and then they wait for
each other. DBs detect deadlocks right away and abort one of the transactions.

To mitigate the risks of deadlocks (not only in databases, but in any concurrent code with mutex and multiple locks)
you must take locks in the same order. Example:

- FunctionalityA requests records and modifies them with UPDATE
- FunctionalityB requests **sorted** records and modifies them with UPDATE

This can easily lead to deadlocks. In this case if you sort records in both cases you eliminate the possibility 
of deadlocks.