package rpc;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args)throws IOException {
    Scanner scannerIn = new Scanner(System.in); 
    Socket sock=new Socket("127.0.0.1", 6066);
    try {
        DataInputStream in= new DataInputStream(sock.getInputStream());
        DataOutputStream out =new DataOutputStream(sock.getOutputStream());
        System.out.println("write Command: ");
        String inputCommand = "";
        while(!inputCommand.equals("stop")){
            inputCommand = scannerIn.nextLine();
            out.writeUTF(inputCommand);
            System.out.println(in.readUTF());
        }
    } catch (Exception e) {
        e.printStackTrace();
		System.exit(1);
    } finally {
        sock.close();
        scannerIn.close();
    }
    }
}