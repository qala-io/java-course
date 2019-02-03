Building Web Apps in Java
-------------------------

Almost any web application would have at least these components:

```
 ______       _____________        ____________________       ____
|      |     |             |      |                    |     |    |
| User | --> | HTTP Server | -->  | Application Server | --> | DB |
|______|     |_____________|      |____________________|     |____|
```

* **HTTP Server** a.k.a. httpd a.k.a. Web Server (Nginx, Apache HTTPD) is the 1st software that would receive requests 
from users. HTTP Server would look at the user request and can do things like:
   * Route the requests to the right app (multiple application can be hosted at the same machine - someone needs to 
   decide which request goes where)
   * Serve static resources. Depending on the situation these could be: pics, videos, fonts, JS, CSS. Instead of asking 
   a Java app to host these resources (which can be more resource consuming) you can put them on file system and tell 
   HTTP Server to serve them directly.
   * Cache responses. E.g. in case resource is served by your Java App you can configure HTTP Server to retrieve that 
   resource once. And then next time give the copy instead of hitting Java App again.
   * Configure SSL certificates to support HTTPS
   * Load balancing. If we run multiple instances of the same app on different machines (for horizontal scaling or 
   failover) we may use HTTP Server to choose to which instance this or that request should go. 
* **Application Server** is a Java software that initializes and runs your web app. Any Java program has to have `main()` 
method - in this case this method is located inside App Server. There are 3 kinds of them:
   * J2EE compatible (GlassFish, WildFly, WebLogic, WebSphere). They implement the full J2EE stack including 
   Servlets API, EJB, Distributed Transactions, JMS, etc.
   * Servlet Containers (Tomcat, Jetty, Undertow). These are lightweight App Servers that implement J2EE spec 
   partially - most importantly Servlets API.
   * Non-J2EE (Undertow, Play Framework) - these implement working with HTTP outside of Servlets API - they use 
   their own interfaces that your application would need to follow.

Application Servers support some of the features of the HTTP Servers like routing, SSL certificates, handling static 
resources but in practice we mostly rely on the HTTP Servers for that as it's their niche. They are more flexible and 
performant for this kind of job.

# Step 1 - Servlets API core

* Learn what a Servlet is. [Basics](https://www.javacodegeeks.com/2012/11/basics-about-servlets.html) 
  More basics [link](http://tutorials.jenkov.com/java-servlets/index.html)
* Write a Hello World example [link](https://medium.com/@backslash112/create-maven-project-with-servlet-in-intellij-idea-2018-be0d673bd9af)
* Learn what `web.xml` is and declare your servlet there [link](http://tutorials.jenkov.com/java-servlets/web-xml.html)
* Read about `Filter` and create 2 (not one!) simple filters yourself. They should write something to console before 
the request is coming and after Servlet responds.
* Read about `Listener` and create one yourself

# Step 2 - Thread Pools

* Read what BlockingQueue is, try out some examples
* Read about states of threads [link1](https://docs.oracle.com/javase/7/docs/api/java/lang/Thread.State.html),
[link2](./articles/threads.md)
* Read about ThreadPools, Executors. Try to write some sample tasks and submit them to the pool for execution. [link](https://www.baeldung.com/thread-pool-java-and-guava) 
* Dig into Thread Pools source code and find out how they work internally.

*Tip*: concurrency is a very complicated topic. Problem is - people often learn it as users not as engineers. To really 
grasp the topic you would need to dive much deeper - to the architecture of modern CPUs and Operating Systems. If you
dig into these topics you would find yourself truly understanding multi-threading instead of just learning its
aspects by heart and then forgetting and then re-learning them again. 

# Step 3 - Application Servers
:
* Everything about Apache Tomcat [link](https://www.mulesoft.com/tcat/understanding-apache-tomcat)
* Start - Stop Tomcat [link](https://www.oreilly.com/library/view/tomcat-the-definitive/9780596101060/ch01s02.html)
* Find out how to configure a Thread Pool for Tomcat
* Read about how this pool is used by App Servers
* Read about `acceptCount` configuration in Tomcat's [Http Connectors](https://tomcat.apache.org/tomcat-8.0-doc/config/http.html)
* Think & research: what types of the applications we write in regards to CPU and IO load.
* Think & research: what value we should set for thread pool's options: max size and accept count?
* Practice: Write a custom load test for your servlet and Tomcat. The test should submit multiple tasks to a Thread 
Pool. Each task hits your server multiple times in a separate thread. Find how your configuration changes in Tomcat
impact the way it responds to the load. Experiment with both - configuration of the test and Tomcat. 
* Load test Tomcat using JMeter [link](https://dzone.com/articles/how-to-load-test-tomcat-servers)
* Load testing using JMeter [link](https://dzone.com/articles/jmeter-performance-and-load-testing) [best_practices](https://jmeter.apache.org/usermanual/best-practices.html)

# Step 4 - Memory Leaks

* Read about the difference between daemon and non-daemon threads [link](https://beginnersbook.com/2015/01/daemon-thread-in-java-with-example/)
* Read about volatile keyword in Java [link](https://www.geeksforgeeks.org/volatile-keyword-in-java/)
* Read about how threads spawn by your code can lead to memory leaks in App Servers
* Read about how ClassLoaders work and their hierarchy in Java apps
* Read about ClassLoader Memory Leaks in App Servers

# Literature

[Head First Servlets & JSP](http://shop.oreilly.com/product/9780596516680.do) - is a pretty old book but it's still 
actual. That's the beauty of basic technologies and specifications - even if time passes they don't change a lot. Since
that book is written couple of major versions of Servlets API were released but they don't obsolete the information in 
the book. In the end a framework called Struts is going to be demoed - that one was outranked by Spring MVC which we're 
going to use down the road.

[Java Concurrency in Practice](http://amzn.to/1jyE5Kx) - one of the most fundamental books on concurrency in Java. 
One of the "must read" for Java developers.

# Links
* Play Framework [link](https://www.lightbend.com/blog/why-is-play-framework-so-fast)
* JMX
* TCP - HTTP [link](https://www.google.com/url?q=https://www.quora.com/What-is-the-difference-between-HTTP-protocol-and-TCP-protocol&sa=D&source=hangouts&ust=1548764098410000&usg=AFQjCNGaZ8yQMc5tdhpWdqgGkTkL48mdvg)
* 
  
  
[Computer Systems: A Programmer's Perspective](https://www.amazon.com/Computer-Systems-Programmers-Perspective-3rd/dp/013409266X) - 
a book about how things work on lower levels, including concurrency. No matter how high-level
the language that we use is (Java) - it still is based on low-level mechanisms. Thus to really know programming we
need to dive into these mechanisms.

[Java Concurrency in Practice](http://amzn.to/1jyE5Kx) - the most popular book on concurrency in Java. But after you 
read it many things will be forgotten unless you read about low-level concurrency and CPUs.