# Breaking connections

In the majority of cases we use standalone DBs like MySQL, Postgre, Oracle that are located on remote hosts. To
communicate with them a client-server interaction must happen. When such an interaction is initiated multiple things
happen:

- A TCP connection is established
- A Server Connection (a.k.a. Session) is created on DB Side (in this case it's the server side). Such
connections have settings, user information, they begin and end transactions, etc.
- A Client Connection (`java.sql.Connection`) object is created in your code.

So what would happen if any of these die (e.g. TCP connection is interrupted or DB crashed)?

1. Client side does _not_ get notified. And even if it would, how could it remove itself from the DB Pool? Thus DB Pool
can't know that the connection died and will return the broken `java.sql.Connection` to your code.
2. When your code receives a `java.sql.Connection` from the pool, it tries to execute some statement and it fails
with a generic `SQLException`.
3. You can't do much with it so you invoke `close()` which _returns it back to the pool_.
4. And the cycle repeats again. Even worse - only 1 connection of 50 could die so it's only 1/50 times when user gets
an error. So it may be hard to notice and debug the problem.

To handle broken connections gracefully we need to configure the pool to test them.

# Testing Connections

In order for the DB Pool to recognize that the connection is terminated and remove it from the pool multiple things can
be done:

1. DB Pool can check the connection (e.g. by sending some query or using a fast `isValid()` method if supported by the
driver) before the connection is returned to the client code.
2. DB Pool can check the connection when it gets returned to the pool
3. DB Pool can test the connections _periodically_ when it's in the pool and no one uses it
4. DB Pool can recognize SQL Exceptions thrown by the `java.sql.Connection` object and mark it as broken (remember that
pools return proxies - not the actual driver connection).

On the one hand 1st option is bullet-proof as it will never return a broken connection to the client, but on the other
hand until the check is done connection is not returned (and the client code is waiting). So 2nd option can be
considered to speed things up sacrificing reliability.

3d option is a good way to ensure the connection is not closed when it's idle. E.g. MySQL Server by default closes
connections if they are idle for 8 hours. Periodically checking the connections is kind of keep-alive request in this
case. Another source of such problems could be firewalls that may close network connections if they are stale for some
time.

4th option may not be implemented by all the DB Pools. Since SQLException a generic exception that can be thrown both
when SQL Query is invalid and when DB Connection is broken, DB Pools have to look inside of the message in the exception
to find out if the error states that the connection is broken. But this feature will not protect you from using a broken
connection - it will simply destroy that connection when it's returned to the pool so that it's not returned to your
code _again_. So periodical/on-checkout/on-checkin testing is still valuable.