package uebung5.aufgabe2;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import uebung5.aufgabe2.strategies.HtmlStrategy;
import uebung5.aufgabe2.strategies.ImageStrategy;
import uebung5.aufgabe2.strategies.ShellStrategy;
import uebung5.aufgabe2.strategies.TextStrategy;

public class SimpleBrowser {

	// Mit Hilfe der Map-Datenstruktur supportedContent kann auf einfacer Art
	// und Weise die jeweilige Klasse zur Bearbeitung des jeweiligen
	// ContentTypes ausgewählt werden.
	Map<String, Map<String, Class<? extends JPanelStrategy>>> supportedContentTypes = new HashMap<String, Map<String, Class<? extends JPanelStrategy>>>();

	public SimpleBrowser() {
		// Bilder-Unterstützung
		Map<String, Class<? extends JPanelStrategy>> imageTypes = new HashMap<String, Class<? extends JPanelStrategy>>();
		imageTypes.put("gif", ImageStrategy.class);
		imageTypes.put("jpeg", ImageStrategy.class);
		supportedContentTypes.put("image", imageTypes);

		// Text- und Html-Unterstützung
		Map<String, Class<? extends JPanelStrategy>> textTypes = new HashMap<String, Class<? extends JPanelStrategy>>();
		textTypes.put("plain", TextStrategy.class);
		textTypes.put("html", HtmlStrategy.class);
		supportedContentTypes.put("text", textTypes);

		// Shell-Skript Unterstützung
		Map<String, Class<? extends JPanelStrategy>> shTypes = new HashMap<String, Class<? extends JPanelStrategy>>();
		shTypes.put("x-sh", ShellStrategy.class);
		shTypes.put("x-shellscript", ShellStrategy.class);
		shTypes.put("x-shar", ShellStrategy.class);
		supportedContentTypes.put("application", shTypes);
	}

	/**
	 * dispathUrl stellt eine Verbindung zu einer URL her und versucht je nach
	 * Content-Type die Daten visuell darzustellen.
	 * 
	 * @param urlString
	 * @return
	 */
	JPanel dispatchUrl(String urlString) {
		JPanel panel = new JPanel();
		URL url = null;
		URLConnection connection = null;

		// Verbindungsherstellung
		try {
			url = new URL(urlString);
			connection = url.openConnection();
			connection.connect();
		} catch (IOException e) {
			panel.add(new JLabel("could no connect to " + urlString));
			return panel;
		}

		// Ermittlung des MIME-Types (ContentTypes)
		String contentType = connection.getHeaderField("Content-Type");

		// Ist der Content-Type richtig formatiert?
		int separatorIndex = contentType.indexOf("/");
		if (separatorIndex == -1) {
			System.err.println("invalind content type separator");
			panel.add(new JLabel("invalid http header (content type: "
					+ contentType + ")"));
			return panel;
		}

		// Unterstützt unser Browser die Oberklasse des ContentTypes
		String contentGroupStr = contentType.substring(0, separatorIndex)
				.trim();
		if (!supportedContentTypes.containsKey(contentGroupStr)) {
			System.err.println("unsupported content type (group: "
					+ contentGroupStr + ")");
			panel.add(new JLabel("unsupported content"));
			return panel;
		}

		Map<String, Class<? extends JPanelStrategy>> contentGroup = supportedContentTypes
				.get(contentGroupStr);

		// Unterstützt unser Browser den MIME-Type?
		String contentElementStr = contentType.substring(separatorIndex + 1)
				.trim();
		separatorIndex = contentElementStr.indexOf(";");
		if (separatorIndex != -1)
			contentElementStr = contentElementStr.substring(0, separatorIndex);

		Class<? extends JPanelStrategy> strategyClass = contentGroup
				.get(contentElementStr);
		if (strategyClass == null) {
			System.err.println("unsupported content type (element: "
					+ contentElementStr + ")");
			panel.add(new JLabel("unsupported content"));
			return panel;
		}

		JPanelStrategy strategy = null;
		try {
			strategy = strategyClass.newInstance();

			// Soll vor dem ausführen eine Warnung erscheinen?
			if (strategy.warnBeforeDispatch()) {
				int n = JOptionPane.showConfirmDialog(panel,
						"Should the bash script executed?", "Warning",
						JOptionPane.YES_NO_OPTION);

				System.out.println(n);
				if (n == 1)
					throw new Exception("do not run!");
			}

			// Führ die jeweilige Klasse zur Visulisierung der Daten aus
			panel = strategy.getJPanelForContent(connection.getInputStream());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
		}

		return panel;

	}

	/**
	 * Main Methode führt das Program aus
	 * 
	 * @param args
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
