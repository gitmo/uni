package uebung5.aufgabe1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class HistogramWorker extends BaseWorker implements Runnable {

	int port;

	public HistogramWorker(int port) {
		this.port = port;
	}

	/**
	 * Wartet auf einem Socket auf Verbindungen Wurde eine Verbindung
	 * hergestellt, so wird eine HTML-Browser-Statistik-Website zurückgegeben.
	 */
	public void run() {
		ServerSocket socket;
		try {
			socket = new ServerSocket(port);
			while (true) {
				// Anfrage entgegennehmen
				Socket connection = socket.accept();

				OutputStreamWriter responseStream = new OutputStreamWriter(
						connection.getOutputStream());
				// HTTP-Header
				responseStream.append("HTTP/1.1 200 OK\r\n");
				responseStream
						.append("Content-Type: text/html;charset=utf-8\r\n");
				responseStream.append("\r\n");

				// File-Content
				responseStream.append(getHistogramString());
				responseStream.flush();
				connection.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Lädt eine HTML-Template-Datei und schreibt den Inhalt der serialisierten
	 * Histogramm-Map-Datenstruktur in diese.
	 * 
	 * @return string with html content
	 */
	String getHistogramString() {
		String htmlTemplate = "";
		try {
			htmlTemplate = this.getFileContent("histogram.html");
		} catch (FileNotFoundException e) {
		}

		Map<String, Integer> histogram = this.loadHistogram();
		StringBuilder sb = new StringBuilder();

		sb.append("<table>\n");

		for (String browser : histogram.keySet()) {
			sb.append("<tr>\n");
			sb.append("<td>" + browser + "</td><td>" + histogram.get(browser)
					+ "</td>\n");
			sb.append("</tr>\n");
		}

		sb.append("</table>\n");

		return htmlTemplate.replace("<!-- Histogram -->", sb.toString());
	}

}
