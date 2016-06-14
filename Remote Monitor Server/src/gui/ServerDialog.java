package gui;

import java.net.InetAddress;

import javax.swing.JOptionPane;

public class ServerDialog {

	/**
	 * Displays a dialog to the user informing them that this program should only
	 * be used with the client computer owner's consent
	 * @return An integer representing the user's input response
	 */
	public static int showUsageWarningDialog() {
		return JOptionPane.showConfirmDialog(null, 
				"This program will have the ability to record and save keyboard and screen from another computer."
						+ System.getProperty("line.separator")
						+ "Misuse of this program can be considered a federal crime and can lead to severe punishment."
						+ System.getProperty("line.separator")
						+ "Only use this program with the expressed consent of the client computer's owner(s)."
						+ System.getProperty("line.separator")
						+ System.getProperty("line.separator")
						+ "Are you sure you would like to proceed?",
						"Program usage warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
	}
	
	/**
	 * Displays a dialog telling the user to enter an authentication key 
	 * @param isFirstTime If it's the first time, it won't say "Invalid input"
	 * @return The user's input key
	 */
	public static String showKeyDialog(boolean isFirstTime) {
		if (isFirstTime) return JOptionPane.showInputDialog(null, 
				"Enter an authentication key\nIt must be 8 characters or longer");
		else return JOptionPane.showInputDialog(null, 
				"That is an invalid key! Try again\nEnter an authentication key\nIt must be 8 characters or longer");
	}
	
	/**
	 * Displays a dialog informing the user that the hash generation for the
	 * authentication key has failed
	 */
	public static void showHashGenerationFailureDialog() {
		JOptionPane.showMessageDialog(null,
				"Program was unable to generate hash from entered key"
						+ System.getProperty("line.separator")
						+ "Remote monitor server will now terminate"
						, "Program error", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Displays a dialog to the user that a certain connection has been lost
	 * @param connection IP address of the disconnected client
	 */
	public static void showConnectionCutDialog(InetAddress connection) {
		JOptionPane.showMessageDialog(null, 
				"The connection with " + connection.getHostAddress() 
				+ " (" + connection.getHostName() + ") has been lost."
				+ System.getProperty("line.separator")
				+ "This client will be removed from the server."
				, "Connection lost", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Displays a dialog notifying the user that this process cannot be undone 
	 * and confirms that the user actually wants to do this
	 * @return Integer representing the number of clients disconnected
	 */
	public static int showClearAllDialog() {
		return JOptionPane.showConfirmDialog(null,
				"This process cannot be undone!"
				+ System.getProperty("line.separator")
				+ "Are you should you would like to continue?"
				, "Confirm action", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
	}
	
	/**
	 * Displays a dialog notifying the user that a connectivity error occured
	 * within the server socket
	 */
	public static void showServerSocketErrorDialog() {
		JOptionPane.showMessageDialog(null, 
				"An unexpected connectivity error occured in the server socket"
						+ System.getProperty("line.separator")
						+ "Program will now exit. Please restart the program to try again", 
						"Unexpected error", JOptionPane.ERROR_MESSAGE);
	}
	
}
