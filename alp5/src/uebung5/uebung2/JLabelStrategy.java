package uebung5.uebung2;

import java.io.InputStream;

import javax.swing.JLabel;

public interface JLabelStrategy {
	JLabel getJLabelForContent(InputStream inputStream);
}
