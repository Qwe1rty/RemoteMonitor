package net;

import head.RemoteMonitor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ClientServer {

	public ClientServer(String hash) throws IOException {
		// Set up UDP socket
		DatagramSocket clientListener = new DatagramSocket(RemoteMonitor.PORT);
		byte[] receiveData = new byte[(PacketHeader.AUTH + " " + hash).length()];

		// Keeps listening for more clients until program is closed
		while (true) {
			
			// Listens for incoming packets
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			clientListener.receive(receivePacket);
			
			// Checks if the message is a valid authentication packet
			try {
				String[] message = new String(receivePacket.getData()).split(" ");
				if (message[0].equals(PacketHeader.AUTH)) { // If header is AUTH
					if (message[1].equals(hash)) { // If the hash matches
						
					}
				}
			} catch (Exception e) {System.out.println("Error in AUTH packet message");}	
		}
	}

}
