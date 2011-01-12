package uebung5.uebung2.strategies;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import uebung5.uebung2.*;

public class ImageStrategy implements JLabelStrategy{

	@Override
	public JLabel getJLabelForContent(InputStream inputStream) {
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
		
		return label;
	}

}
