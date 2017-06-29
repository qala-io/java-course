When you start a thread your method `run()` is invoked. Until `run()` is in progress the thread is working. When
`run()` is finished the thread stops and enters TERMINATED state. It's impossible to start the thread again after that.

Sometimes heavy work is done in threads that can take a lot of time. So what if application wants to stop those threads?
Actually there is no way you can stop threads. You can only ask threads to stop and it's up to them to decide. Usually
it happens like this:

```java
public void run() {
    while(true) {
        if(Thread.currentThread().isInterrupted())
            break;
        //do work
    }
}
```

Note, that we check the flag of the current thread - did anyone ask us to stop (invoked `thread.interrupt()`)? If so,
we'll break the cycle. If no, we keep working. If we don't check the flag there is no way the thread can be stopped
unless JVM is stopped.