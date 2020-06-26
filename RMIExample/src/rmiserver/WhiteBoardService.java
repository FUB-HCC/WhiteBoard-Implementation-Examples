package rmiserver;

//import java.rmi.Naming;
//import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import rmiinterface.*;

/**
* implements the following Whiteboard functionalities:
* Select (available shapes: triangle, rectangle, circle)
* put Shape onto the Whiteboard 
* delete shape on Whiteboard
* get shape by id from Whiteboard
*/

public class WhiteBoardService implements WhiteBoardInterface {
    
    //private static final long serialVersionUID = 1L;
    private ArrayList<Shape> board;
    private int shapeIdCounter;

    public WhiteBoardService() throws RemoteException {
        super();
        this.board = new ArrayList<Shape>();
        this.shapeIdCounter = 0; 
    }

    @Override
    public String toString(){
        return String.format("WhiteBoard: %s", this.board.toString());
    }

    /**
     * make the overwritten toString methode available remotely 
     */
    public String whiteBoardToString(){
        return this.toString();
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

    /**
     * starts the server and makes the Whiteboard object available to clients 
     * @param args
     */
    public static void main(String[] args){
		try {
			WhiteBoardService aWhiteBoard = new WhiteBoardService();
			WhiteBoardInterface stub = (WhiteBoardInterface) UnicastRemoteObject.exportObject(aWhiteBoard,0); 
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("WhiteBoard", stub);  // binding the remote object to an url using the default port 1099 of the rmiregistry        
            System.err.println("Server ready");
            
        } catch (Exception e) {
        	System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
	}
}