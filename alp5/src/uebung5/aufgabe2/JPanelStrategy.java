package uebung5.aufgabe2;

import java.io.InputStream;

import javax.swing.JPanel;

public interface JPanelStrategy {
	JPanel getJPanelForContent(InputStream inputStream);
}
