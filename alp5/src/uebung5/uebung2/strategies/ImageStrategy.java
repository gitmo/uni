package uebung5.uebung2.strategies;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import uebung5.uebung2.*;

public class ImageStrategy implements JPanelStrategy{

	@Override
	public JPanel getJPanelForContent(InputStream inputStream) {
		JPanel panel = new JPanel();
		JLabel label = null;
		Image image = null;
		
		try {
			// Read from an input stream
		    InputStream is = new BufferedInputStream(inputStream);
		    image = ImageIO.read(is);


			// Use a label to display the image
			label = new JLabel(new ImageIcon(image));
		} catch (IOException e) {
			label = new JLabel("Invalid graphic file!");
		}
		
		panel.add(new JScrollPane(label));
		
		return panel;
	}

}
