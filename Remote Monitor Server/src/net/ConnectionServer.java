package net;

import head.RemoteMonitorServer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ConnectionServer {

	private ArrayList<Connection> clients;

	public ConnectionServer() throws IOException {
		// Initialize client list
		clients = new ArrayList<Connection>();
	}

	public void initializeServer(String hash, int port) throws IOException {
		// Set up TCP listener
		@SuppressWarnings("resource")
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

	/**
	 * Returns a list of all connected computers
	 * @return List of the IP addresses of all connected clients
	 */
	public ArrayList<InetAddress> getConnectionList() {
		ArrayList<InetAddress> connectionList = new ArrayList<InetAddress>();
		for (Connection connection : clients)
			connectionList.add(connection.getInetAddress());
		return connectionList;
	}

	private class Connection implements Runnable {

		private Socket connection;
		private String clientAddress;
		private String clientHostName;
		private String hash;

		private InputStreamReader input;
		private DataOutputStream output;

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
		public String getHostName() {return clientHostName;}
		public InetAddress getInetAddress() {return connection.getInetAddress();} 

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
								System.out.println(clientAddress + "  SUCCESSFULLY CONNECTED");
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
				return;
			}
			System.out.println(clients.size() + " Connected clients");
			System.out.println();
			
			// Updates the client list in the GUI
			RemoteMonitorServer.updateClientList();

		}

	}

}
