package head;
import gui.ServerFrame;

import java.awt.HeadlessException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import net.ConnectionServer;

public class RemoteMonitorServer {

	public static final int PORT = 60922;
	
	public static ConnectionServer clientServer;
	public static ServerFrame frame;
	public static String hash;

	public static void main(String[] args) throws IOException, HeadlessException, NoSuchAlgorithmException {

		// Greatest line of code of all time 
		try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());} catch (Exception e) {}

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
				hash = generateHash(seed);
				break;
			} else firstInput = false;
		} while (true);

		// Creates the server frame
		frame = new ServerFrame("Remote Monitor");

		// Sets up authentication listener. Main thread will be listening for new connections
		// until program is closed
		clientServer = new ConnectionServer();
		clientServer.initializeServer(hash, PORT);
	}

	public static void updateClientList() {frame.updateList();}
	
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

}
