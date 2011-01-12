package uebung5.aufgabe2;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

import uebung5.aufgabe2.strategies.ImageStrategy;
import uebung5.aufgabe2.strategies.TextStrategy;

public class SimpleBrowser {

	Map<String, Map<String, Class<? extends JPanelStrategy>>> supportedContentTypes = new HashMap<String, Map<String, Class<? extends JPanelStrategy>>>();

	public SimpleBrowser() {
		Map<String, Class<? extends JPanelStrategy>> imageTypes = new HashMap<String, Class<? extends JPanelStrategy>>();
		imageTypes.put("jpeg", ImageStrategy.class);

		supportedContentTypes.put("image", imageTypes);

		Map<String, Class<? extends JPanelStrategy>> textTypes = new HashMap<String, Class<? extends JPanelStrategy>>();
		textTypes.put("plain", TextStrategy.class);
		textTypes.put("html", TextStrategy.class);

		supportedContentTypes.put("text", textTypes);
	}

	JPanel dispatchUrl(String urlString) {
		JPanel panel = new JPanel();
		URL url = null;
		URLConnection connection = null;

		try {
			url = new URL(urlString);
			connection = url.openConnection();
			connection.connect();
		} catch (IOException e) {
			panel.add(new JLabel("could no connect to " + urlString));
			return panel;
		}

		String contentType = connection.getHeaderField("Content-Type");

		int separatorIndex = contentType.indexOf("/");
		if (separatorIndex == -1) {
			System.err.println("invalind content type separator");
			panel.add(new JLabel("invalid http header (content type: "
					+ contentType + ")"));
			return panel;
		}

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

		
		String contentElementStr = contentType.substring(separatorIndex + 1)
		.trim();
		separatorIndex = contentElementStr.indexOf(";");
		if(separatorIndex != -1)
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
			panel = strategy.getJPanelForContent(connection.getInputStream());
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return panel;

	}
}
