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

/**
 * The server for all client connections. Manages and maintains requests
 * @author Caleb Choi
 */
public class ConnectionServer {

	private ArrayList<Connection> clients;
	private ServerSocket connListener;

	/**
	 * Initializes the client list. Server will not be initialized, and should be
	 * done through intitializeServer()
	 */
	public ConnectionServer() {
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
		this.connListener = new ServerSocket(port);
		while (true) {

			// Listens indefinitely for clients
			Socket client = connListener.accept();

			// Creates a new connection which listens for stuff in its own thread
			Connection connection = new Connection(client, hash);
			Thread clientThread = new Thread(connection);
			clientThread.start();
			clients.add(connection);
			System.out.println("NEW UNAUTHENTICATED CONNECTION: " + connection.getAddress());
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
	 * Deletes a client off of the client list. This will not cut connections between
	 * the client, as that is done through requestOperation() in Connection.
	 * The GUI will also updated along the way
	 * 
	 * @param client The IP address of the client to remove
	 * @return True if a connection was successfully removed, false if no connection
	 * was removed or something went wrong
	 */
	public synchronized boolean removeConnection(InetAddress client) {
		
		// Finds the selected client and removes it and returns true
		for (int i = 0; i < clients.size(); i++) {
			if (clients.get(i).getAddress().equals(client.getHostAddress())) {
				clients.remove(i);
				RemoteMonitorServer.updateClientList();
				return true;
			}
		}
		
		// Returns false if nothing happened
		return false;
	}
	
	/**
	 * Tries to remove all dead connections. Connections that are simply inactive
	 * will not be removed
	 * @return Number of clients that were moved
	 */
	public synchronized int removeDeadConnections() {
		
		// Tries to send a CONN packet to all the clients. If something happens,
		// the client is removed from the list
		int connectionsRemoved = 0;
		for (int i = 0; i < clients.size(); i++)
			if (clients.get(i).isConnectionDead()) {
				clients.get(i).shutdown();
				connectionsRemoved++;
//				clients.remove(i);
			}
		RemoteMonitorServer.updateClientList();
		return connectionsRemoved;
	}
	
	/**
	 * Removes every single client connection regardless of what they were doing.
	 * A kill request will be sent to the client, but there is no guarantee that
	 * it will be received and interpreted
	 * @return Number of clients that were removed
	 */
	public synchronized int removeAllConnections() {
		// Clears eeeeeeveerrrrrryyyyttthhhiiiiinnnnngggg
		int connectionsRemoved = 0;
		for (int i = 0; i < clients.size(); i++) {
			clients.get(i).requestOperation(PacketHeader.KILL);
			connectionsRemoved++;
//			clients.remove(i);
		}
		RemoteMonitorServer.updateClientList();
		return connectionsRemoved;
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
			clients.get(i).killOperation();

		// Pause for a quick moment so the threads can die
		try {Thread.sleep(200);} catch (Exception e) {}
		
		// Finds the requested thread
		for (int i = 0; i < clients.size(); i++)
			if (clients.get(i).getAddress().equals(client.getHostAddress())) {
				clients.get(i).requestOperation(header);
				return;
			}
	}

	/**
	 * Attempts to shut down all connections, including the server socket and every
	 * single individual client connection. There is no guarantee that every single
	 * connection will successfully be closed
	 */
	public synchronized void shutdown() {
		
		// Iterates through every client and shuts them down
		for (int i = 0; i < clients.size(); i++)
			clients.get(i).shutdown();
		
		// Try to shut down server socket
		try {
			connListener.close();
			System.out.println("SERVER SOCKET SUCESSFULLY CLOSED");
		} catch (IOException e) {
			System.out.println("SERVER SOCKET COULD NOT BE CLOSED");
			e.printStackTrace();
		}
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

		/**
		 * Returns the raw IP address of the connection in textual form
		 * @return IP address of the connection
		 */
		public String getAddress() {return clientAddress;}
		
		/**
		 * Returns the host name of the connected computer
		 * @return Host name of the connected computer
		 */
		@SuppressWarnings("unused") // This warning is dumb; this is used for the list
		public String getHostName() {return clientHostName;}
		
		/**
		 * Returns the IP address of the connection as an InetAddress
		 * @return IP address of the connection
		 */
		public InetAddress getInetAddress() {return connection.getInetAddress();} 

		/**
		 * Stops the current operation if there is one
		 */
		public void killOperation() {
			if (operation != null && !operation.isInterrupted()) {
				operation.interrupt();
				operation = null;
			}
		}
		
		/**
		 * Shuts down the connection to the client. Note that the client will not
		 * receive any forewarning, and the user will not be informed of any dialog
		 * that the connection has been cut. This method will also kill any currently
		 * running operations
		 */
		public void shutdown() {
			killOperation(); // Stops whatever they were doing
			System.out.println("CLIENT " + getAddress() + " DISCONNECTED");
			
			// Will try to close all output streams
			try {input.close();} catch (IOException e) {System.out.println(getAddress() + " INPUT STREAM FAILED TO CLOSE");}
			try {output.flush();} catch (IOException e) {System.out.println(getAddress() + " OUTPUT STREAM FAILED TO FLUSH");}
			try {output.close();} catch (IOException e) {System.out.println(getAddress() + " OUTPUT STREAM FAILED TO CLOSE");}
			try {connection.close();} catch (IOException e) {System.out.println(getAddress() + " SOCKET CONNECTION FAILED TO CLOSE");}

			// Removes connection from the server list
			System.out.println("REMOVE CONNECTION RESULT: " + removeConnection(connection.getInetAddress()));
		}
		
		public boolean isConnectionDead() {
			try {
				output.writeBytes(PacketHeader.CONN + System.getProperty("line.separator"));
				return false;
			} catch (IOException ioe) {return true;}
		}
		
		/**
		 * Requests a client operation. If there is one in progress, the thread will be
		 * interrupted and cleared
		 * @param header Packet header to be sent
		 */
		public void requestOperation(final String header) {
			
			// Kills any currently running operation
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
							shutdown(); 
							RemoteMonitorServer.displayConnectionCutDialog(connection.getInetAddress());
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
							shutdown(); 
							RemoteMonitorServer.displayConnectionCutDialog(connection.getInetAddress());
						}
					}
				});
				operation.start();
				
			} else if (header.equals(PacketHeader.KILL)) { // Tells client to terminate its existence

				// Try to send a kill request to client
				try {output.writeBytes(header + System.getProperty("line.separator"));} 
				catch (IOException e) {System.out.println("KILL REQUEST WAS UNABLE TO BE SENT");}
				finally {shutdown();} // Regardless of whether the kill request was able to be sent, server closes client
				
			} else System.out.println("INVALID REQUEST HEADER - IGNORING REQUEST TO " + getAddress());
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
						shutdown();
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
					shutdown();
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