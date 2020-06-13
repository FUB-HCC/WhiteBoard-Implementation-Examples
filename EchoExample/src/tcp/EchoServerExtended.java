package tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServerExtended extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintStream out;

    public EchoServerExtended(Socket socket) throws IOException{
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintStream(socket.getOutputStream());
    }

    public void run() {
        try {
            while (true) { // end of input stream will close dialogue
                String message = this.in.readLine();
                if(message==null) {
                    System.out.println("Socket closed.");
                    break;
                }
                String answer = message.replace('i','o');
                this.out.println(answer);
            }
            this.in.close(); this.out.close(); this.socket.close(); // close connection
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main (String args[]) throws IOException {
        int PORT = 12345;
        ServerSocket listen = new ServerSocket(PORT); // args[0] port
        while(true) { // non-terminating server
            Socket socket = listen.accept();
            new EchoServerExtended(socket).start();
        }
    }
}