package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class SidePanel extends JPanel {

	public SidePanel(String[] log) {
//		setLayout(null);
		
		JTextArea text = new JTextArea();
		text.setEditable(false);
		text.setBounds(0, 0, (ServerFrame.WINDOW_WIDTH - 15) - (ServerPanel.LIST_WIDTH + 30), 
				ServerFrame.WINDOW_HEIGHT - 30);
		JScrollPane scroll = new JScrollPane(text);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scroll);
	}
	
//	public SidePanel(final BufferedImage img) {
//		setLayout(null);
//		JPanel canvas = new JPanel() {
//			protected void paintComponent(Graphics g) {
//				super.paintComponent(g);
//				g.drawImage(img, 0, 0, null);
//			}
//		};
//		canvas.setBounds(0, 0, (ServerFrame.WINDOW_WIDTH - 15) - (ServerPanel.LIST_WIDTH + 30), 
//				ServerFrame.WINDOW_HEIGHT - 30);
//		JScrollPane spane = new JScrollPane(canvas);
////		spane.setBounds(0, 0, (ServerFrame.WINDOW_WIDTH - 15) - (ServerPanel.LIST_WIDTH + 30), ServerFrame.WINDOW_HEIGHT - 30);
//		add(spane);
//	}
	
//	public void paintComponent(Graphics g) {
//		super.paintComponent(g);
//		g.setColor(Color.BLACK);
//		g.drawLine(14, 14, 15 + ServerPanel.LIST_WIDTH, 14);
//		g.drawLine(14, 14, 14, ServerFrame.WINDOW_HEIGHT);
//		g.drawLine(15 + ServerPanel.LIST_WIDTH, 14, 15 + ServerPanel.LIST_WIDTH, ServerFrame.WINDOW_HEIGHT);
//		g.drawLine(14, ServerFrame.WINDOW_HEIGHT, 15 + ServerPanel.LIST_WIDTH, ServerFrame.WINDOW_HEIGHT);
//	}
	
}
