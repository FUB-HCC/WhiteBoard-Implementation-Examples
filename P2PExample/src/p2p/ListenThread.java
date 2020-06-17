package p2p;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ListenThread extends Thread {

    private WhiteBoard whiteBoard;
    private ServerSocket serverListen;
    private boolean exit;

    /**
     * This thread listens for peer to connect and establishes a PeerConnection
     * @param wb
     * @param serverListen
     */
    public ListenThread(WhiteBoard wb, ServerSocket serverListen) {
        this.whiteBoard = wb;
        this.serverListen = serverListen;
        this.exit = false;
    }

    public void run() {
        try {
            System.out.println(String.format("Peer is Listening on port %s ......", this.serverListen.getLocalPort()));
            while (!this.exit) {
                Socket socket = this.serverListen.accept();
                PeerConnection pc = new PeerConnection(this.whiteBoard, socket,
                        socket.getInetAddress().getHostAddress(), 0);
                pc.receivePort();
                pc.sendPeerId(this.whiteBoard.getPeerId());
                int sig = pc.getSignalFromPeer();
                if (sig == 1) {
                    pc.sendPeerAddressListAndEditRecord();
                }
                this.whiteBoard.addPeerConnection(pc);
                pc.start(); // here a new Thread is stated that calls the run method of the
                            // WhiteBoardThread, which takes care closing the socket

                System.out.println(String.format("connected to new peer on %s", pc.getPeerAddress()));
            }
            this.serverListen.close();
            System.out.println("Stop Listening");
        } catch (SocketException e) { // socket closed by stopListen
            //e.printStackTrace(); 
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        } 
    }

    public void stopListen() throws IOException {
        this.exit = true;
        this.serverListen.close(); 
    }
}