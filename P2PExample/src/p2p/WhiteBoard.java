package p2p;

import java.util.ArrayList;
import java.util.Collections;




/**
* implements the following Whiteboard functionalities:
* Select (available shapes: triangle, rectangle, circle)
* put Shape onto the Whiteboard 
* delete shape on Whiteboard
* get shape by id from Whiteboard
*/


/**
 * @param connections   - connections to all other peer is place here, because WhiteBoard is the share class between threads
 * @param record        - state and place of unique truth 
 * @param logicalTime   - shared between all peers to generate an ordering of EditRecords 
 * @param board         - representation of the whiteboard state
 * @param peerId        - to reference the user of any edit
 * 
 */
public class WhiteBoard {
    
    private ArrayList<BoardEntry> board;
    private ArrayList<EditRecord> record; 
    private ArrayList<PeerConnection> connections;
    private int logicalTime;
    private int peerId;

    /**
     * 
     * @param peerId
     */
    public WhiteBoard(int peerId){
        this.board = new ArrayList<BoardEntry>();
        this.record = new ArrayList<EditRecord>();
        this.connections = new ArrayList<PeerConnection>();
        this.logicalTime = 0; 
        this.peerId = peerId;
    }

    @Override
    public String toString(){
    	String counter = new String();
    	for (BoardEntry boardEntry : board) {
			counter += boardEntry.toString();
		}
    	
        return String.format("WhiteBoard State:\n%s\nMy State: time: %d Id: %d", counter, this.logicalTime, this.peerId);
    }

    public ArrayList<EditRecord> getRecord() {
        return this.record;
    }
    public ArrayList<BoardEntry> getBoard(){
        return this.board;
    }

    public int getPeerId(){ return this.peerId; }
    
    public void setPeerId(int id){ this.peerId = id; }

    public void updateLogicalTime() {
        int lastIndex = this.record.size()-1;
        this.logicalTime = this.record.get(lastIndex).getLogicalTimestamp() + 1; 
    }
    /**
     * generates the state of the board 
     */
    public void buildBoardFromRecord() {
        this.board.clear();
        Collections.sort(this.record); 
        for( EditRecord e: this.record){
            if (e.getEdit()==Edit.add){
            	this.board.add(new BoardEntry(e.getShape(),e.getLogicalTimestamp(), e.getPeerId()));
            } else {
                this.board.remove(getBoardEntry(e.getShape().id)); //remove shape from board by reference 
            }
        }
    }
    public void addAllEditRecord(EditRecord[] edits) {
        if (edits.length > 0){
            for(EditRecord e: edits){
                this.record.add(e);
            }
            buildBoardFromRecord();
            updateLogicalTime();
        }
    }
    
    public void addEditRecord(EditRecord edit) {
        this.record.add(edit);
        buildBoardFromRecord();
        updateLogicalTime();
	}
    public Shape createShape(ShapeType type){
        Shape shape = new Shape(type, getShapeId());
        return shape; 
    }

    private int getShapeId() {
        return Integer.parseInt( this.peerId + "" + this.logicalTime );
    }

    public BoardEntry getBoardEntry(int shapeID) {
        for (BoardEntry boardEntry : board) {
			if(boardEntry.getShape().id == shapeID)
				return boardEntry;
		}
        return null;
    }

    public EditRecord placeShape(Shape shape){
        this.board.add(new BoardEntry(shape, this.logicalTime, this.peerId));
        EditRecord edit = new EditRecord(Edit.add, shape, this.logicalTime, this.peerId);
        this.record.add(edit); 
        this.logicalTime ++; 
        return edit;
    }
    
    public EditRecord removeShape(int shapeID){
    	BoardEntry bEntry = this.getBoardEntry(shapeID);
        EditRecord edit = new EditRecord(Edit.remove, bEntry.getShape(), this.logicalTime, this.peerId);
        boolean flag =  this.board.remove(bEntry);
        if(flag) {
            this.record.add(edit);
            this.logicalTime ++; 
            return edit;
        }
        return null;
    }

	public void addPeerConnection(PeerConnection pc) {
        this.connections.add(pc);
    }

    public void removePeerConnection(PeerConnection pc) {
        this.connections.remove(pc);
	}

    public ArrayList<PeerConnection> getPeerConnections() {
        return this.connections; 
    }

	
    
}