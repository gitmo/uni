package uebung6.aufgabe1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Harvest {
	private final String HREF_START = "<a href=";
	private final String HREF_END = "</a>";

	private List<String> supportedContentTypes = new ArrayList<String>();
	private List<String> validLinkPrefixes = new ArrayList<String>();
	HashSet<String> visitedLinks = new HashSet<String>(); //Effiziente Möglichkeit bereits besuchte Links zu speichern
	
	public Harvest() {
		supportedContentTypes.add("text/html");
		
		validLinkPrefixes.add("#");
		validLinkPrefixes.add("mailto:");
		validLinkPrefixes.add("javascript:");
		validLinkPrefixes.add("http");
	}

	/**
	 * Liest den Inhalt eines Streams aus und gibt diesen als String zurück
	 * Zeilenumbrüche und Tabulator-Zeichen werden aus Kombatibilitätsgründen nicht weitergegeben.
	 * @param stream
	 * @return
	 */
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

	/**
	 * Extrahiert alle Links einer Website und gibt diese als Liste zurück
	 * @param urlString
	 * @return
	 */
	private List<String> getLinks(String urlString) {
		Set<String> urlSet = new TreeSet<String>();

		try {
			//Stellt eine Verbinung her liest die Website in einen String ein
			URL url = new URL(urlString);

			URLConnection connection = url.openConnection();

			String contentType = connection.getHeaderField("Content-Type");
			
			if(contentType == null)
				throw new IOException("no content type given!");
			
			if(!this.supportContentType(contentType))
				throw new IOException("unsupported content type: "
						+ contentType);

			String contentAsString = this.getInputStreamAsString(connection
					.getInputStream());

			//Ermittelt wo Links im String stehen und extrahiert die Url mittels einer Regex 
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
					urlSet.add(this.addBaseUrl(urlString, matcher.group(1)));
				
				tagStartIndex = tagEndIndex;

			}

		} catch (MalformedURLException e) {
			System.err.println("Mal formed url: " + urlString);
		} catch (IOException e) {
			System.err.println("io error: " + e.getMessage());
		}

		return new LinkedList<String>(urlSet);
	}
	
	/**
	 * 
	 * @param baseUrl
	 * @param url
	 * @return
	 */
	private String addBaseUrl(String baseUrl, String url) {
		
		for(String prefix : this.validLinkPrefixes)
			if(url.startsWith(prefix))
				return url;
		
		
		String newUrl = url;
		try {
			newUrl= new URL(new URL(baseUrl), url).toString();
		} catch (MalformedURLException e) {}
		
		return newUrl;
	}

	/**
	 * Ueberprueft ob es sich um einen unterstützten Content-Type handelt
	 * @param contentType
	 * @return
	 */
	private boolean supportContentType(String contentType) {
		for(String type : this.supportedContentTypes)
			if(contentType.startsWith(type.trim()))
				return true;
		
		return false;
	}

	/**
	 * Durchsucht eine Website und deren Verweise nach Emails
	 * @param url
	 * @param depth
	 * @return
	 */
	public List<String> harvestEmails(String url, int depth)
	{
		final String mailTo = "mailto:";
		final List<String> emails = new LinkedList<String>();
		
		//Breitensuche über die Verweise
		int i;
		for (String link : this.getLinks(url)) {

			if(visitedLinks.contains(link))
				continue;
			else
				visitedLinks.add(link);
			
			if(depth > 0) {
				
				this.harvestEmails(link, depth - 1);
				
			}
			
			i = link.indexOf(mailTo);
			
			if(i != -1)
				emails.add(link.substring(i + mailTo.length()));
		}
		
		return emails;
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage: java Harvest url depth");
			System.exit(2);
		}
		
		int depth = Integer.parseInt(args[1]);
		
		System.out.println("Harvest email for given url: " + args[0]);
		
		for (String email : new Harvest().harvestEmails(args[0], depth)) {
			System.out.println(email);
		}
	}
}
