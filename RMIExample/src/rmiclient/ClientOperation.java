package rmiclient;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


import rmiinterface.RMIInterface;

public class ClientOperation {
	private static RMIInterface look_up;

	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
		
		look_up = (RMIInterface) Naming.lookup("//localhost/WhiteBoard");
		WhiteBoardHandler whiteBoardHandler = new WhiteBoardHandler(look_up);
		whiteBoardHandler.startCommunicationHandler();
	}
}
