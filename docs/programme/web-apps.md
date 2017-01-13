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