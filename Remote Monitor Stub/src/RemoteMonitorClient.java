import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 * This is the client portion of the remote monitor program. When run on
 * a computer, it will be able to send keystrokes along with screen captures
 * to the specified server.
 * 
 * @author Caleb Choi
 */
public class RemoteMonitorClient {

	private static final int PORT = 60922;

	// Client has only one connection unlike server, so static fields are ok here
	private static InetAddress serverIP;
	private static Connection connection;
	private static String hash;

	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {

		// Greatest line of code of all time 
		try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());} catch (Exception e) {}

		// Ensures that when program is closed, all sockets and connections are closed
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				if (connection != null) {
					System.out.println("CLOSING ALL SOCKET CONNECTIONS");
					connection.shutdown();
				} else System.out.println("CLOSING PROGRAM");
			}
		}));

		// Shows a consent prompt to ensure that the user is willfully allowing
		// their computer to be monitored. Also, all the dialog boxes prevent someone
		// from just silently running the stub without the owner's knowledge
		if (ClientDialog.showConsentResponseDialog() != 0) System.exit(0);

		// Prompts user to enter a key and generates hash
		boolean firstInput = true;
		do {
			String seed;

			// Show input dialog
			seed = ClientDialog.showKeyDialog(firstInput);

			// If user clicked cancel or red X, exit program
			// If string is less than 8 chars long, retry
			// Otherwise generate hash
			if (seed == null) System.exit(0);
			else if (seed.length() >= 8) {
				setHash(generateHash(seed));
				break;
			} else firstInput = false;
		} while (true);

		// Setup for packet sending
		firstInput = true;
		do {
			String serverIP;

			// Show input dialog
			serverIP = ClientDialog.showServerIPDialog(firstInput);

			// Exit if used clicked cancel or red X
			if (serverIP == null) System.exit(0);

			// If the person added a colon (for port), remove it
			if (serverIP.indexOf(":") != -1)
				serverIP = serverIP.substring(0, serverIP.indexOf(":"));

			// Check if entered IP is valid. If so, keep it
			else if (isIP(serverIP)) {
				setServerIP(InetAddress.getByName(serverIP));
				break;
			} else firstInput = false;
		} while (true);

		// Try connecting with server
		try {
			connection = new Connection(getServerIP(), PORT, getHash());

			// Connection will now listen for server requests
			// Once the server sends a kill request, user is notified
			try {
				connection.listen();
				ClientDialog.showTerminationDialog();
				System.exit(0);

			} catch (IOException ie) { // If IOException is caught here, that means the client disconnected
				ClientDialog.showDisconnectionDialog();
				ie.printStackTrace();
				System.exit(2);
			}

		} catch (Exception e) { // If anything goes wrong with authentication, inform user of failure 
			e.printStackTrace();
			ClientDialog.showUnauthenticatedDialog();
			System.exit(1);
		}
	}

	/**
	 * Checks whether a provided string is a valid IP format
	 * @param ip A string to be checked
	 * @return If the IP address is valid, returns true
	 */
	public static boolean isIP(String ip) {
		// If localhost
		if (ip.equals("localhost")) return true;

		// Check if there are 4 elements that are separated by spaces
		String[] ips = ip.split("\\.");
		if (ips.length != 4) return false;

		try {
			// Check if the spaced elements are actually numbers 
			int[] ipn = new int[4];
			for (int i = 0; i < 4; i++)
				ipn[i] = Integer.parseInt(ips[i]);

			// Check if elements are within 0-255
			for (int i = 0; i < 4; i++)
				if (ipn[i] < 0 || ipn[i] > 255)
					return false;

			return true;
		} catch (Exception e) {return false;}
	}
	
	/**
	 * Generates an SHA1 hash given an input key
	 * 
	 * @param input A hash key in String format
	 * @return An SHA1 hash of the input key in String format
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 * @author Caleb Choi
	 */
	public static String generateHash(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md = MessageDigest.getInstance("SHA1");
		md.reset();
		byte[] buffer = input.getBytes("UTF-8");
		md.update(buffer);
		byte[] digest = md.digest();

		String hexStr = "";
		for (int i = 0; i < digest.length; i++)
			hexStr +=  Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1);
		return hexStr;
	}

	/**
	 * Returns the server IP in InetAddress form
	 * @return The server IP as an InetAddress
	 */
	public static InetAddress getServerIP() {return serverIP;}
	
	/**
	 * Sets the server IP address
	 * @param serverIP The server IP address as an InetAddress
	 */
	public static void setServerIP(InetAddress serverIP) {RemoteMonitorClient.serverIP = serverIP;}
	
	/**
	 * Gets the authentication hash
	 * @return The authentication hash
	 */
	public static String getHash() {return hash;}
	
	/**
	 * Sets the authentication hash
	 * @param hash The authentication hash
	 */
	public static void setHash(String hash) {RemoteMonitorClient.hash = hash;}

	/**
	 * Gets the TCP communication port 
	 * @return The TCP communication port
	 */
	public static int getPort() {return PORT;}
	
}