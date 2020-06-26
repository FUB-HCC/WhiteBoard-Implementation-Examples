package aws;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread { 
    
    private Socket socket;

    public Client(String HOST, int PORT) throws UnknownHostException, IOException {
        this.socket = new Socket(HOST, PORT);
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream out = new PrintStream(socket.getOutputStream());
            System.out.println("write Commands here: ");
            System.out.println(in.readLine());

            //String inputCommand = "";
            String serverResponds = "";
            String[] commandSequence = {"create", "circle", "put", "create", "triangle", "put", "create", "rectangle", "put"};
            while(!serverResponds.equals("goodby")){
                for (String inputCommand : commandSequence) {
                    System.out.println(inputCommand);
                    out.println(inputCommand);
                    serverResponds = in.readLine();
                }
                //System.out.println(serverResponds);
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    public static void main(String[] args) {
        int PORT = 12345; 
        String HOST = "localhost";
        int numThreads = 10;
        if (args.length>0){HOST=args[0];}
        if (args.length==2){numThreads = Integer.parseInt(args[1]);}

        try {
            for (int i = 0; i < numThreads; i++) {
                new Client(HOST, PORT).start();
            }
        } catch (UnknownHostException e) {          // throws Exception if server is not running
            System.out.println(e.getMessage());
            System.exit(1);
        } catch (IOException e){
            System.out.println(e.getMessage());
            System.exit(1);
        }  
    }
}