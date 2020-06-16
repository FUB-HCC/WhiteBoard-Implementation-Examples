package p2p;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

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

    private ServerSocket serverListen;
    private WhiteBoard whiteBoard;
    private Shape currentShape;
    private BufferedReader in;

    /**
     * port to server
     * 
     * @param port used to connect to client
     * 
     */
    public Peer(int port, String firstPeerAdress, int firstPeerPort) throws IOException {
        this.whiteBoard = new WhiteBoard(1);
        this.serverListen = new ServerSocket(port);
        this.currentShape = null;
        this.in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(String.format("init Peer in port %d", this.serverListen.getLocalPort()));
        connectToFirstPeer(firstPeerAdress, firstPeerPort);
        // this.whiteBoard = new WhiteBoard(); // one shared Whiteboard between all
        // Threats created on this server
    }

    public Peer(int port) throws IOException { // first peer
        this.whiteBoard = new WhiteBoard(1); // first peerId is 1 
        this.serverListen = new ServerSocket(port);
        this.currentShape = null;
        this.in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(String.format("init Peer in port %d", this.serverListen.getLocalPort()));
    }

    private void connectToFirstPeer(String firstPeerAdress, int firstPeerPort)
            throws UnknownHostException, IOException {
        Socket socket = new Socket(firstPeerAdress, firstPeerPort);
        PeerConnection firstPC = new PeerConnection(socket, this.whiteBoard);
        this.whiteBoard.addPeerConnection(firstPC);
        try {
            int peerID = firstPC.receivePeerId(); 
            int maxPeerId = peerID; 
            System.out.println(peerID);
            String[] adressList = firstPC.getPeerAdressListAndEditRecord();
            for(String adress : adressList ) {
                String host = adress.split(" ")[0];
                int port = Integer.parseInt( adress.split(" ")[1] );
                Socket s = new Socket(host, port); 
                PeerConnection newPC = new PeerConnection(s, this.whiteBoard);
                peerID = newPC.receivePeerId();
                if (peerID > maxPeerId ) { maxPeerId = peerID; }
                newPC.sendSignalToPeer(0); 
                newPC.start();
                this.whiteBoard.addPeerConnection(newPC);
            }
            this.whiteBoard.setPeerId(maxPeerId+1);

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
    }
    private void broadcastEditToPeers(EditRecord edit) {
        for( PeerConnection pc : this.whiteBoard.getPeerConnections()) {
            try {
                pc.sendEdit(edit);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    /**
     * Starts the Server and does all the control for incoming connections assigned to Threads, secret MAIN ;) 
     */
    public void startServer() throws IOException{
        new ListenThread(this.whiteBoard, this.serverListen).start();
        startInputHandler();
    }

    public void startInputHandler() {
        System.out.println(welcomeMessage);
        try {
            String messageIn = "";
            while (!messageIn.equals("stop")) {
                messageIn = this.in.readLine();
                handleCommand(messageIn);
            }
            this.in.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
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
            peer.startServer();
        } catch (Exception e) {
            System.err.println("Server coudn't be started");
            e.printStackTrace();
			System.exit(1);
        }
        
    }
}