package udp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


class UDPClient {
    public static void main(String args []) throws Exception {
        int PORT = 9876;
        String HOST = "localhost";

    	BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        DatagramSocket clientSocket = new DatagramSocket();
        
        InetAddress IPAddress = InetAddress.getByName(HOST);
        
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];
        
        String sentence = inFromUser.readLine();
        
        sendData = sentence.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, PORT);
        clientSocket.send(sendPacket);
       
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);
        
        String modifiedSentence = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength());
        
        System.out.println("FROM SERVER: " + modifiedSentence);
       
        clientSocket.close();
    }
}
