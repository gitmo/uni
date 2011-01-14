package uebung5.aufgabe2.strategies;

import java.io.InputStream;

import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import uebung5.aufgabe2.JPanelStrategy;

/**
 * Ermöglicht das rudimentare Darstellen von HTML-Daten Achtung: Website mit
 * HTTP-Redirect werden nicht unterstützt
 */
public class HtmlStrategy implements JPanelStrategy {

	public JPanel getJPanelForContent(InputStream inputStream) {

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		String htmlString = StreamUtil.getStreamAsString(inputStream);

		JEditorPane htmlPane = new JEditorPane("text/html", htmlString);
		htmlPane.setEditable(false);
		panel.add(new JScrollPane(htmlPane));

		return panel;
	}

	public boolean warnBeforeDispatch() {
		return false;
	}

}
