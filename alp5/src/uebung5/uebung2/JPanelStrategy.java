package uebung5.uebung2;

import java.io.InputStream;

import javax.swing.JPanel;

public interface JPanelStrategy {
	JPanel getJPanelForContent(InputStream inputStream);
}
