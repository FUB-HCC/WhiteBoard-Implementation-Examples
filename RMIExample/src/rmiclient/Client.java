package rmiclient;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import rmiinterface.WhiteBoardInterface;

public class Client {
	private static WhiteBoardInterface wbInterface;

	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
		String name = "localhost";
		if (args.length==1){name=args[0];}
		String[] list = Naming.list(String.format("rmi://%s:1099", name));
		System.out.println(String.join(" ", list));
		
		wbInterface = (WhiteBoardInterface) Naming.lookup(String.format("rmi://%s:1099/WhiteBoard", name));
		WhiteBoardHandler whiteBoardHandler = new WhiteBoardHandler(wbInterface); 
		whiteBoardHandler.startInputHandler(); 	// starts the input interaction with the remote whiteboard  
	}
}
