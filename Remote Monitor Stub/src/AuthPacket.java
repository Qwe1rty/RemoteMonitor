import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class AuthPacket {
	
	public static void sendAuthPacket(String hash, InetAddress serverAddress) throws IOException {
		DatagramSocket clientSocket = new DatagramSocket();
		
		byte[] sendData = (PacketHeader.AUTH + " " + hash).getBytes();
		clientSocket.send(new DatagramPacket(sendData, sendData.length, serverAddress, RemoteMonitor.PORT));
		
	}
}
