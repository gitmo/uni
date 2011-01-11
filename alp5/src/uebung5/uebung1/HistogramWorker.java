package uebung5.uebung1;

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
		Map<String, Integer> histogram = this.loadHistogram();
		StringBuilder builder = new StringBuilder();
		builder.append("Histogram\n");
		for (String word : histogram.keySet())
			builder.append(word + ":\t" + histogram.get(word));
		return builder.toString();
	}

}
