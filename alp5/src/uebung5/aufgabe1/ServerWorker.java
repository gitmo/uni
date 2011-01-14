package uebung5.aufgabe1;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerWorker extends BaseWorker implements Runnable {

	final static String persistenceFileName = "counter.persistence";
	int port;

	public ServerWorker(int port) {
		this.port = port;
	}

	/**
	 * Lädt die Http-Header-Daten in eine Map-Datenstruktur
	 * 
	 * @param stream
	 * @return
	 */
	public Map<String, String> getHeaderFields(InputStream stream) {
		BufferedReader requestStream = new BufferedReader(
				new InputStreamReader(stream));

		Map<String, String> map = new HashMap<String, String>();

		// Gültiger Stream?
		if (requestStream != null) {

			try {
				char[] buffer = new char[256];
				// Liest 256-Bytes in den Buffer
				// Verhindert, dass Angreifer mit Byte-Blob-Daten im Http-Header
				// den Server in die Knie zwingen
				requestStream.read(buffer, 0, buffer.length);

				// Http-Header-Zeilen werden immer mit "\r\n"-abgeschloßen
				String[] lines = new String(buffer).split("\r\n");

				// Erste Zeile ist die HTTP-Anfrage
				if (lines.length > 0 && lines[0] != null)
					map.put(null, lines[0].trim());

				// Http-Header Daten
				// Fieldname : Fieldvalue
				for (int i = 1; i < lines.length; ++i) {
					int colonIndex = lines[i].indexOf(':');

					if (colonIndex != -1) {
						String fieldName = lines[i].substring(0, colonIndex)
								.trim();
						String fieldValue = lines[i].substring(colonIndex + 1)
								.trim();

						map.put(fieldName, fieldValue);
					}

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	/**
	 * Lädt eine serialisierte Int-Variable und setzt diese in ein HTML-Template
	 * 
	 * @param responseMsg
	 * @return
	 */
	public String loadCounter(String responseMsg) {
		final String placeholder = "<!-- COUNTER -->";

		Integer counter = 0;
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(
					new FileInputStream(persistenceFileName));
			counter = ois.readInt();
			ois.close();

		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}

		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(
					persistenceFileName));
			oos.writeInt(counter + 1);
			oos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return responseMsg.replace(placeholder, counter.toString());
	}

	/**
	 * Liefert eine HTML-Website aus und speichert die User-Agents
	 */
	public void run() {
		ServerSocket socket;
		try {
			socket = new ServerSocket(port);
			while (true) {
				// Anfrage entgegennehmen
				Socket connection = socket.accept();

				final String responseMessage = loadCounter(getFileContent("404.html"));

				Map<String, String> fieldMap = this.getHeaderFields(connection
						.getInputStream());

				OutputStreamWriter responseStream = new OutputStreamWriter(
						connection.getOutputStream());
				// HTTP-Header
				responseStream.append("HTTP/1.1 200 OK\r\n");
				responseStream
						.append("Content-Type: text/html;charset=utf-8\r\n");
				responseStream.append("\r\n");
				// HTML-Content
				responseStream.append(responseMessage);
				responseStream.flush();
				connection.close();

				// Ermittelt den User-Agent
				String userAgent = stripUserAgent(fieldMap.get("User-Agent"));

				// Speichert den User-Agent zu Statistikzwecken ab
				if (userAgent != null) {
					Map<String, Integer> statistic = this.loadStatistic();

					if (statistic.containsKey(userAgent))
						statistic.put(userAgent, statistic.get(userAgent) + 1);
					else
						statistic.put(userAgent, 1);

					this.saveStatistic(statistic);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Formatiert die userAgent-Zeile in ein übersichtlicheres Format
	 * 
	 * @param userAgent
	 * @return
	 */
	public String stripUserAgent(String userAgent) {
		String[] commonUserAgents = { "Firefox", "MSIE", "Chrome", "Safari", 
				"Opera", "curl" };

		for (int i = 0; i < commonUserAgents.length; ++i)
			if (userAgent.contains(commonUserAgents[i]))
				return commonUserAgents[i];

		return "Unknown";
	}

}
