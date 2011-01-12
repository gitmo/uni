package uebung5.aufgabe2;

import java.io.InputStream;

import javax.swing.JPanel;

public interface JPanelStrategy {
	boolean warnBeforeDispatch();
	JPanel getJPanelForContent(InputStream inputStream);
}
