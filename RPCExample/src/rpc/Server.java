package rpc;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    
    private ServerSocket serverListen;
    private WhiteBoard whiteBoard;

    /**
	 * port to server
	 * @param port used to connect to client
	 * 
	 */
    public Server(int port) throws IOException {
        this.serverListen = new ServerSocket(port); 
        this.whiteBoard = new WhiteBoard();  // one shared Whiteboard between all Threats created on this server
    }
    /**
     * Starts the Server and does all the control for incoming connections assigned to Threads, secret MAIN ;) 
     */
    public void startServer() throws IOException{
        while (true) {
            System.out.println("Server is Listening......");
            Socket socket=serverListen.accept();
            new WhiteBoardThread(socket, this.whiteBoard).start(); //here a new Thread is stated that calls the run method of the WhiteBoardThread, which takes care closing the socket
            System.out.println("connected to new client"); 
        }
    }
    public static void main(String[] args) throws IOException{
        Server server = new Server(12345);
        try {
            server.startServer();
        } catch (Exception e) {
            System.err.println("Server coudn't be started");
			e.printStackTrace();
			System.exit(1);
        }
        
    }
}