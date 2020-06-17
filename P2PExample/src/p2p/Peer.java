package p2p;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Peer {
    
    static String useHelpMessage = "For more information on how to use this service type: \"help\"";
    static String helpInfo = String.format(
            "There are four services: \"create\", \"put\", \"delete\" and \"get\", please select one. To quit the connection type: \"stop\". When a Shape is created, use \"put\" to place the Shape onto the WhiteBoard");
    static String createShapeInfo = String.format("Choose a Shape by selecting one of: %s, %s, %s.", ENUMShape.circle,
            ENUMShape.triangle, ENUMShape.rectangle);
    static String errorMessageShape = String
            .format("This type of Shape is not available, maybe you can change that? %s", createShapeInfo);
    static String errorMessagePut = String
            .format("There is no Shape to \"put\" onto the whiteboard, \"create\" one first...");
    static String deleteInfo = String.format("Select the Shape you want to delete by ID: ");
    static String successMessage = String.format("Successfully");
    static String failureMessage = String.format("Failure in");
    static String errorMessageDelete = String.format("Please type in a number for id");
    static String defaultMessage = String.format("Typo? or %s", useHelpMessage);
    static String welcomeMessage = String.format("Welcome to this Whiteboard service! %s", useHelpMessage);
    static String goodbyMessage = "goodby :)";

    private ListenThread listen;
    private WhiteBoard whiteBoard;
    private Shape currentShape;
    private BufferedReader in;
    private int port;

    /**
     * 
     * @param port
     * @param firstPeerHost
     * @param firstPeerPort
     * @throws IOException
     */
    public Peer(int port, String firstPeerHost, int firstPeerPort) throws IOException {
        this.port = port; 
        this.whiteBoard = new WhiteBoard(1);
        this.listen = new ListenThread(this.whiteBoard, new ServerSocket(port)); 
        this.currentShape = null;
        this.in = new BufferedReader(new InputStreamReader(System.in));
        connectToFirstPeer(firstPeerHost, firstPeerPort);
        // this.whiteBoard = new WhiteBoard(); // one shared Whiteboard between all
        // Threats created on this server
    }
    /**
     * 
     * @param port
     * @throws IOException
     */
    public Peer(int port) throws IOException { // first peer
        this.whiteBoard = new WhiteBoard(1); // first peerId is 1 
        this.listen = new ListenThread(this.whiteBoard, new ServerSocket(port)); 
        this.currentShape = null;
        this.in = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * only call in initialization to connect the existing network 
     * Connects to the know Peer, receives the addresses of all other Peers in the network and establishes all PeerConnections
     * @param firstPeerHost
     * @param firstPeerPort
     * @throws IOException
     */
    private void connectToFirstPeer(String firstPeerHost, int firstPeerPort)
            throws IOException {

        Socket socket = new Socket(firstPeerHost, firstPeerPort);
        PeerConnection firstPC = new PeerConnection(this.whiteBoard, socket, firstPeerHost, firstPeerPort);
        this.whiteBoard.addPeerConnection(firstPC);
        firstPC.sendPeerPort(this.port);
        int peerID = firstPC.receivePeerId(); 

        int maxPeerId = peerID; 
        String[] adressList = firstPC.getPeerAddressListAndEditRecord();
        firstPC.start(); // start the thread to receive new EditRecords on the Whiteboard 

        for(String adress : adressList ) {
            String host = adress.split(" ")[0];
            int port = Integer.parseInt( adress.split(" ")[1] );
            Socket newSocket = new Socket(host, port); 
            PeerConnection newPC = new PeerConnection(this.whiteBoard, newSocket, host, port);
            this.whiteBoard.addPeerConnection(newPC);
            newPC.sendPeerPort(this.port);
            peerID = newPC.receivePeerId();
            if (peerID > maxPeerId ) { maxPeerId = peerID; }
            newPC.sendSignalToPeer(0); // signals that Peer is already connected to the network
            newPC.start();
        }
        this.whiteBoard.setPeerId(maxPeerId+1); // sets its unique id for the P2P network. 
    }

    /**
     * Broadcasts an edit on the Whiteboard to all connected Peers
     * @param edit
     * @throws IOException
     */
    private void broadcastEditToPeers(EditRecord edit) throws IOException {
        for( PeerConnection pc : this.whiteBoard.getPeerConnections()) {
            pc.sendEdit(edit);
        }
    }
   
    public void startInputHandler() throws IOException {
        this.listen.start(); // listen for incoming peer connections 

        System.out.println(welcomeMessage);
        String messageIn = "";
        while (!messageIn.equals("stop")) {
            messageIn = this.in.readLine();
            handleCommand(messageIn);
        }
        closeAllConnections();
    }
    
    private void closeAllConnections() throws IOException {
        this.in.close();

        for (PeerConnection pc : this.whiteBoard.getPeerConnections()) {
            pc.stopConnection();
        }
        this.listen.stopListen();
    }

    /**
     * 
     * @param command
     * @throws IOException
     */
    public void handleCommand(String command) throws IOException {
        switch (command.strip().toLowerCase()) {
            case "create":
                System.out.println(createShapeInfo);
                String shapeName = this.in.readLine().strip().toLowerCase();
                try {
                    ENUMShape shapeType = ENUMShape.valueOf(shapeName);
                    this.currentShape = whiteBoard.createShape(shapeType);
                    System.out.println(String.format("Created %s, now you can \"put\" the Shape onto the Whiteboard." , currentShape.toString()));
                } catch (final IllegalArgumentException ex) {
                    System.out.println(errorMessageShape);
                }
                break;
            case "put":
                if (this.currentShape != null) {
                    EditRecord edit = this.whiteBoard.placeShape(this.currentShape);
                    broadcastEditToPeers(edit); 
                    this.currentShape = null;
                    System.out.println(this.whiteBoard.toString());
                } else {
                    System.out.println(errorMessagePut);
                }
                break;
            case "delete":
                System.out.println(deleteInfo + this.whiteBoard.toString());
                try {
                    command = this.in.readLine();
                    int shapeId = Integer.parseInt(command);
                    EditRecord edit = this.whiteBoard.removeShape(shapeId);
                    broadcastEditToPeers(edit); 
                    System.out.println(String.format("%s removed Shape %d: ", getStatusMessage(edit != null), shapeId)
                            + this.whiteBoard.toString());
                    if(edit != null){
                        //asd
                    }
                } catch (final NumberFormatException e) {
                    System.out.println(errorMessageDelete);
                }
                break;
            case "get":
                System.out.println(this.whiteBoard.toString());
                break;
            case "stop":
                System.out.println(goodbyMessage);
                break;
            case "help":
                System.out.println(helpInfo);
                break;
            default:
                System.out.println(defaultMessage);
        }
    }
    /**
     * returns the corresponding Message if an operation was successful or not
     * 
     * @param status
     * @return message
     */
    public String getStatusMessage(boolean status) {
        if (status){
            return successMessage;
        } else {
            return failureMessage;
        }
    }

    public static void main(String[] args) throws IOException{
        Peer peer;
        if (args.length == 3){
            int PORT = Integer.parseInt( args[0] );
            String firstPeerHost = args[1];
            int firstPeerPort = Integer.parseInt( args[2]);
            peer = new Peer(PORT, firstPeerHost, firstPeerPort);
        } else if (args.length == 1) {
            int PORT = Integer.parseInt( args[0] );
            peer = new Peer(PORT);
        } else {
            peer = null; 
        }
        
        try {
            peer.startInputHandler();
        } catch (Exception e) {
            System.err.println("Could not start Peer!");
            e.printStackTrace();
			System.exit(1);
        }
    }
}