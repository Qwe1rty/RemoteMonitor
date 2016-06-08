package net;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
			Connection connection = new Connection(client);
			Thread clientThread = new Thread(connection);
			clientThread.start();
			clients.add(connection);
			System.out.println("NEW CONNECTION ESTABLISHED: " + connection.getAddress());
		}

		//		// Set up UDP socket to listen for stuff
		//		DatagramSocket authListener = new DatagramSocket(port);
		//		byte[] receiveData = new byte[(PacketHeader.AUTH + " " + hash).length()];
		//		
		//		// Set up TCP server socket for when people connect
		//		ServerSocket connListener = new ServerSocket(port);
		//
		//		// Keeps listening for more clients until program is closed
		//		while (true) {
		//			
		//			// Listens for incoming packets
		//			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		//			authListener.receive(receivePacket);
		//			
		//			// Checks if the message is a valid authentication packet
		//			try {
		//				String[] message = new String(receivePacket.getData()).split(" ");
		//				if (message[0].equals(PacketHeader.AUTH)) { // If header is AUTH
		//					if (message[1].equals(hash)) { // If the hash matches
		//						System.out.println("AUTHENTICATION PACKET RECEIVED");
		//						
		//						// Sends back a connection packet
		//						ServerPacket.sendConnPacket(hash, receivePacket.getAddress());
		//						System.out.println("CONNECTION PACKET SENT: " + receivePacket.getAddress().getHostAddress());
		//					}
		//				}
		//			} catch (Exception e) {System.out.println("Error in AUTH packet message");}	
		//		}
	}

	private class Connection implements Runnable {

		String clientAddress;
		Socket connection;
		InputStreamReader input;
		DataOutputStream output;

		public Connection(Socket connection) throws IOException {
			this.connection = connection;
			this.clientAddress = this.connection.getInetAddress().getHostAddress();
			input = new InputStreamReader(this.connection.getInputStream());
			output = new DataOutputStream(this.connection.getOutputStream());

		}

		public String getAddress() {return clientAddress;}

		@Override
		public void run() { 
			
			// Listens for the authentication packet
			while (true) {
				try {
					if (input.ready()) {
						String[] authMessage = new BufferedReader(input).readLine().split(" ");
						
						if ()
					}
				} catch (Exception e) {

				}
			}
		}
	}

}
