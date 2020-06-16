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


public class WhiteBoard {
    
    private ArrayList<Shape> board;
    private ArrayList<EditRecord> record;
    private ArrayList<PeerConnection> connections;
    private int logicalTime;
    private int peerId;

    public WhiteBoard(int peerId){
        this.board = new ArrayList<Shape>();
        this.record = new ArrayList<EditRecord>();
        this.connections = new ArrayList<PeerConnection>();
        this.logicalTime = 0; 
        this.peerId = peerId;
    }

    @Override
    public String toString(){
        return String.format("WhiteBoard: %s, time: %d, peerId: %d", this.board.toString(), this.logicalTime, this.peerId);
    }

    public ArrayList<EditRecord> getRecord() {
        return this.record;
    }
    public ArrayList<Shape> getBoard(){
        return this.board;
    }

    public int getPeerId(){ return this.peerId; }
    
    public void setPeerId(int id){ this.peerId = id; }

    public void updateLogicalTime() {
        int lastIndex = this.record.size()-1;
        this.logicalTime = this.record.get(lastIndex).getLogicalTimestamp(); 
    }

    public void buildBoardFromRecord() {
        this.board.clear();
        Collections.sort(this.record); 
        for( EditRecord e: this.record){
            if (e.getEdit()==Edit.add){
                this.board.add(e.getShape());
            } else {
                this.board.remove(e.getShape());
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
    public Shape createShape(ENUMShape name){
        Shape shape = new Shape(name, getShapeId());
        return shape; 
    }

    private int getShapeId() {
        return Integer.parseInt( this.peerId + "" + this.logicalTime );
    }

    public Shape getShape(int shapeID) {
        for (int i = 0; i < this.board.size(); i++) {
            if (this.board.get(i).id==shapeID){
                return this.board.get(i);
            }
        }
        return null;
    }

    public EditRecord placeShape(Shape shape){
        this.board.add(shape);
        EditRecord edit = new EditRecord(Edit.add, shape, this.logicalTime++, this.peerId);
        this.record.add(edit); 
        return edit;
    }
    
    public EditRecord removeShape(int shapeID){
        Shape shape = this.getShape(shapeID);
        EditRecord edit = new EditRecord(Edit.remove, shape, this.logicalTime++, this.peerId);
        boolean flag =  this.board.remove(shape);
        if(flag) {
            this.record.add(edit);
            return edit;
        }
        return null;
    }

	public void addPeerConnection(PeerConnection pc) {
        this.connections.add(pc);
    }

    public ArrayList<PeerConnection> getPeerConnections() {
        return this.connections; 
    }
    
}