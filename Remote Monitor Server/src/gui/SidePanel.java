package gui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;

public class SidePanel extends JPanel {
	
	private JTextArea text;
	private JPanel canvas;

	public SidePanel(String[] log) {
		setLayout(null);
//		setLayout(new BorderLayout());
//		setBorder(new EtchedBorder());
		
		text = new JTextArea();
		text.setEditable(false);
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		
		for (int i = 0; i < log.length; i++)
			text.append(log[i] + "\n");
		
		JScrollPane scroll = new JScrollPane(text);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		scroll.setBounds(0, 0, ServerFrame.WINDOW_WIDTH - (55 + ServerPanel.LIST_WIDTH),
				ServerFrame.WINDOW_HEIGHT - (15 + ServerPanel.BUTTON_HEIGHT));
		add(scroll);
	}
	
	public SidePanel(final BufferedImage img) {
		setLayout(null);
//		setLayout(new BorderLayout());
//		setBorder(new EtchedBorder());
		
		canvas = new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(img, 0, 0, null);
			}
		};
		JScrollPane scroll = new JScrollPane(canvas);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		scroll.setBounds(0, 0, ServerFrame.WINDOW_WIDTH - (55 + ServerPanel.LIST_WIDTH),
				ServerFrame.WINDOW_HEIGHT - (15 + ServerPanel.BUTTON_HEIGHT));
		add(scroll);
	}
	
	/**
	 * Adds the provided string onto the text area, if it exists
	 * @param key Text to add onto the text area
	 */
	public void addText(String key) {if (text != null) text.append(key);}
	
//	public void paintComponent(Graphics g) {
//		super.paintComponent(g);
//		g.setColor(Color.BLACK);
//		g.drawLine(14, 14, 15 + ServerPanel.LIST_WIDTH, 14);
//		g.drawLine(14, 14, 14, ServerFrame.WINDOW_HEIGHT);
//		g.drawLine(15 + ServerPanel.LIST_WIDTH, 14, 15 + ServerPanel.LIST_WIDTH, ServerFrame.WINDOW_HEIGHT);
//		g.drawLine(14, ServerFrame.WINDOW_HEIGHT, 15 + ServerPanel.LIST_WIDTH, ServerFrame.WINDOW_HEIGHT);
//	}
	
}
