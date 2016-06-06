import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class RemoteMonitor {

	private static int port = 60922;
	private static String key;

	public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException, UnknownHostException, SocketException {

		// Greatest line of code of all time 
		try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());} catch (Exception e) {}

		// Prompts user to enter a key and generates hash
		boolean firstInput = true;
		do {
			String seed;
			if (firstInput) seed = JOptionPane.showInputDialog(null, "Enter an authentication key\nIt must be 8 characters or longer"); 
			else seed = JOptionPane.showInputDialog(null, "That is an invalid key! Try again\nEnter an authentication key\nIt must be 8 characters or longer");

			if (seed == null) System.exit(0);
			else if (seed.length() >= 8) {
				key = generateHash(seed);
				break;
			} else firstInput = false;
		} while (true);
		
		// Setup for packet sending
	
	}
	
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
