package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JList;
import javax.swing.JPanel;

public class ServerPanel extends JPanel {
	
	public JList clientList;
	public SidePanel sidePanel;
	
	protected static final int LIST_WIDTH = 300;

	public ServerPanel() {
		
		setPreferredSize(new Dimension(ServerFrame.WINDOW_WIDTH, ServerFrame.WINDOW_HEIGHT));
		setLayout(null);
		
		clientList = new JList();
		sidePanel = new SidePanel(new String[] {"Run the client stub on a target computer to connect!", 
				"Once connected, right click on any entry in the list to the left to execute various functions"});
		
		clientList.setBounds(15, 15, LIST_WIDTH, ServerFrame.WINDOW_HEIGHT - 15);
		sidePanel.setBounds(LIST_WIDTH + 30, 15, ServerFrame.WINDOW_WIDTH - (45 + LIST_WIDTH), ServerFrame.WINDOW_HEIGHT - 15);
		
		add(clientList);
		add(sidePanel);
	}
	
	public void setSidePanel(String[] s) {
		sidePanel = new SidePanel(s);
		sidePanel.setBounds(LIST_WIDTH + 30, 15, ServerFrame.WINDOW_WIDTH - (45 + LIST_WIDTH), ServerFrame.WINDOW_HEIGHT - 15);
		add(sidePanel);
		
		revalidate();
		repaint();
		System.out.println("TEEEESTs");
	}
	public void setSidePanel(BufferedImage i) {
		sidePanel = new SidePanel(i);
		sidePanel.setBounds(LIST_WIDTH + 30, 15, ServerFrame.WINDOW_WIDTH - (45 + LIST_WIDTH), ServerFrame.WINDOW_HEIGHT - 15);
		add(sidePanel);
		
		revalidate();
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.GRAY);
		g.drawLine(14, 14, 15 + LIST_WIDTH, 14);
		g.drawLine(14, 14, 14, ServerFrame.WINDOW_HEIGHT);
		g.drawLine(15 + LIST_WIDTH, 14, 15 + LIST_WIDTH, ServerFrame.WINDOW_HEIGHT);
		g.drawLine(14, ServerFrame.WINDOW_HEIGHT, 15 + LIST_WIDTH, ServerFrame.WINDOW_HEIGHT);
	}
}