package uebung5.aufgabe2.strategies;

import java.io.InputStream;

import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import uebung5.aufgabe2.JPanelStrategy;

public class HtmlStrategy implements JPanelStrategy {

	@Override
	public JPanel getJPanelForContent(InputStream inputStream) {
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		String htmlString = StreamUtil.getStreamAsString(inputStream);
		
		JEditorPane htmlPane = new JEditorPane("text/html", htmlString);
		htmlPane.setEditable(false);
		panel.add(new JScrollPane(htmlPane));
		
		return panel;
	}

	@Override
	public boolean warnBeforeDispatch() {
		return false;
	}

}
