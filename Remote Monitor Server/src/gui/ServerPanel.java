package gui;

import head.RemoteMonitorServer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;

public class ServerPanel extends JPanel {
	
	private DefaultListModel<InetAddress> listModel;
	private JList<InetAddress> clientList;
	private JScrollPane clientListScroll;
	private SidePanel sidePanel;
	
	protected static final int LIST_WIDTH = 300;

	public ServerPanel() throws UnknownHostException {
		
		// Setup panel settings
		setPreferredSize(new Dimension(ServerFrame.WINDOW_WIDTH, ServerFrame.WINDOW_HEIGHT));
		setLayout(null);
		
		// Creates list components and settings
		listModel = new DefaultListModel<InetAddress>();
		clientList = new JList<InetAddress>(listModel);
		clientList.setCellRenderer(new InetAddressRenderer());
		clientListScroll = new JScrollPane(clientList);
		listModel.addElement(InetAddress.getLocalHost());
		
		// 
		sidePanel = new SidePanel(new String[] {"Run the client stub on a target computer to connect!", 
				"Once connected, right click on any entry in the list to the left to execute various functions",
				"",
				"Your internal server IP is " + InetAddress.getLocalHost().getHostAddress() + ", with hostname " + InetAddress.getLocalHost().getHostName(),});
		
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
	
	/**
	 * Custom renderer to display connected clients in a list 
	 * @author Caleb Choi
	 */
	private class InetAddressRenderer implements ListCellRenderer<InetAddress> {
		public InetAddressRenderer() {setOpaque(true);}
		
		@Override
		public Component getListCellRendererComponent(JList<? extends InetAddress> list, InetAddress client, int index,
				boolean isSelected, boolean cellHasFocus) {
			
			JTextArea textArea = new JTextArea(client.getHostName() + "\n" + client.getHostAddress());
			if (isSelected) {
				textArea.setBackground(Color.LIGHT_GRAY);
				textArea.setForeground(list.getForeground());
			} else {
				textArea.setBackground(list.getBackground());
				textArea.setForeground(list.getForeground());
			}
			return textArea;
		}
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
