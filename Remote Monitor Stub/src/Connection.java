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
	
	public void listen() {
		
	}
}
