package main;
import gui.ServerDialog;
import gui.ServerFrame;

import java.awt.HeadlessException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import net.ConnectionServer;

/**
 * This program will monitor various computers within a Local Area Network.
 * The main class will run the program, and it contains random various methods
 * that are used throughout the application and don't belong anywhere else
 *  
 * @author Caleb Choi
 */
public class RemoteMonitorServer {

	private static final int PORT = 60922;

	private static ConnectionServer clientServer;
	private static ServerFrame frame;
	private static String hash;

	public static void main(String[] args) 
			throws HeadlessException, NoSuchAlgorithmException, UnsupportedEncodingException, UnknownHostException {

		// Greatest line of code of all time 
		try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());} catch (Exception e) {}

		// Ensures that when program is closed, all sockets and connections are closed
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				if (clientServer != null) {
					System.out.println("CLOSING ALL SOCKET CONNECTIONS");
					clientServer.shutdown();
				} else System.out.println("CLOSING PROGRAM");
			}
		}));
		
		// Shows the warning message not to use this program in a dumb manner
		if (ServerDialog.showUsageWarningDialog() != 0) System.exit(0);

		// Prompts user to enter a key and generates hash
		boolean firstInput = true;
		do {
			// Show input dialog
			String seed = ServerDialog.showKeyDialog(firstInput); 

			// If user clicked cancel or red X, exit program
			// If string is less than 8 chars long, retry
			// Otherwise generate hash
			if (seed == null) System.exit(0);
			else if (seed.length() >= 8) {

				// Try to generate hash. If unable to (exception thrown), close program
				hash = generateHash(seed);
				if (hash == null) {
					ServerDialog.showHashGenerationFailureDialog();
					System.exit(0);
				}
				break;
			} else firstInput = false;
		} while (true);

		// Creates the server frame
		frame = new ServerFrame("Remote Monitor - " + InetAddress.getLocalHost().getHostAddress());

		// Sets up authentication listener. Main thread will be listening for new connections
		// until program is closed
		clientServer = new ConnectionServer();
		try {clientServer.initializeServer(hash, PORT);}
		catch (IOException e) { // If something goes wrong

			// Notify user that something went wrong
			System.out.println("SERVER SOCKET ERROR");
			ServerDialog.showServerSocketErrorDialog();
			System.exit(3);
		}

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
	
	// The following are all basically wrapper methods for the private fields

	/**
	 * Updates the GUI client list
	 */
	public static void updateClientList() {frame.updateList();}

	/**
	 * Gets the side panel to add the text onto the text field if that exists
	 * @param key Text to add onto the text area
	 */
	public static void addText(String key) {frame.addText(key);}
	
	/**
	 * Tells the client server to remove a client from the connections list
	 * 
	 * @param client IP address of the client in InetAddress form
	 * @return A boolean representing whether the client was found and
	 * successfully removed
	 */
	public static boolean removeConnection(InetAddress client) {
		return clientServer.removeConnection(client);
	}
	
	/**
	 * Resets the side panel to a specified image
	 * @param img Image to be displayed
	 */
	public static void resetPictureArea(BufferedImage img) {frame.resetPictureArea(img);}
	
	/**
	 * Tells the client server to remove all dead connections. A dialog box
	 * will appear telling the user how many clients were removed
	 */
	public static void removeDeadConnections() {
		JOptionPane.showMessageDialog(null, 
				String.valueOf(clientServer.removeDeadConnections()) 
				+ " dead connection(s) have been removed"
				, "Operation result", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Tells the client server to remove all client connections. A dialog box
	 * will appear telling the user how many clients were terminated/removed
	 */
	public static void removeAllConnections() {
		JOptionPane.showMessageDialog(null,
				String.valueOf(clientServer.removeAllConnections())
				+ " connection(s) have been removed"
				, "Operation result", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Returns the client server's connection list
	 * @return ArrayList of all current active client connections
	 */
	public static ArrayList<InetAddress> getConnectionList() {return clientServer.getConnectionList();}

	/**
	 * Sends a request to the client server
	 * @param client Client to sent request to
	 * @param header Request header
	 */
	public static void requestOperation(InetAddress client, String header)
	{clientServer.requestOperation(client, header);}
}
