package rmiinterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * interface of the Whiteboard extends the Remote interface to make its functionality available remotely 
 */
public interface WhiteBoardInterface extends Remote {
	public ArrayList<Shape> getBoard() throws RemoteException;
	public Shape createShape(ENUMShape name) throws RemoteException;
	public Shape getShape(int shapeID) throws RemoteException;
	public boolean placeShape(Shape shape) throws RemoteException;
	public boolean removeShape(int shapeID) throws RemoteException;
	public String whiteBoardToString() throws RemoteException;
}
