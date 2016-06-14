package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	private JScrollPane canvasScroll;
	private BufferedImage image;
	private JButton size;
	private boolean fullSize;

	/**
	 * Displays a text field where key logs can be written in
	 * @param log Default text to be displayed
	 */
	public SidePanel(String[] log) {
		setLayout(null);

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
		image = img;

		// JPanel where the image is drawn settings
		System.out.println("IMAGE: " + image.getWidth() + "x" + image.getHeight());
		canvas = new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(image, 0, 0, null);
				g.dispose();
			}
		};
		// This ensures that the panel is larger than the scroll pane, so that it'll scroll
		canvas.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));

		// Button to switch between full size and fit size images
		size = new JButton("Adjust image to fit");
		size.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Switching image size");
				switchImageSize();
			}
		});
		size.setBounds(0, ServerFrame.WINDOW_HEIGHT - (15 + ServerPanel.BUTTON_HEIGHT),
				ServerFrame.WINDOW_WIDTH - (55 + ServerPanel.LIST_WIDTH), ServerPanel.BUTTON_HEIGHT);
		fullSize = true;
		add(size);

		// Scroll pane stuffs
		canvasScroll = new JScrollPane(canvas);
		canvasScroll.getVerticalScrollBar().setUnitIncrement(16);
		canvasScroll.getHorizontalScrollBar().setUnitIncrement(48);
		canvasScroll.setBounds(0, 0, ServerFrame.WINDOW_WIDTH - (55 + ServerPanel.LIST_WIDTH),
				//				ServerFrame.WINDOW_HEIGHT - 15);
				ServerFrame.WINDOW_HEIGHT - (15 + ServerPanel.BUTTON_HEIGHT));
		add(canvasScroll);
	}

	/**
	 * Switches the image to either fit, or be full size depending on
	 * whatever it was before
	 */
	private void switchImageSize() {
		if (canvas == null) return;

		// Calculate the canvas' aspect ratio
		final double aspectRatio = ServerFrame.WINDOW_WIDTH - (55 + ServerPanel.LIST_WIDTH)
				/ (ServerFrame.WINDOW_HEIGHT - (15 + ServerPanel.BUTTON_HEIGHT) + 0.000);

		// If the image being displayed is already full/real size
		if (fullSize) {

			// Changes button text
			size.setText("Revert image to full/real size");

			// Calculate the image's size
			final double imageRatio = image.getWidth() / (image.getHeight() + 0.000); 
			final int newWidth, newHeight;

			// If the image's ratio is wider than high compared to panel's ratio
			if (imageRatio < aspectRatio) {

				// Define new width and height
				newWidth = ServerFrame.WINDOW_WIDTH - (55 + ServerPanel.LIST_WIDTH);
				newHeight = (int) ((ServerFrame.WINDOW_WIDTH - (55 + ServerPanel.LIST_WIDTH)) * (1 / imageRatio));

			} else { // If the image ratio is higher than wide compared to panel's ratio

				// Define new width and height
				newWidth = (int) ((ServerFrame.WINDOW_HEIGHT - (15 + ServerPanel.BUTTON_HEIGHT)) * imageRatio);
				newHeight = ServerFrame.WINDOW_HEIGHT - (15 + ServerPanel.BUTTON_HEIGHT);
			}

			// Redefines the canvas JPanel entirely
			canvas = new JPanel() {
				protected void paintComponent(Graphics g) {

					// Copy image and resize it
					final BufferedImage resized = new BufferedImage(
							newWidth,
							newHeight,
							image.getType());

					// Draw the resized image
					super.paintComponent(g);
					Graphics2D g2 = (Graphics2D) g;
					g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
							RenderingHints.VALUE_INTERPOLATION_BILINEAR);
					
					// Redraws the image, and centers it
					if (imageRatio < aspectRatio)
						g2.drawImage(image, 0, 
								((ServerFrame.WINDOW_HEIGHT - (15 + ServerPanel.BUTTON_HEIGHT)) - newHeight) / 2, newWidth, 
								(((ServerFrame.WINDOW_HEIGHT - (15 + ServerPanel.BUTTON_HEIGHT)) - newHeight) / 2) + newHeight, 
								0, 0, image.getWidth(), image.getHeight(), null);
					else
						g2.drawImage(image, 
								(ServerFrame.WINDOW_WIDTH - (55 + ServerPanel.LIST_WIDTH) - newWidth) / 2,
								0, ((ServerFrame.WINDOW_WIDTH - (55 + ServerPanel.LIST_WIDTH) - newWidth) / 2) + newWidth, 
								newHeight, 0, 0, image.getWidth(), image.getHeight(), null);
				}
			};

			// Replaces scrolling panel and replaces it with raw canvas
			remove(canvasScroll);
			canvas.setBounds(0, 0, ServerFrame.WINDOW_WIDTH - (55 + ServerPanel.LIST_WIDTH),
					ServerFrame.WINDOW_HEIGHT - (15 + ServerPanel.BUTTON_HEIGHT));
			add(canvas);

			// Change boolean flag
			fullSize = false;

		} else { // If image being displayed is the resized one

			// Removes the raw canvas and readds the scroll pane
			remove(canvas);
			canvas = new JPanel() {
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(image, 0, 0, null);
					g.dispose();
				}
			};
			// This ensures that the panel is larger than the scroll pane, so that it'll scroll
			canvas.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));

			// Redefine the scoll pane
			canvasScroll = new JScrollPane(canvas);
			canvasScroll.getVerticalScrollBar().setUnitIncrement(16);
			canvasScroll.getHorizontalScrollBar().setUnitIncrement(48);
			canvasScroll.setBounds(0, 0, ServerFrame.WINDOW_WIDTH - (55 + ServerPanel.LIST_WIDTH),
					//				ServerFrame.WINDOW_HEIGHT - 15);
					ServerFrame.WINDOW_HEIGHT - (15 + ServerPanel.BUTTON_HEIGHT));
			add(canvasScroll);

			// Changes button text
			size.setText("Adjust image to fit");

			// Change boolean flag
			fullSize = true;
		}

		// Obligatory
		revalidate();
		repaint();
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
