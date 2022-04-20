How does Debug work?
----

Debugging is a client-server interaction where server (JVM that's debugged) communicates with the client (IDE) sending the information like current line of code executed in every thread, variables, etc. Client (IDE) can ask the server to stop the execution of the program and receive some alternatives commands. 

Since it's a communication between 2 processes, they need to find a way to talk to each other. It's possible to use shared linux socket (a common file through which they communicate) if both JVM and IDE run on the same machine; or they simply can talk over TCP. 

The latter option is universal and can be used both on a single machine or we could debug a remote JVM. For this we need to tell JVM to listen for commands on some TPC port. Here's a typical JVM debug option:

```
-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000
```

We tell JVM:
1. To include an debugging agent when running (`-agentlib:jdwp`). Basically it's an additional code that will be executed by JVM.
2. To communicate via TCP socket: `transport=dt_socket` (I think `dt` stands for "debug transport")
3. To be a debugging server (`server=y`). Our IDE will be the client.
4. Start the program right away (`suspend=n`) and then allow IDE to connect somewhere in the middle of work. Or we could wait for IDE to connect, and only then start the program (`suspend=y`)
5. And listen on some network interface (`*`) and port (`8000`)

Note, that if the binary that you debug is built not from the code that you look at - the lines of code won't match and the IDE won't stop at the places where you would expect but will stop at lines with JavaDocs for instance. So make sure your binary was built from the sources at hand.