# WhiteBoard-Implementation-Examples
Five examples of how to implement a WhiteBoard PRC RMI P2P Cloud and Web

## 1. EchoExample


## 2. RPC
* **RPC Simple Server**: Handling only one client at a time. 
* **RPC Server**: Handling multiple clients in Threads and executing changes on the Whiteboard.
* **Client**: Connects to the server, sending commands the Whiteboard service. For each message to the server, the client awaits a response.
Command **stop**: The client shuts down and the connection closes. 
The principal commands are: *create*, *put*, *get*, and *delete*. 
Other commands are interactively described by the Whiteboard service.

* **Starting Client and Server**: 
First run the RPC Server main and then Client's main function. The Server can handle up to 50 Clients parallel. 


## 3. RMI 
* **RMI Server**: Provides the WhiteBoard class as an Interface.
Start the registry first in the `bin` directory (or the same as you start the server)
* **RMI Client**: Handles commands and executes them via the Whiteboard remote Interface. The interaction is the same as for the RPC example with the principal commands: *create*, *put*, *get*, and *delete*. 
* *consists of 3 packages*: 
  + rmiserver: 
  + rmiinterface: 
  + rmiclient:
* *compile*: `javac -d bin -cp src src/*/*.java` or with you IDE relying on the *.classpath* file
* *run*: 
  + `cd /bin`
  + `rmiregistry`
  + `java rmiserver.WhiteBoardService`
  + `java rmicient.Client`
* *alternative run*: 
  + `bash startServer.sh` complies all classes, starts the *rmiregistry* and the WhiteBoardService, kills the *rmiregistry* on server termination. 
  + `bash startCient.sh` starts the client. 

## 4. P2P
* **Peers**: 
+ [whiteborad solution](https://medium.com/bpxl-craft/building-a-peer-to-peer-whiteboarding-app-for-ipad-2a4c7728863e)  
the action of a peer is broadcasted to all others, each action has a logical time-stamp, and ever peer records a sorted array of all actions to generate a consistent state of the whiteboard. 
+ which peer holds what data? 
+ who connects the peers to each other
