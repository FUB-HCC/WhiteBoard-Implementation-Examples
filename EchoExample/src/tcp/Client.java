package tcp;

import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;


public class Client {
	
	/**
	 * @param args[0] host name, e.g. localhost and args[1] port, e.g. 1234 
	 * @throws IOException
	 */
	public static void main(String args[]) throws IOException { 
		int PORT = 12345;
		String HOST = "127.0.0.1";
		
		Socket socket = new Socket(HOST, PORT); 

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