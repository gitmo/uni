package uebung5.aufgabe1;

public class WebServer {
	
	// starten mit java WebServer [ port ]
	public static void main(String[] arg) throws Exception {
		
		Thread serverThread = new Thread(new ServerWorker(8080));
		Thread histogramThread = new Thread(new HistogramWorker(8081));
		
		serverThread.start();
		System.out.println("Started server thread");

		histogramThread.start();
		System.out.println("Started histogram thread");
		
		serverThread.join();
		histogramThread.join();
		

	}
}
