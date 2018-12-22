DNS
----

Before sending the actual HTTP request, browser identifies which IP address to send data to (IP protocol 
doesn't know what https://google.com is, it can only work with IP addresses). There are DNS servers that store
the mapping NiceLookingName->IP address. Browser first issues a request to identify the IP address of the target 
machine and only then it sends an HTTP request _to that IP address_.

But when we discussed Web Servers like nginx we touched the topic of having multiple apps on the same
machine and serving different domain names. So how does nginx determine which app to give control to: 
mysite.com or google.com? IP protocol doesn't have a notion of a domain name. The answer is - after determining
IP address of the machine we don't throw away the domain name - we also duplicate it in HTTP protocol as 
a `Host` header.

A similar problem exists when using HTTPS. For each site hosted on 1 machine we want to have a different SSL
certificate. But during SSL handshake there is no information about domain name. Well, there haven't been. 
Today this problem is eliminated with [SNI](https://en.wikipedia.org/wiki/Server_Name_Indication) extension,
but you still may run into code that uses older HTTP libs (even in Java) that don't have SNI as part 
of communication - this would make server return wrong SSL certificate.

And to finish, let's get the terminology straight:
* NiceLookingName of the site like google.com is called **FQDN** (fully qualified domain name)
* If you use a Linux utility `dig google.com` to identify the IP addresses, it has multiple records - e.g. 
there is a record for mail server for that host. But the one that we're interested at the moment 
(it defines FQDN->IP mapping) is called **A record**.