package rpc;

import java.util.ArrayList;

/**
* implements the following Whiteboard functionalities:
* Select (available shapes: triangle, rectangle, circle)
* put Shape onto the Whiteboard 
* delete shape on Whiteboard
* get shape by id from Whiteboard
*/

public class WhiteBoard {
    private ArrayList<Shape> board;
    private int shapeIdCounter;
    public WhiteBoard(){
        this.board = new ArrayList<Shape>();
        this.shapeIdCounter = 0; 
    }

    @Override
    public String toString(){
        return String.format("WhiteBoard: %s", this.board.toString());
    }

    public ArrayList<Shape> getBoard(){
        return this.board;
    }
    public Shape createShape(ENUMShape name){
        Shape shape = new Shape(name, this.shapeIdCounter);
        this.shapeIdCounter++;
        return shape; 
    }

    public Shape getShape(int shapeID){
        for (int i = 0; i < this.board.size(); i++) {
            if (this.board.get(i).id==shapeID){
                return this.board.get(i);
            }
        }
        return null;
    }

    public boolean placeShape(Shape shape){
        return this.board.add(shape);
    }
    
    public boolean removeShape(int shapeID){
        Shape shape = this.getShape(shapeID);
        return this.board.remove(shape);
    }
}