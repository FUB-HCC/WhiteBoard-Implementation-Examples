package p2p;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class PeerConnection extends Thread {

    private final Socket socket;
    private WhiteBoard whiteBoard;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    /**
     * 
     * @param socket
     * @param whiteBoard
     */
    PeerConnection(final Socket socket, WhiteBoard whiteBoard) throws IOException {
        this.socket = socket;
        this.whiteBoard = whiteBoard;
        this.out = new ObjectOutputStream(this.socket.getOutputStream());
        this.out.flush();
        this.in = new ObjectInputStream(this.socket.getInputStream());
    }
    public void sendEdit(EditRecord edit) throws IOException {
        this.out.writeObject(edit);
        this.out.flush();
    }
    public String[] getPeerAdressListAndEditRecord() throws ClassNotFoundException, IOException {
        this.out.writeInt(1); // tell the first Peer you need all data
        this.out.flush();
        String[] adressList = (String[]) this.in.readObject();
        EditRecord[] editRecord = (EditRecord[]) this.in.readObject();
        this.whiteBoard.addAllEditRecord(editRecord);
        return adressList;
    }
    public void sendPeerAdressListAndEditRecord() throws IOException {
        ArrayList<PeerConnection> pcList = this.whiteBoard.getPeerConnections(); 
        String[] adressList = new String[pcList.size()]; 
        for (int i = 0; i < pcList.size(); i++) {
            adressList[i] = pcList.get(i).getPeerAddrees(); 
        }
        this.out.writeObject(adressList);
        this.out.flush();
        EditRecord[] er = this.whiteBoard.getRecord().toArray(new EditRecord[0]);
        this.out.writeObject(er);
        this.out.flush();
	}
    
    public String getPeerAddrees() {
        InetAddress address = this.socket.getLocalAddress(); 
        String addressAndPort = String.format("%s %d", address.getHostAddress(), this.socket.getLocalPort());
        System.out.println(addressAndPort);
        return addressAndPort;
    }

    @Override
    public void run() {
        EditRecord edit;
        boolean running = true;
        while (running) { 
            try {
                edit = (EditRecord) in.readObject();
                System.out.println(String.format("received: %s", edit.getShape().toString()));
                this.whiteBoard.addEditRecord(edit);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                running = false;
            } catch (IOException e) {
                running = false;
                e.printStackTrace();
            }
        }
        try {
            this.in.close();
            this.out.close();
            this.socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	public int getSignalFromPeer() throws IOException {
        return this.in.readInt();
	}
	public void sendSignalToPeer(int signal) throws IOException {
        this.out.writeInt(signal);
	}
	public int receivePeerId() throws IOException {
		return this.in.readInt();
	}
	public void sendPeerId(int peerId) throws IOException {
        this.out.writeInt(peerId);
        this.out.flush();
	}
}