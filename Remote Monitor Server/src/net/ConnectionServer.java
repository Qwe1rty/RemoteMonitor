package net;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ConnectionServer {

	ArrayList<Connection> clients;

	public ConnectionServer(String hash, int port) throws IOException {

		// Initialize client list
		clients = new ArrayList<Connection>();

		// Set up TCP listener
		ServerSocket connListener = new ServerSocket(port);
		while (true) {

			// Listens indefinitely for clients
			Socket client = connListener.accept();

			// Creates a new connection which listens for stuff in its own thread
			Connection connection = new Connection(client, hash);
			Thread clientThread = new Thread(connection);
			clientThread.start();
			clients.add(connection);
			System.out.println("NEW CONNECTION ESTABLISHED: " + connection.getAddress());
		}
	}

	private class Connection implements Runnable {

		Socket connection;
		String clientAddress;
		String clientHostName;
		String hash;
		
		InputStreamReader input;
		DataOutputStream output;

		public Connection(Socket connection, String hash) throws IOException {
			this.connection = connection;
			this.clientAddress = this.connection.getInetAddress().getHostAddress();
			this.clientHostName = this.connection.getInetAddress().getHostName();
			this.hash = hash;
			
			input = new InputStreamReader(this.connection.getInputStream());
			output = new DataOutputStream(this.connection.getOutputStream());
			output.flush();
		}

		public String getAddress() {return clientAddress;}

		@Override
		public void run() { 
			
			// Listens for the authentication packet
			while (true) {
				try {
					// If there is input to be read
					BufferedReader br = new BufferedReader(input);
					if (input.ready()) {
						String[] authMessage = br.readLine().split(" ");
						
						// Checks to see if the header and hash for authentication is valid
						if (authMessage[0].equals(PacketHeader.AUTH)) {
							System.out.println("AUTHENTICATION PACKET RECEIVED:" + clientAddress);
							
							// Check if the client hash matches
							if (authMessage[1].equals(hash)) {
								System.out.println("CLIENT HASH MATCHED");
								System.out.println(clientAddress + " SUCCESSFULLY CONNECTED");
								break;
								
							} else { // If the hashes don't match, kill connection
								System.out.println("ERROR: HASH MISMATCH");
								throw new Exception("Hash mismatch");
							}
							
						} else { // If the header is invalid
							System.out.println("ERROR: INVALID AUTHENTICATION HEADER");
							throw new Exception("Invalid authentication header");
						}
					}
				} catch (Exception e) {
					System.out.println("EXITING THREAD");
					System.out.println(System.currentTimeMillis());
					e.printStackTrace();
					try {
						connection.close();
						System.out.println("CONNECTION TERMINATED");
					} catch (Exception ex) {
						System.out.println("CONNECTION COULD NOT BE TERMINATED");}
					System.out.println();
					return;
				}
			}
			
			// Sends back a confirmation packet to the client
			try {
				output.writeBytes(PacketHeader.CONN + " " + hash + System.getProperty("line.separator"));
				System.out.println("CONFIRMATION PACKET SENT");
			} catch (IOException e) {
				System.out.println("CONFIRMATION PACKET COULD NOT BE SENT");
				try {
					connection.close();
					System.out.println("TERMINATING CONNECTION");
				} catch (Exception ex) {
					System.out.println("CONNECTION COULD NOT BE TERMINATED");}
				System.out.println();
				return;
			}
			
		}
	}

}
