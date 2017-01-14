Building Web Apps in Java
-------------------------

Almost any web application would have at least these components:

```
 ______       _____________        ____________________       ____
|      |     |             |      |                    |     |    |
| User | --> | HTTP Server | -->  | Application Server | --> | DB |
|______|     |_____________|      |____________________|     |____|
```

* HTTP Server a.k.a. httpd a.k.a. Web Server (Nginx, Apache HTTPD) is the 1st software that would receive requests 
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
* Application Server is a Java software that initializes and runs your web app. Any Java program has to have `main()` 
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

* Learn what a Servlet is and write a Hello World example
* Learn what `web.xml` is and declare your servlet there
* Read about `Filter` and create 2 (not one!) simple filters yourself. They should write something to console before 
the request is coming and after Servlet responds.
* Read about `Listener` and create one yourself

# Step 2 - Thread Pools

* Read what BlockingQueue is, try out some examples
* Read about [States of Threads](https://docs.oracle.com/javase/7/docs/api/java/lang/Thread.State.html)
* Read about ThreadPools, Executors. Try to write some sample tasks and submit them to the pool for execution.
* Dig into Thread Pools source code and find out how they work internally.

# Step 3 - Application Servers

* Find out how to configure a Thread Pool for Tomcat
* Read about how this pool is used by App Servers
* Read about `acceptCount` configuration in Tomcat's [Http Connectors](https://tomcat.apache.org/tomcat-8.0-doc/config/http.html)
* Think about what value we should set for thread pool's options: max size and accept count?

# Step 4 - App Server Memory Leaks

* Read about the difference between daemon and non-daemon threads
* Read about how threads spawn by your code can lead to memory leads in App Servers
* Read about how ClassLoaders work and their hierarchy in Java apps
* Read about ClassLoader Memory Leaks in App Servers
