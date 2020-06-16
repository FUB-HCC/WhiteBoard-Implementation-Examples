package p2p;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ListenThread extends Thread {

    private WhiteBoard whiteBoard;
    private ServerSocket serverListen;

    public ListenThread(WhiteBoard wb, ServerSocket serverListen) {
        this.whiteBoard = wb;
        this.serverListen = serverListen;
    }
    public void run() {
        try{
            while (true) {
                System.out.println("Peer is Listening......");
                Socket socket = this.serverListen.accept();
                PeerConnection pc = new PeerConnection(socket, this.whiteBoard);
                pc.sendPeerId(this.whiteBoard.getPeerId()); 
                int sig = pc.getSignalFromPeer();
                if( sig == 1) {
                    pc.sendPeerAdressListAndEditRecord();
                }
                this.whiteBoard.addPeerConnection(pc);
                pc.start(); //here a new Thread is stated that calls the run method of the WhiteBoardThread, which takes care closing the socket
    
                System.out.println(String.format("connected to new peer on %s %d", socket.getInetAddress().getHostAddress(), socket.getLocalPort())); 
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}