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
This example implements the Whiteboard application in a pure Peer2Peer architactural style that relies on a complete network of *TCP* connections. 

* **Peer**: First, start one Peer with its port (1 var). Then, start all other peers with their port, their hostname and the port of a known peer in the network (3 vars). 
    * *compile*: `javac -d bin -cp src src/*.java` or with you IDE relying on the *.classpath* file (or in Eclipse "run as configuration")
    * *run*:        
    `java p2p.Peer 12345`    
    `java p2p.Peer 12344 localhost 12345`   
    `java p2p.Peer 12343 localhost 12344` ...

An **Edit** is defined as a: `enum Edit { add, remove }`. Each edit on the Whiteboard is "broadcasted" to all others.
An **EditRecord** contains the Edit, Shape, Peer ID, and has a logical time-stamp. Every Peer records a sorted array of all EditRecords to generate a consistent state of the Whiteboard across Peers. 
The **Whiteboard** class is the shared instance between the different Threads, where the data about the Peer2Peer network and the Whiteboard itself is stored. 
The **PeerConnection** class takes care of all communication on one socket connected to one Peer and is running a thread to receive broadcasted EditRecords. 
The **ListingThread** waits for new Peers to join the network and establishes PeerConnections, which then send the needed information, namely the Addresses of all Peer in the Network and the EditRecords. 

If one Peer disconnects, all others update their List of PeerConnections. The input interaction is the same as in the previous examples. With the *stop* keyword the Peer leaves the network. 

* Protocol choices *TCP* vs. *UDP*: This implementation relies on stable TCP connnections. For increasing size of the network it might result in larger bandwith usage and overhead. In this case *UDP* could be a better choice, though possible packege los needs to be handled, to assure a consistent Whiteborad.

![uml class diagram](out/P2PExample/ulm/Peer.png)

## 5. Cloud AWS Example
The Amazon Elastic Compute Cloud (EC2) offers the following advantages:
+ High availability for different geo-locations (availability zones)
+ Autoscalling to maintain the application availability by adding or removing EC2 instances, depending on demand, e.g. CPU-usage.
+ Elastic Load Balancer (ELB) to distribute the incoming traffic equally among EC2 instances. 
+ Performs health checks to terminate and restart erroneous instances. 

### Setup of our RPC-Server on the AWS-Cloud
1. Launch EC2 Instance (Amazon Linux 2 AMI) Instance type: *t2.micro*
2. Connect to the instance with ssh and do the desired setup, so we install JAVA and pull from this git Repository.
3. Create an Amazon Machine Images (AMI) from the Instance
4. Create and Launch an Autoscaling Configuration Group with our AMI. Then we choose a desired capacity one 1 Instance, minimum 1 and maximum 4. When an instance terminates, a new one will be started to reach the desired Capacity. We configured scaling policy to increase and decrease the number of instances depending on the average CPU-Utilization. Decrease if it is below 20%, and increase by one if it is above 30% for demonstration purpose. 
5. Add a Load Balancer to the Auto-Scaling Group, that performs health checks on the instances and distributes the traffic.

6. Now we have a scalable Whiteboard-Service, but each Server-Instance has its Whiteboard, as they are not synchronized. A database would provide a solution to guarantee a consistent state, e.g. accessing an Amazon Elastic Block Store (EBS) from all instances.

For interaction and testing of the cloud use:
+ RPCExample:   
    `java rpc.Client RPC-load-balancer-205ffa34ea80b9cb.elb.us-east-2.amazonaws.com`   
+ AWSCloudExample:      
    `java aws.Client RPC-load-balancer-205ffa34ea80b9cb.elb.us-east-2.amazonaws.com <number of threads>` 

### aws.Client:
Creates multiple Client-Threads to connect to the server and create Shapes on the Whiteboard in a while-loop, to test the server, whit 40 Threads the CPU is quickly above 30%. 
