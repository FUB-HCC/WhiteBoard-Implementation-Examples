# WhiteBoard-Implementation-Examples
Five examples of how to implement a WhiteBoard PRC RMI P2P Cloud and Web

## 1. EchoExample
* **TCP**: 
    + **Client**: Connects to the Server, sends the input from *stdin* and prints the echo responds.
    + **EchoServer**: Handling one client at a time on port `12345` and sends back the echo changing "i" to "o".
    + **EchoServerExtended**: Is of the same functionality as *EchoServer*, but handles multiple clients concurrently. 
    + *compile*: `javac -d bin -cp src src/tcp/*.java`
    + *run*:        
        `java tcp.EchoServer` or `java tcp.EchoServerExtended`      
        `java tcp.Client`   
    
* **UDP**: 
  + **UDPClient**: Is sending *one* input from *stdin* as a datagram packet to the *UDPServer* and prints its responds.
  + **UDPServer**: Receives datagram packets on port `9876` und echoes them back to the client. 
  + *compile*: `javac -d bin -cp src src/udp/*.java`
  + *run*:  
    `java udp.UDPServer`      
    `java udp.UDPClient`    

## 2.1 RPC Example Simple
* **RPC Simple Server**: Handling only one client at a time and has the same service as the *RPC Server*.
* **Client**: Same as *2.1 RPC Client*
* *compile*: `javac -d bin -cp src src/rpc/*.java`
* *run*:     
    `java rpc.SimpleServer`     
    `java rpc.Client` 

## 2.2 RPC Example
* **Server**: Handling multiple clients in Threads and executing changes on the Whiteboard. 
* **Client**: Connects to the server, sending commands the Whiteboard service. For each message to the server, the client awaits a response.
Command **stop**: The client shuts down and the connection closes. 
The principal commands are: *create*, *put*, *get*, and *delete*. 
Other commands are interactively described by the Whiteboard service.
* *compile*: `javac -d bin -cp src src/rpc/*.java`
* *run*:        
    `java rpc.Server`       
    `java rpc.Client`        



## 3. RMI Example 
* **RMI Server**: Provides the WhiteBoard class as an Interface.
Start the registry first in the `bin` directory (or the same as you start the server)
* **RMI Client**: Handles commands and executes them via the Whiteboard remote Interface. The interaction is the same as for the RPC example with the principal commands: *create*, *put*, *get*, and *delete*. 
* *consists of 3 packages*: 
  + rmiserver: 
  + rmiinterface: 
  + rmiclient:
* *compile*: `javac -d bin -cp src src/*/*.java` or with you IDE relying on the *.classpath* file
* *run*:        
    `java rmiserver.WhiteBoardService`    
    `java rmicient.Client`      
  
## 4. P2P Example
* **Peers**: Start first one Peer with its port and all others with port and the host and port of one known Peer in the network. 
    * *compile*: `javac -d bin -cp src src/*.java` or with you IDE relying on the *.classpath* file
    * *run*:        
    `java p2p.Peer 12345`    
    `java p2p.Peer 12344 localhost 12345` 

+ [whiteborad solution](https://medium.com/bpxl-craft/building-a-peer-to-peer-whiteboarding-app-for-ipad-2a4c7728863e)  
the action of a peer is broadcasted to all others, each action has a logical time-stamp, and ever peer records a sorted array of all actions to generate a consistent state of the whiteboard. 
+ which peer holds what data? 
+ who connects the peers to each other
