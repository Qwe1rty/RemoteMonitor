package net;

import head.RemoteMonitorServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ServerPacket {

	public static void sendConnPacket(String hash, InetAddress clientAddress) throws IOException {
		// Sets up socket
		DatagramSocket clientSocket = new DatagramSocket();

		// Sends authentication packet to server address
		byte[] sendData = (PacketHeader.CONN + " " + hash).getBytes();
		clientSocket.send(new DatagramPacket(sendData, sendData.length, clientAddress, RemoteMonitorServer.PORT));
		clientSocket.close();
	}

}
