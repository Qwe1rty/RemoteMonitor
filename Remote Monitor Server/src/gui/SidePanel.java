package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.DefaultCaret;

public class SidePanel extends JPanel {

	private JTextArea text;
	private JPanel canvas;
	private JButton size;

	/**
	 * Displays a text field where key logs can be written in
	 * @param log Default text to be displayed
	 */
	public SidePanel(String[] log) {
		setLayout(null);
		//		setLayout(new BorderLayout());
		//		setBorder(new EtchedBorder());

		text = new JTextArea();
		text.setEditable(false);
		text.setLineWrap(true);
		text.setWrapStyleWord(true);

		// Auto scrolling
		DefaultCaret caret = (DefaultCaret) text.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		for (int i = 0; i < log.length; i++)
			text.append(log[i] + "\n");

		// Scroll pane declaration and settings
		JScrollPane scroll = new JScrollPane(text);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBounds(0, 0, ServerFrame.WINDOW_WIDTH - (55 + ServerPanel.LIST_WIDTH),
				ServerFrame.WINDOW_HEIGHT - 15);
		//				ServerFrame.WINDOW_HEIGHT - (15 + ServerPanel.BUTTON_HEIGHT));
		add(scroll);
	}

	/**
	 * Displays a scrollable image in the entirety of the panel
	 * @param img Image to be displayed
	 */
	public SidePanel(final BufferedImage img) {
		setLayout(null);
		//		setLayout(new BorderLayout());
		//		setBorder(new EtchedBorder());

		// JPanel where the image is drawn settings
		System.out.println("IMAGE: " + img.getWidth() + "x" + img.getHeight());
		canvas = new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(img, 0, 0, null);
			}
		};
		// This ensures that the panel is larger than the scroll pane, so that it'll scroll
		canvas.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));

		// Scroll pane stuffs
		JScrollPane scroll = new JScrollPane(canvas);
		scroll.getVerticalScrollBar().setUnitIncrement(16);
		scroll.getHorizontalScrollBar().setUnitIncrement(48);
		scroll.setBounds(0, 0, ServerFrame.WINDOW_WIDTH - (55 + ServerPanel.LIST_WIDTH),
				ServerFrame.WINDOW_HEIGHT - 15);
		//				ServerFrame.WINDOW_HEIGHT - (15 + ServerPanel.BUTTON_HEIGHT));
		add(scroll);
	}

	/**
	 * Adds the provided string onto the text area, if it exists
	 * @param key Text to add onto the text area
	 */
	public void addText(String key) {
		if (text != null && key != null)
			if (key.equals("[ENTER]") || key.equals("[KP_ENTER]"))
				text.append(key + System.getProperty("line.separator"));
			else text.append(key);
	}

	//	public void paintComponent(Graphics g) {
	//		super.paintComponent(g);
	//		g.setColor(Color.BLACK);
	//		g.drawLine(14, 14, 15 + ServerPanel.LIST_WIDTH, 14);
	//		g.drawLine(14, 14, 14, ServerFrame.WINDOW_HEIGHT);
	//		g.drawLine(15 + ServerPanel.LIST_WIDTH, 14, 15 + ServerPanel.LIST_WIDTH, ServerFrame.WINDOW_HEIGHT);
	//		g.drawLine(14, ServerFrame.WINDOW_HEIGHT, 15 + ServerPanel.LIST_WIDTH, ServerFrame.WINDOW_HEIGHT);
	//	}

}
