import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class RemoteMonitor {

	public static final int PORT = 60922;
	public static final int AUTH_TIMEOUT = 10; // default is 15, for 15 secs
	
	private static InetAddress serverIP;
	private static String key;

	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {

		// Greatest line of code of all time 
		try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());} catch (Exception e) {}

		// Prompts user to enter a key and generates hash
		boolean firstInput = true;
		do {
			String seed;
			
			// Show input dialog
			if (firstInput) seed = JOptionPane.showInputDialog(null, "Enter an authentication key\nIt must be 8 characters or longer"); 
			else seed = JOptionPane.showInputDialog(null, "That is an invalid key! Try again\nEnter an authentication key\nIt must be 8 characters or longer");

			// If user clicked cancel or red X, exit program
			// If string is less than 8 chars long, retry
			// Otherwise generate hash
			if (seed == null) System.exit(0);
			else if (seed.length() >= 8) {
				key = generateHash(seed);
				break;
			} else firstInput = false;
		} while (true);
		
		// Setup for packet sending
		firstInput = true;
		do {
			String serverIP;
			
			// Show input dialog
			if (firstInput) serverIP = JOptionPane.showInputDialog(null, "Enter the server IP address\nPort is automatically assigned"); 
			else serverIP = JOptionPane.showInputDialog(null, "That is an invalid address! Try again!\nEnter the server IP address\nPort is automatically assigned");

			// Exit if used clicked cancel or red X
			if (serverIP == null) System.exit(0);
			
			// If the person added a colon (for port), remove it
			if (serverIP.indexOf(":") != -1)
				serverIP = serverIP.substring(0, serverIP.indexOf(":"));
			
			// Check if entered IP is valid. If so, keep it
			else if (isIP(serverIP)) {
				RemoteMonitor.serverIP = InetAddress.getByName(serverIP);
				break;
			} else firstInput = false;
		} while (true);
		
		// Send authentication packet to server
		AuthPacket.sendAuthPacket(key, serverIP);
//		ExecutorService executor = Executors.newSingleThreadExecutor();
//		executor.submit(task)
	}
	
	public static boolean isIP(String ip) {
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

}
