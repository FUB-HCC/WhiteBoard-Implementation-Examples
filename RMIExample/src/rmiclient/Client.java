package rmiclient;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import rmiinterface.WhiteBoardInterface;

public class Client {
	private static WhiteBoardInterface wbInterface;

	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
		wbInterface = (WhiteBoardInterface) Naming.lookup("WhiteBoard");
		WhiteBoardHandler whiteBoardHandler = new WhiteBoardHandler(wbInterface); 
		whiteBoardHandler.startInputHandler(); 	// starts the input interaction with the remote whiteboard  
	}
}
