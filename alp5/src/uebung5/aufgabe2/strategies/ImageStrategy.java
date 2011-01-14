package uebung5.aufgabe2.strategies;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import uebung5.aufgabe2.*;

/**
 * Ermöglicht das Darstellen von Bildern
 */
public class ImageStrategy implements JPanelStrategy {

	public JPanel getJPanelForContent(InputStream inputStream) {
		JPanel panel = new JPanel();
		JLabel label = null;
		Image image = null;

		try {
			// Bild laden
			InputStream is = new BufferedInputStream(inputStream);
			image = ImageIO.read(is);

			// Bild darstellen
			label = new JLabel(new ImageIcon(image));
		} catch (IOException e) {
			label = new JLabel("Invalid graphic file!");
		}

		panel.add(new JScrollPane(label));

		return panel;
	}

	public boolean warnBeforeDispatch() {
		return false;
	}

}
