import java.net.InetAddress;

import javax.swing.JOptionPane;

/**
 * Contains all the JOptionPanes that are used within the program 
 * @author Caleb Choi
 */
public class ClientDialog {

	/**
	 * Show a dialog informing the user that this program should only be used responsibly
	 * @return The user's response
	 */
	public static int showConsentResponseDialog() {
		return JOptionPane.showConfirmDialog(null, 
				"Are you sure you would like to allow this program to send your keyboard input and screen"
						+ System.getProperty("line.separator")
						+ "feed to another computer? You will not be able to terminate the program via normal means."
						+ System.getProperty("line.separator")
						+ System.getProperty("line.separator")
						+ "If you are unsure whether to proceed, DO NOT continue.",
						"Program consent", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
	}
	
	/**
	 * Shows a dialog telling the user to enter an authentication key 
	 * @param isFirstTime If it's the first time, it won't say "Invalid input"
	 * @return The user's input key
	 */
	public static String showKeyDialog(boolean isFirstTime) {
		if (isFirstTime) return JOptionPane.showInputDialog(null, 
				"Enter an authentication key\nIt must be 8 characters or longer",
				"Program Input", JOptionPane.QUESTION_MESSAGE);
		else return JOptionPane.showInputDialog(null, 
				"That is an invalid key! Try again\nEnter an authentication key\nIt must be 8 characters or longer",
				"Program Input", JOptionPane.QUESTION_MESSAGE);
	}
	
	/**
	 * Shows a dialog telling the user to enter the serer's IP address
	 * @param isFirstTime If it's the time time, it won't say "Invalid input"
	 * @return The user's input key
	 */
	public static String showServerIPDialog(boolean isFirstTime) {
		if (isFirstTime) return JOptionPane.showInputDialog(null, 
					"Enter the server IP address\nPort is automatically assigned",
					"Program Input", JOptionPane.QUESTION_MESSAGE);
		else return JOptionPane.showInputDialog(null, 
				"That is an invalid address! Try again!\nEnter the server IP address\nPort will be automatically assigned",
				"Program Input", JOptionPane.QUESTION_MESSAGE);
	}
	
	/**
	 * Show the information dialog when the server terminates the client's connection
	 */
	public static void showTerminationDialog() { // exit code 0
		JOptionPane.showMessageDialog(null, 
				"Remote monitor client connection has been successfully terminated by the server " + RemoteMonitorClient.getServerIP().getHostAddress() + "."
						+ System.getProperty("line.separator")
						+ System.getProperty("line.separator")
						+ "If you are unsure why you are seeing this message, please contact your system administrator"
						+ System.getProperty("line.separator")
						+ "immediately. There may have been unauthorized access to your computer and your personal data "
						+ System.getProperty("line.separator")
						+ "has potentially been stolen."
						, "Connection Terminated", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Shows the error dialog when the client is unable to authenticate with the server
	 */
	public static void showUnauthenticatedDialog() { // exit code 1
		JOptionPane.showMessageDialog(null, 
				"Unable to authenticate with server. Program will now terminate" 
				, "Authentication unsuccessful", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Show the error dialog when the client has been disconnected from the server unexpectedly
	 */
	public static void showDisconnectionDialog() { // exit code 2
		JOptionPane.showMessageDialog(null, 
				"Remote monitor connection has been unexpectedly disconnected from the server " + RemoteMonitorClient.getServerIP().getHostAddress() + "."
						+ System.getProperty("line.separator")
						+ System.getProperty("line.separator")
						+ "If you are unsure why you are seeing this message, please contact your system administrator"
						+ System.getProperty("line.separator")
						+ "immediately. There may have been unauthorized access to your computer and your personal data "
						+ System.getProperty("line.separator")
						+ "has potentially been stolen."
						, "Connection Terminated", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Show the error dialog when the client has been disconnected from the server unexpectedly
	 */
	public static void showNativeHookExceptionDialog() { // exit code 3
		JOptionPane.showMessageDialog(null, 
				"Native key hook was unsuccessful and will now disconnect from the server " + RemoteMonitorClient.getServerIP().getHostAddress() + "."
						+ System.getProperty("line.separator")
						+ System.getProperty("line.separator")
						+ "If you are unsure why you are seeing this message, please contact your system administrator"
						+ System.getProperty("line.separator")
						+ "immediately. There may have been unauthorized access to your computer and your personal data "
						+ System.getProperty("line.separator")
						+ "has potentially been stolen."
						, "Connection Terminated", JOptionPane.ERROR_MESSAGE);
	}
	
}
