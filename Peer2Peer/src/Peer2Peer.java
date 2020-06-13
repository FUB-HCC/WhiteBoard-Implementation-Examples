
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Peer2Peer {
    
    private ServerSocket serverListen;
    private String firstPeer;
    private WhiteBoard whiteBoard;

    /**
	 * port to server
	 * @param port used to connect to client
	 * 
	 */
    public Peer2Peer(int port, String firstPeer) throws IOException {
        this.serverListen = new ServerSocket(port); 
        this.firstPeer = firstPeer;
        this.whiteBoard = new WhiteBoard();  // one shared Whiteboard between all Threats created on this server
    }
    /**
     * Starts the Server and does all the control for incoming connections assigned to Threads, secret MAIN ;) 
     */
    public void startServer() throws IOException{
        while (true) {
            System.out.println("Server is Listening......");
            Socket socket=serverListen.accept();
            //new PeerConnection(socket, this.whiteBoard).start(); //here a new Thread is stated that calls the run method of the WhiteBoardThread, which takes care closing the socket
            System.out.println("connected to new client"); 
        }
    }
    public static void main(String[] args) throws IOException{
        Peer2Peer peer = new Peer2Peer(Integer.parseInt( args[1] ), args[2]);
        try {
            peer.startServer();
        } catch (Exception e) {
            System.err.println("Server coudn't be started");
			e.printStackTrace();
			System.exit(1);
        }
        
    }
}