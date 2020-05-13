package rpc;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    
    static final int PORT = 6066;
    private ServerSocket serverSocket;
    private WhiteBoard whiteBoard;

    public Server(){
        try{
            this.serverSocket = new ServerSocket(PORT);
            this.whiteBoard = new WhiteBoard(); 
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void runServer() throws IOException {
        try {   
           while (true) {
            System.out.println("Server Listening......");
            Socket socket=serverSocket.accept();
            new WhiteBoardThread(socket, this.whiteBoard).start();
            System.out.println("connected to new client");
           }
       } catch (IOException e) {
            e.printStackTrace();
			System.exit(1);
       } finally {
        System.out.println("Server closes forcefully connections!");;
        this.serverSocket.close();
       }
    }
    public static void main(String[] args) throws IOException{
        Server server = new Server();
        server.runServer();
    }
}