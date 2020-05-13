package rpc;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    
    static final int PORT = 6066;
    private ServerSocket serverListen;
    private WhiteBoard whiteBoard;

    public Server(){
        try{
            this.serverListen = new ServerSocket(PORT); 
            this.whiteBoard = new WhiteBoard();  // one shared Whiteboard between all Threats created on this server
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void runServer(){
        while (true) {
            try {
            System.out.println("Server is Listening......");
            Socket socket=serverListen.accept();
            new WhiteBoardThread(socket, this.whiteBoard).start(); // the new Thread takes care closing the socket
            System.out.println("connected to new client"); 
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        } 
    }
    }
    public static void main(String[] args) {
        Server server = new Server();
        server.runServer();
    }
}