# WhiteBoard-Implementation-Examples
Five examples of how to implement a WhiteBoard PRC RMI P2P Cloud and Web

## 1. RPC
* **RPC Simple Server**: Handling only one client at a time. 
* **RPC Server**: Handling multiple clients in Threads and executing changes on the Whiteboard.
* **Client**: Connects to the server, sending commands the Whiteboard service. For each message to the server, the client awaits a response.
Command **stop**: The client shuts down and the connection closes. 
The principal commands are: *create*, *put*, *get*, and *delete*. 
Other commands are interactive described by the Whiteborad service.

* **Starting Client and Server**: 
First run the RPC Server main and then Clients main function. The Server can handle up to 50 Clients parallel. 

## 2. RMI 
* **RMI Server**: Provides the WhiteBoard class a Interface, first start the registry in the right directory (the same as you start the server)
* **RMI Client**: handles commands and executes them via the Whiteboard remote Interface 
* **start**: 
  + **cd** /bin
  + rmiregistry
  + java rmiserver.WhiteBoardService
  + java rmicient.ClientOperation
