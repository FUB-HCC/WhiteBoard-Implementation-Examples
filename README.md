# WhiteBoard-Implementation-Examples
Five examples of how to implement a WhiteBoard PRC RMI P2P Cloud and Web

## 1. RPC
* **RPC Server**: handling multiple clients in Threads and executing changes on the Whiteboard
* **Client**: connects to the server and sends commands to manipulate the Whiteborad. For each message to the server the is a response.
the client shuts down and the conncetion stops whit the command **stop**. 
The pricipal commands are: *create, put, get,* and *delete*. 
Other command are interactive described by the server.  
