# WhiteBoard-Implementation-Examples
Examples of WhiteBoard Implementations using different architectural styles: PRC RMI P2P Cloud and Web

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
* **Peer**: First, start one Peer with its port and all others with its port and the host and port of one known Peer in the network. 
    * *compile*: `javac -d bin -cp src src/*.java` or with you IDE relying on the *.classpath* file
    * *run*:        
    `java p2p.Peer 12345`    
    `java p2p.Peer 12344 localhost 12345`   
    `java p2p.Peer 12343 localhost 12344` ...


Each edit on the Whiteboard is broadcasted to all others.
An **Edit** defined as a: `enum Edit { add, remove }`.

An **EditRecord** contains the Edit, Shape, Peer ID, and has a logical time-stamp. Every Peer records a sorted array of all EditRecords to generate a consistent state of the Whiteboard across Peers. 
The **Whiteboard** class is the shared instance between the different Threads, where the data about the Peer2Peer network and the Whiteboard itself is stored. 
The **PeerConnection** class takes care of all communication on one socket connected to one Peer and is running a thread to receive broadcasted EditRecords. 
The **ListingThread** waits for new Peers to join the network and establishes PeerConnections, which then send the needed information, namely the Addresses of all Peer in the Network and the EditRecords. 

If one Peer disconnects, all others update their List of PeerConnections. The input interaction is the same as in the previous examples. With the *stop* keyword the Peer exits. 
