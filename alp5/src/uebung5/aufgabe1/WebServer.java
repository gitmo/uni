package uebung5.aufgabe1;

public class WebServer {

	/**
	 * Startet eines einfachen Webserver
	 * 
	 * Hierzu wird mit Hilfe von zwei Threads auf verschiedenen Ports eine
	 * statische HTML-, sowie ein Histogram-Seite verfügbar gemacht.
	 * 
	 * @param int port1
	 * @param int port2
	 * @throws Exception
	 */
	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Usage: java Webserver port1 port2");
			System.exit(2);
		}

		int port1 = Integer.valueOf(args[0]);
		int port2 = Integer.valueOf(args[1]);

		while (true) {
			try {
				Thread serverThread = new Thread(new ServerWorker(port1));
				Thread histogramThread = new Thread(new HistogramWorker(port2));

				serverThread.start();
				System.out.println("Started server thread on " + port1
						+ " Port.");

				histogramThread.start();
				System.out.println("Started histogram thread on " + port2
						+ " Port.");
			} catch (Exception e) {

			}
		}

	}
}
