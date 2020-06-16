package p2p;

import java.io.Serializable;

enum Edit {
    add, remove 
}

public class EditRecord implements Comparable<EditRecord>, Serializable  {
    
    /**
     *
     */
    private static final long serialVersionUID = 8537359144349404507L;
    private Edit edit;
    private Shape shape;
    private int logicalTimestamp;
    private int peerId;
    

    public EditRecord(Edit edit, Shape shape, int logicalTime, int peerId){
        this.edit = edit;
        this.shape = shape;
        this.logicalTimestamp = logicalTime;
        this.peerId = peerId;
    }

    public Edit getEdit() { return this.edit; }
    public Shape getShape() { return this.shape; }
    public int getLogicalTimestamp() { return this.logicalTimestamp; }
    public int getPeerId() { return this.peerId; }

    @Override
    public int compareTo(EditRecord e) {
        int value = Integer.compare(this.logicalTimestamp, e.getLogicalTimestamp());
        if(value==0){
            return Integer.compare(this.peerId, e.getPeerId());
        }
        return value;
    }
}