package uebung5.uebung2;

import javax.swing.SwingUtilities;

public class Main {
	/**
	 * Launches this application
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Gui application = new Gui();
				application.getSimpleBrowser().setVisible(true);
			}
		});
	}
}
