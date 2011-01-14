package uebung5.aufgabe2;

import java.io.InputStream;

import javax.swing.JPanel;

/**
 * Strategy Behaviour Pattern ermöglicht einfaches austauschen der
 * dahinterliegenden Algorithmen
 */
public interface JPanelStrategy {
	// Soll eine Warnung vor der Abarbeitung ausgegeben werden?
	boolean warnBeforeDispatch();

	// Implementation zur Abarbeitung der Daten
	JPanel getJPanelForContent(InputStream inputStream);
}
