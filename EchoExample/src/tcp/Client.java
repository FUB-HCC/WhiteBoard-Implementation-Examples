package tcp;

import java.io.*;
import java.net.*;

public class Client {
	
	/**
	 * @param args[0] host name, e.g. localhost and args[1] port, e.g. 1234 
	 * @throws IOException
	 */
	public static void main(String args[]) throws IOException { 

		Socket socket = new Socket(args[0], Integer.parseInt(args[1])); 

		PrintStream out = new PrintStream(socket.getOutputStream());
		
		// BufferedReader reads Keyboard Input
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

		while (true) { // ^D will close dialogue
			String message = keyboard.readLine();
			if (message == null)
				break;
			out.println(message);
			
			String answer = in.readLine();
			System.out.println(answer);
		}
		in.close();
		out.close();
		socket.close();
	}
}