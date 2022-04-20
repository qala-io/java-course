Maven Surefire & Forks
----

When running tests via Maven, we wouldn't want those to be run in the same JVM as Maven itself. Because then they'd share a lot of resources: we'd see system and environment variables available to Maven, we'd use the same memory and same Garbage Collector, etc. Instead we'd like our code to be isolated from Maven as much as possible.

While we could achieve some level of isolation in the same JVM (using a different ClassLoader from the one Maven uses for its own classes), the most robust approach is to start a _separate process_ just for our tests. And this is what Surefire does by default. The process that's started by another process is called a _fork_*. 

This makes it harder to debug our tests because we want to debug the fork, not Maven itself. So if we were to pass debugging options to Maven, they wouldn't be applied to the fork and we wouldn't be able to debug our tests. To overcome this Surefire allows specifying JVM options in its configuration (e.g. through `<argLine>`) with which it'll start the forked JVM. Here's an example from [Surefire docs](https://maven.apache.org/surefire/maven-surefire-plugin/examples/fork-options-and-parallel-execution.html):

```
<argLine>-Xmx1024m -XX:MaxPermSize=256m</argLine>
<systemPropertyVariables>
    <databaseSchema>MY_TEST_SCHEMA_${surefire.forkNumber}</databaseSchema>
</systemPropertyVariables>
<workingDirectory>FORK_DIRECTORY_${surefire.forkNumber}</workingDirectory>
```

This is where you'd configure debugging options. It's also possible to configure Surefire not to use forks (`<forkCount>0</forkCount>` option) - then the tests will be run in Maven's JVM.

*Generally speaking a "fork" is a child process created by our parent process. In low-level programming when we create a fork it also has the same files open and it sees the variables that were created by the parent process. That child process is basically a copy of the parent process. But how this happens at lower levels isn't important for us here. Just treat a fork as a completely new JVM that doesn't see anything from its parent JVM.