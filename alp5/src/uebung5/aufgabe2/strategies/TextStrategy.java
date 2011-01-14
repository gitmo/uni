package uebung5.aufgabe2.strategies;

import java.io.InputStream;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import uebung5.aufgabe2.JPanelStrategy;

/**
 * Ermöglicht das Darstellen von Text-Dateien
 */
public class TextStrategy implements JPanelStrategy {

	public JPanel getJPanelForContent(InputStream inputStream) {
		JPanel panel = new JPanel();

		JTextArea textArea = new JTextArea(StreamUtil
				.getStreamAsString(inputStream), 50, 50);

		panel.add(new JScrollPane(textArea));

		return panel;
	}

	public boolean warnBeforeDispatch() {
		return false;
	}

}
