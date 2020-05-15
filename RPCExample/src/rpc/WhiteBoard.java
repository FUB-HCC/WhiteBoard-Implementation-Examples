package rpc;

import java.util.ArrayList;

/**
* Form wählen (available shapes: triangle, rectangle, circle)
* Form (Shape) auf Zeichenfläche ablegen
* Form von Zeichenfläche löschen
* Formen von Zeichenfläche abrufen
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