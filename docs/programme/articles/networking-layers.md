How do network protocols are built on top of other protocols?
----

Each networking protocol has its headers and a payload. Headers are needed for the correct routing while 
payload is the actual data. So the payloads can have anything from that protocol perspective. And this is 
how higher-layer protocols are built - their own header and body are stored in the payload of the lower-layer
protocols. It's like russian dolls:

```
 ----------------IP Layer------------------------------------------------------
|IP Headers: source IP, destination IP, etc.                                   |
|                                                                              |
|IP Payload:   --------------TCP Layer-----------------------------------------|
|             |TCP Headers: source port, destination port                      |
|             |                                                                |
|             |TCP Payload:  ---------HTTP Layer-------------------------------|
|             |             |HTTP Headers: request line (Method, URI, Version),| 
|             |             |              headers (Host, Content-Type, etc)   |
|             |             |                                                  |
|             |             |HTTP Payload: HTML, JSON, CSS, etc.               |
|             |             |                                                  |
 ------------------------------------------------------------------------------
```
Note, though that HTTP request usually doesn't fit 1 TCP packet and therefore is split in many.

The whole routing looks like this:

* IP protocol identifies the physical machine where our software runs
* TCP determines which port to use on that machine. There might be multiple software packages listening 
on different ports.
* HTTP determines which endpoint to invoke depending on its URI