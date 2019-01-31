RPC vs REST
----

RPC (remote call procedure) is an app protocol that defines which methods (procedures) to call, which 
parameters can be accepted and what it returns. The most notorious example is SOAP.

When SOAP is used over HTTP it doesn't actually use much of HTTP. It uses only one methods - POST and several URIs,
but the biggest part of the protocol is described in HTTP Body. So SOAP is about creating a protocol inside of 
another protocol. It uses HTTP only as a tunnel.

REST on the other hand embraces HTTP. HTTP is an _application_ layer protocol - meaning our software can understand
it and change its behaviour depending on the headers. REST uses these capabilities instead of creating its own
protocol. It uses HTTP Method to define which operation to run (GET, POST, DELETE), HTTP Headers to add a flavour
to the request (e.g. Mime Types like XML, JSON or caching) and then it uses Request/Response Body to carry the 
information about the Resource it acts upon.

Over time REST won the battle with SOAP to rule the internet because of how simple and universal it is. But there
are times when we use RPC anyway, though not necessarily with SOAP - an interesting example of binary RPC 
protocol is [Protobuf](https://developers.google.com/protocol-buffers/). It uses binary format which should 
theoretically be faster than text formats (JSON) and it also is supported by many platforms. 