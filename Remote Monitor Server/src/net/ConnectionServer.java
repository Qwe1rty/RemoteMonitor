package net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import main.RemoteMonitorServer;

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
			clients.get(i).requestOperation(PacketHeader.KILL, true);
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
				clients.get(i).requestOperation(header, false);
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

}