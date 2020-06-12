package tcp;

import java.io.*;
import java.net.*;

public class EchoServer {
	public static void main(String args[]) throws IOException {

		@SuppressWarnings("resource")
		ServerSocket listen = new ServerSocket(1234); // set port

		while (true) { // non-terminating server

			Socket socket = listen.accept();
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			PrintStream out = new PrintStream(socket.getOutputStream());

			while (true) { // end of input stream will close dialogue

				String message = in.readLine();

				if (message == null) {
					break;
				}
				String answer = message.replace('i', 'o');
				out.println(answer);
			}
			in.close();
			out.close();
			socket.close(); // close connection
			System.out.println("Socket closed.");
		}
	}
}