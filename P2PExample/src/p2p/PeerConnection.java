package p2p;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class PeerConnection extends Thread {

    private final Socket socket;
    private WhiteBoard whiteBoard;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String host;
    private int port;
    private boolean exit;
    /**
     * 
     * @param socket
     * @param whiteBoard
     */
    PeerConnection(WhiteBoard whiteBoard, Socket socket, String host, int port) throws IOException {
        this.whiteBoard = whiteBoard;
        this.socket = socket;
        this.host = host; 
        this.port = port;
        this.out = new ObjectOutputStream(this.socket.getOutputStream());
        this.out.flush();
        this.in = new ObjectInputStream(this.socket.getInputStream());
        this.exit = false;
    }
    public void sendEdit(EditRecord edit) throws IOException {
        this.out.writeObject(edit);
        this.out.flush();
    }
    public String[] getPeerAddressListAndEditRecord() throws IOException {
        this.out.writeInt(1); // tell the first Peer you need all data
        this.out.flush();
        try {
            String[] addressList = (String[]) this.in.readObject();
            EditRecord[] editRecord = (EditRecord[]) this.in.readObject();
            this.whiteBoard.addAllEditRecord(editRecord);
            return addressList;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void sendPeerAddressListAndEditRecord() throws IOException {
        ArrayList<PeerConnection> pcList = this.whiteBoard.getPeerConnections(); 
        String[] addressList = new String[pcList.size()]; 
        for (int i = 0; i < pcList.size(); i++) {
            addressList[i] = pcList.get(i).getPeerAddress(); 
        }
        this.out.writeObject(addressList);
        this.out.flush();
        EditRecord[] er = this.whiteBoard.getRecord().toArray(new EditRecord[0]);
        this.out.writeObject(er);
        this.out.flush();
	}
    
    public String getPeerAddress() {
        String addressAndPort = String.format("%s %d", this.host, this.port);
        return addressAndPort;
    }

    @Override
    public void run() {
        EditRecord edit;
        try {
            while (!this.exit) { 
                edit = (EditRecord) in.readObject();
                if (edit == null){
                    out.writeObject(null);
                    out.flush();
                    close(); 
                    break; // if socket closes 
                }
                this.whiteBoard.addEditRecord(edit);
            }
            this.in.close();
            this.out.close();
            this.socket.close();
        } catch (ClassNotFoundException e) {
            close(); 
            e.printStackTrace();
        } catch (IOException e) {
            close();
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
    public void receivePort() throws IOException {
		this.port = this.in.readInt();
	}
    public void sendPeerPort(int port) throws IOException {
        this.out.writeInt(port);
        this.out.flush();
	}
	public void stopConnection() throws IOException {
        this.out.writeObject(null);
        this.out.flush();
        this.exit = true;
    }
    private void close() {// removes PeerConnection from list when socket dies
        this.exit = true;
        this.whiteBoard.removePeerConnection(this); 
        System.out.println(String.format("removed PeerConnection: %s %d", this.host, this.port));
    }
}