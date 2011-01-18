package uebung6.aufgabe1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebCrawler {
	private final String HREF_START = "<a href=";
	private final String HREF_END = "</a>";

	private List<String> supportedContentTypes = new ArrayList<String>();

	public WebCrawler() {
		supportedContentTypes.add("text/html; charset=utf-8");
	}

	private String getInputStreamAsString(InputStream stream) {
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(stream));

		StringBuilder builder = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null)
				builder.append(line.replace("\t", ""));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return builder.toString();
	}

	public List<String> getLinks(String urlString) {
		Set<String> urlSet = new TreeSet<String>();

		try {
			URL url = new URL(urlString);

			URLConnection connection = url.openConnection();

			String contentType = connection.getHeaderField("Content-Type");
			if (!this.supportedContentTypes.contains(contentType))
				throw new IOException("unsupported content type: "
						+ contentType);

			String contentAsString = this.getInputStreamAsString(connection
					.getInputStream());

			int tagStartIndex = 0, tagEndIndex;
			while ((tagStartIndex = contentAsString.indexOf(HREF_START,
					tagStartIndex)) != -1) {
				tagEndIndex = contentAsString.indexOf(HREF_END, tagStartIndex)
						+ HREF_END.length();

				if (tagEndIndex < 0)
					break;

				String tagString = contentAsString.substring(tagStartIndex,
						tagEndIndex);

				Pattern pattern = Pattern
						.compile("<a href=\"([^\"]*)\"(.*)>(.*)</a>");
				Matcher matcher = pattern.matcher(tagString);

				if (matcher.matches())
					urlSet.add(matcher.group(1));

				tagStartIndex = tagEndIndex;

			}

		} catch (MalformedURLException e) {
			System.err.println("Mal formed url");
		} catch (IOException e) {
			System.err.println("io error: " + e.getMessage());
		}

		return new LinkedList<String>(urlSet);
	}

	public static void main(String[] args) {
		for (String link : new WebCrawler()
				.getLinks("http://www.fu-berlin.de/einrichtungen"))
			System.out.println(link);
	}
}
