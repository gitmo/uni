package uebung5.uebung2;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;

import uebung5.uebung2.strategies.ImageStrategy;

public class SimpleBrowser {

	Map<String, Map<String, Class<? extends JLabelStrategy>>> supportedContentTypes = new HashMap<String, Map<String, Class<? extends JLabelStrategy>>>();
	
	public SimpleBrowser() {
		Map<String, Class<? extends JLabelStrategy>> imageTypes = new HashMap<String, Class<? extends JLabelStrategy>>();
		imageTypes.put("jpeg", ImageStrategy.class);
		
		supportedContentTypes.put("image", imageTypes);
	}
	
	JLabel dispatchUrl(String urlString) {
		URL url = null;
		URLConnection connection = null;
		
		try {
			url = new URL(urlString);
			connection = url.openConnection();
			connection.connect();
		} catch (IOException e) {
			return new JLabel("could no connect to " + urlString);
		}
		
		String contentType = connection.getHeaderField("Content-Type");
		
		int separatorIndex = contentType.indexOf("/");
		if(separatorIndex == -1) {
			System.err.println("invalind content type separator");
			return new JLabel("invalid http header (content type)");
		}
		
		String contentGroupStr = contentType.substring(0, separatorIndex).trim();
		String contentElementStr = contentType.substring(separatorIndex+1).trim();
		if(!supportedContentTypes.containsKey(contentGroupStr)) {
			System.err.println("unsupported content type (group)");
			return new JLabel("unsupported content");
		}
		
		Map<String, Class<? extends JLabelStrategy>> contentGroup = supportedContentTypes.get(contentGroupStr);
		
		Class<? extends JLabelStrategy> strategyClass = contentGroup.get(contentElementStr);
		if(strategyClass == null) {
			System.err.println("unsupported content type (element)");
			return new JLabel("unsupported content");
		}
		
		JLabelStrategy strategy = null;
		JLabel label = null;
		try {
			strategy = strategyClass.newInstance();
			label = strategy.getJLabelForContent(connection.getInputStream());
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
		
		return label;

	}
}
