package rpc;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {
    
    private ServerSocket serverListen;
    private WhiteBoard whiteBoard;

    /**
	 * port to server
	 * @param port used to connect to client
	 * 
	 */
    public SimpleServer(int port) throws IOException {
        this.serverListen = new ServerSocket(port); 
        this.whiteBoard = new WhiteBoard();  // one shared Whiteboard between all Clients 
    }
    /**
     * Starts the Server and does all the control for incoming connections
     */
    public void startServer() throws IOException{
        while (true) {
            System.out.println("Server is Listening......");
            Socket socket=serverListen.accept();
            new WhiteBoardHandler(socket, this.whiteBoard).startCommunicationHandler(); //calls the run method of the WhiteBoardHandler, which takes care closing the socket
            System.out.println("Connection closed"); 
        }
    }
    public static void main(String[] args) throws IOException{
        SimpleServer server = new SimpleServer(12345);
        try {
            server.startServer();
        } catch (Exception e) {
            System.err.println("Server coudn't be started");
			e.printStackTrace();
			System.exit(1);
        }
        
    }
}