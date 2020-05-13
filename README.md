# WhiteBoard-Implementation-Examples
Five examples of how to implement a WhiteBoard PRC RMI P2P Cloud and Web

## 1. RPC
* **RPC Server**: handling only one client at a time. 
* **RPC Server**: handling multiple clients in Threads and executing changes on the Whiteboard.
* **Client**: connects to the server and sends commands to manipulate the Whiteboard. For each message to the server, the client waits for a response.
Command **stop**: the client shuts down and the connection closes. 
The principal commands are: *create, put, get,* and *delete*. 
Other commands are interactive described by the service.

## 2. RMI 
* **RMI Server**:
* **RMI Client**: 