package uebung5.aufgabe1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class StatisticWorker extends BaseWorker implements Runnable {

	int port;

	public StatisticWorker(int port) {
		this.port = port;
	}

	/**
	 * Wartet auf einem Socket auf Verbindungen Wurde eine Verbindung
	 * hergestellt, so wird eine HTML-Browser-Statistik-Website zurückgegeben.
	 */
	public void run() {
		ServerSocket socket = null;
		try {
			socket = new ServerSocket(port);
			while (true) {
				// Anfrage entgegennehmen
				Socket connection = null;
				try {
					connection = socket.accept();
	
					// Timeout setzen, damit read() nicht für immer blocken kann.
					// Dieser muss groß genug für Clients mit honen Latenzen sein.
					// Z.Zt. wird nur eine Verbindung zu einem Zeitpunkt behandelt.
					// TODO: Klären ob ein Thread pro Verbindung sinnvoller ist.
					connection.setSoTimeout(500);
					
					OutputStreamWriter responseStream = new OutputStreamWriter(
							connection.getOutputStream());
					// HTTP-Header
					responseStream.append("HTTP/1.1 200 OK\r\n");
					responseStream
							.append("Content-Type: text/html;charset=utf-8\r\n");
					responseStream.append("\r\n");
	
					// File-Content
					responseStream.append(getStatisticString());
					responseStream.close();
					connection.close();
				//u.a. SocketException, SocketTimeoutException
				} catch (IOException e) {
					if(connection != null)
						connection.close();
				}
			}
		} catch (Exception e) {
			System.err.println("Unknown error: " + e.getMessage());
			System.err.println("Trace:\n\n" + e.getStackTrace());
		} finally {
			try {
				if(socket != null)
					socket.close();
			} catch (IOException e) {
				System.err.println("Could not close socket");
			}
		}
	}

	/**
	 * Lädt eine HTML-Template-Datei und schreibt den Inhalt der serialisierten
	 * Histogramm-Map-Datenstruktur in diese.
	 * 
	 * @return string with html content
	 */
	String getStatisticString() {
		String htmlTemplate = "";
		try {
			htmlTemplate = this.getFileContent("statistic.html");
		} catch (FileNotFoundException e) {
		}

		Map<String, Integer> statistic = this.loadStatistic();
		StringBuilder sb = new StringBuilder();

		sb.append("<table>\n");
		sb.append("<tr><th>User-Agent</th><th>Anzahl</th></tr>\n");
		
		for (String browser : statistic.keySet()) {
			sb.append("<tr>\n");
			sb.append("<td>" + browser + "</td><td>" + statistic.get(browser)
					+ "</td>\n");
			sb.append("</tr>\n");
		}

		sb.append("</table>\n");

		return htmlTemplate.replace("<!-- Statistic -->", sb.toString());
	}

}
