package gui;

import javax.swing.JFrame;

public class ServerFrame extends JFrame {

	public static ServerPanel mainPanel;
	
	protected static final int WINDOW_HEIGHT = 700;
	protected static final int WINDOW_WIDTH = 1200;

	public ServerFrame(String title) {
		super(title);
		mainPanel = new ServerPanel();
		getContentPane().add(new SidePanel(new String[0]));
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(100, 50);
		
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT + 46);
		setResizable(false);
		setVisible(true);
	}
}
