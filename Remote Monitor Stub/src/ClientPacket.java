import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ClientPacket {

	/**
	 * Sends a UDP authentication pack to the server with the hash. Will then 
	 * listen on specified port for the return connection packet
	 * If the server determines the hash matches, a connection packet is sent back
	 * @param hash The SHA1 authentication hash
	 * @param serverAddress The internal address of the server
	 * @return Returns a boolean indicating whether the authentication was successful
	 * @throws IOException
	 */
	public static boolean sendAuthPacket(String hash, InetAddress serverAddress, int port) throws IOException, BindException {
		// Sets up socket
		DatagramSocket clientSocket = new DatagramSocket(port);

		// Sends authentication packet to server address
		byte[] sendData = (PacketHeader.AUTH + " " + hash).getBytes();
		clientSocket.send(new DatagramPacket(sendData, sendData.length, serverAddress, port));
		System.out.println("AUTHENTICATION PACKET SENT");

		// Listens for a return connection packet from server
		byte[] receiveData = (PacketHeader.CONN + " " + hash).getBytes();
		DatagramPacket responsePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(responsePacket);
		System.out.println("CONNECTION RESPONSE RECEIVED: " + responsePacket.getAddress().getHostAddress());

		// Deconstructs message and sees if the return packet's hash also matches
		try {
			String[] message = new String(responsePacket.getData()).split(" ");
			if (message[0].equals(PacketHeader.CONN) && message[1].equals(hash))
				return true;
			else return false;
		} catch (Exception e) {return false;}
		finally {clientSocket.close();}
	}
}
