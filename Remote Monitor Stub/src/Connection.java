import java.net.InetAddress;
import java.net.Socket;

public class Connection {
	
	// There is only one available connection ever at a time
	private static Socket connection;
	
	public static boolean establishConnection(InetAddress address, int port) {
		return true;
	}

	public static boolean isConnectionEstablished() {
		return !(connection == null);
	}

}
