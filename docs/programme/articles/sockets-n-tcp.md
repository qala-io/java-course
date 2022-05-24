# Network communication over sockets

In order to receive or send data over the network in Java we work through so called Sockets (like an electrical socket in your wall). Since these classes work at TCP level, we need to:

1. Start accepting TCP connections at some TCP port (just an integer number), let's say `8080`.
2. If a request-for-connection came from some client, we may decide to establish this connection.
3. When establishing the connection, our server and the client agree on some TCP ports (called ephemeral ports), let's say client's port is `84123` and the server port is `612391`

Note, that our `8080` port was _only_ for establishing new connections and to agree on the ephemeral ports. When the connection is established - the data is sent back and forth using the ephemeral ports.

Moreover, the actual connection is identified by 4 pieces of information (they are sent in each TCP packet):

1. Source IP and (ephemeral) Port
2. Destination IP and (ephemeral) Port

So when the next TCP packet arrives at our end, OS identifies Source IP, Source Port, Destination IP, Destination Port - and this tells it which TCP connection this relates to.

## Java implementation

Here's how typical work with sockets looks:

```
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket s = new ServerSocket(8080);
        Socket connection = s.accept(); 
        
        InputStream input = connection.getInputStream();
        ...
        OutputStream output = connection.getOutputStream();
        ...
    }
}
```

1. `new ServerSocket(8080)` - this is just a preparation. At this stage Java asks OS to create an OS-level socket. Which basically just an integer identifier with some meta information.
2. `s.accept()` - this now starts listening (accepting new connections). This is a blocking operation - we're stuck here until someone decides to connect to us.
3. Once a client connected to us, we can continue and start working with Input and Output streams and read/write data over the network.

## Accepting multiple connections

Note that until we returned back to `s.accept()` we're not accepting new connections. What happens if some other client decides to establish a connection to our `8080`? OS will put it into a queue and will give it to our program when we get back to `s.accept()`.

What happens if new requests-for-connection are coming and coming, and are not getting `accept`ed? The queue will grow, but there's a limit to how much it can grow. If the limit is reached, OS will reply with an error and the client won't be able to connect. This limit can be configured when we create the socket and it's called (at least in Java) a `backlog`:

```
new ServerSocket(8080, 10)
```

So if we `accept`ed 1 connection and we work with it - the other 10 will be queued up. If 11th comes, OS will reply with an error. Well, it's a little more complicated since when we pass 10 - [it may turn into another number by OS](https://notes.shichao.io/unp/ch4/#listen-function).