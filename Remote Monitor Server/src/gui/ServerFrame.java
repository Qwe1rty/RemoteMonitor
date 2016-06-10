package gui;

import java.net.UnknownHostException;

import javax.swing.JFrame;

public class ServerFrame extends JFrame {

	private ServerPanel mainPanel;
	
	protected static final int WINDOW_HEIGHT = 700;
	protected static final int WINDOW_WIDTH = 1200;
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
	
	public void updateList() {mainPanel.updateList();}
}
