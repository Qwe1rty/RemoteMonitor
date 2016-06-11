package head;
import gui.ServerFrame;

import java.awt.HeadlessException;
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
		int warningResponse = JOptionPane.showConfirmDialog(null, 
				"This program will have the ability to record and save keyboard and screen"
						+ System.getProperty("line.separator")
						+ "from another computer. Misuse of this program can be considered a federal"
						+ System.getProperty("line.separator")
						+ "crime and can be punished severely. Only use this program with the"
						+ System.getProperty("line.separator")
						+ "expressed consent of the client computer's owner(s)."
						+ System.getProperty("line.separator")
						+ System.getProperty("line.separator")
						+ "Are you sure you would like to proceed?",
						"Program usage warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (warningResponse != 0) System.exit(0);

		// Prompts user to enter a key and generates hash
		boolean firstInput = true;
		do {
			String seed;

			// Show input dialog
			if (firstInput) seed = JOptionPane.showInputDialog(frame, "Enter an authentication key\nIt must be 8 characters or longer"); 
			else seed = JOptionPane.showInputDialog(frame, "That is an invalid key! Try again\nEnter an authentication key\nIt must be 8 characters or longer");

			// If user clicked cancel or red X, exit program
			// If string is less than 8 chars long, retry
			// Otherwise generate hash
			if (seed == null) System.exit(0);
			else if (seed.length() >= 8) {

				// Try to generate hash. If unable to (exception thrown), close program
				hash = generateHash(seed);
				if (hash == null) {
					JOptionPane.showMessageDialog(null,
							"Program was unable to generate hash from entered key"
									+ System.getProperty("line.separator")
									+ "Remote monitor server will now terminate"
									, "Program error", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}
				break;
			} else firstInput = false;
		} while (true);

		// Creates the server frame
		frame = new ServerFrame("Remote Monitor");

		// Sets up authentication listener. Main thread will be listening for new connections
		// until program is closed
		clientServer = new ConnectionServer();
		try {clientServer.initializeServer(hash, PORT);}
		catch (IOException e) { // If something goes wrong

			System.out.println("SERVER SOCKET ERROR");
			e.printStackTrace();

			// Notify user that something went wrong
			JOptionPane.showMessageDialog(null, 
					"An unexpected connectivity error occured in the server socket"
							+ System.getProperty("line.separator")
							+ "Program will now exit. Please restart the program to try again", 
							"Unexpected error", JOptionPane.ERROR_MESSAGE);
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
	
	/**
	 * Displays a dialog to the user that a certain connection has been lost
	 * @param connection IP address of the disconnected client
	 */
	public static void displayConnectionCutDialog(InetAddress connection) {
		JOptionPane.showMessageDialog(null, 
				"The connection with " + connection.getHostAddress() + " has been lost."
				+ System.getProperty("line.separator")
				+ "This client will be removed from the server."
				, "Connection lost", JOptionPane.INFORMATION_MESSAGE);
	}

	// The following are all basically wrapper methods for the private fields

	/**
	 * Updates the GUI client list
	 */
	public static void updateClientList() {frame.updateList();}
	
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
