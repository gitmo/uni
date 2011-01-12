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
	
	public void run() {
		ServerSocket socket;
		try {
			socket = new ServerSocket(port);
			while (true) {
				// Anfrage entgegennehmen
				Socket connection = socket.accept();
				
				OutputStreamWriter responseStream = new OutputStreamWriter(connection.getOutputStream());
				responseStream.append("HTTP/1.1 200 OK\r\n");
				responseStream.append("Content-Type: text/html\r\n");
				responseStream.append("\r\n");
				responseStream.append(getHistogramString());
				responseStream.flush();
				connection.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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
			sb.append("<td>" + browser + "</td><td>" + histogram.get(browser) + "</td>\n");
			sb.append("</tr>\n");
		}

		sb.append("</table>\n");
		
		return htmlTemplate.replace("<!-- Histogram -->", sb.toString());
	}

}
