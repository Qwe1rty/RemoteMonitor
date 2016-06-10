package gui;

import java.awt.Color;
import java.awt.Component;
import java.net.InetAddress;

import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;

/**
 * Custom renderer to display connected clients in a list 
 * @author Caleb Choi
 */
public class ClientListRenderer implements ListCellRenderer<InetAddress> {
	@Override
	public Component getListCellRendererComponent(JList<? extends InetAddress> list, InetAddress client, int index,
			boolean isSelected, boolean cellHasFocus) {
		
		JTextArea textArea = new JTextArea(client.getHostName() + "\n" + client.getHostAddress());
		if (isSelected) textArea.setBackground(Color.LIGHT_GRAY);
		else textArea.setBackground(list.getBackground());
		return textArea;
	}
}