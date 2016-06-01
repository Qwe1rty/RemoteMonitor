import gui.ServerFrame;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.UIManager;

public class RemoteMonitor {
	
	public static ServerFrame frame;

	public static void main(String[] args) throws IOException {
		
		try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());} catch (Exception e) {}
		frame = new ServerFrame("Remote Monitor");
		
//		frame.mainPanel = new ServerPanel();
		frame.mainPanel.setSidePanel(ImageIO.read(new File("Apple.jpg")));
	}

}
