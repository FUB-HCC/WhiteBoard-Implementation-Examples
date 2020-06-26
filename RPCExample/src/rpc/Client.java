package rpc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    public static void main(String[] args) {
        int PORT = 12345;
        String HOST = "localhost";
		if (args.length==1){HOST=args[0];}
        
        BufferedReader bufferReader = new BufferedReader(new InputStreamReader(System.in));
        Socket socket = null;
        try {
            socket = new Socket(HOST, PORT);        // connect to the server on port 12345 localhost 
        } catch (UnknownHostException e) {          // throws Exception if server is not running
            System.out.println(e.getMessage());
            System.exit(1);
        } catch (IOException e){
            System.out.println(e.getMessage());
            System.exit(1);
        }
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream out = new PrintStream(socket.getOutputStream());
            System.out.println("write Commands here: ");
            System.out.println(in.readLine());

            String inputCommand = "";
            String serverResponds = "";
            while(!serverResponds.equals("goodby")){
                inputCommand = bufferReader.readLine();
                out.println(inputCommand);                 
                serverResponds = in.readLine();
                System.out.println(serverResponds);
            }
            socket.close();
            bufferReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}