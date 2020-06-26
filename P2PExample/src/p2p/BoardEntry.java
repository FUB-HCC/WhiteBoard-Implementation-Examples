package p2p;

public class BoardEntry {
	
	private Shape shape;
	private int timestamp;
	private int peerID;
	
	
	public BoardEntry(Shape shape, int timestamp, int peerID) {
		super();
		this.shape = shape;
		this.timestamp = timestamp;
		this.peerID = peerID;
	}
	
	public Shape getShape() {
		return shape;
	}

	public void setShape(Shape shape) {
		this.shape = shape;
	}

	public int getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}

	public int getPeerID() {
		return peerID;
	}

	public void setPeerID(int peerID) {
		this.peerID = peerID;
	}

	@Override
	public String toString() {
		return "[shape=" + shape + ", timestamp=" + timestamp + ", peerID=" + peerID + "]\n";
	}
	
}
