package net;

import head.RemoteMonitorServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;

public class ConnectionServer {

	public ConnectionServer(String hash, int port) throws IOException {
		
		// Set up UDP socket to listen for stuff
		DatagramSocket authListener = new DatagramSocket(port);
		byte[] receiveData = new byte[(PacketHeader.AUTH + " " + hash).length()];
		
		// Set up TCP server socket for when people connect
		ServerSocket connListener = new ServerSocket(port);

		// Keeps listening for more clients until program is closed
		while (true) {
			
			// Listens for incoming packets
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			authListener.receive(receivePacket);
			
			// Checks if the message is a valid authentication packet
			try {
				String[] message = new String(receivePacket.getData()).split(" ");
				if (message[0].equals(PacketHeader.AUTH)) { // If header is AUTH
					if (message[1].equals(hash)) { // If the hash matches
						System.out.println("AUTHENTICATION PACKET RECEIVED");
						
						// Sends back a connection packet
						ServerPacket.sendConnPacket(hash, receivePacket.getAddress());
						System.out.println("CONNECTION PACKET SENT: " + receivePacket.getAddress().getHostAddress());
					}
				}
			} catch (Exception e) {System.out.println("Error in AUTH packet message");}	
		}
	}

}
