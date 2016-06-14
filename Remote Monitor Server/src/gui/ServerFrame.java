package gui;

import java.awt.image.BufferedImage;
import java.net.UnknownHostException;

import javax.swing.JFrame;

/**
 * Creates a new GUI JFrame for the program
 * @author Caleb Choi
 */
public class ServerFrame extends JFrame {

	private ServerPanel mainPanel;
	
	protected static final int WINDOW_HEIGHT = 700;
	protected static final int WINDOW_WIDTH = 1400;
	protected static final int OFFSET = 46;

	public ServerFrame(String title) throws UnknownHostException {
		super(title);
		mainPanel = new ServerPanel();
		getContentPane().add(mainPanel);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(100, 50);
		
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT + OFFSET);
		setResizable(false);
		setVisible(true);
	}
	
	/**
	 * Updates the client list
	 */
	public void updateList() {mainPanel.updateList();}
	
	/**
	 * Resets the side panel to a specified image
	 * @param img Image to be displayed
	 */
	public void resetPictureArea(BufferedImage img) {mainPanel.resetPictureArea(img);}
	
	/**
	 * Gets the side panel to add the text onto the text field if that exists
	 * @param key Text to add onto the text area
	 */
	public void addText(String key) {mainPanel.addText(key);}
}
