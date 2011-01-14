package uebung5.aufgabe1;

public class WebServer {

	/**
	 * Startet einen einfachen Webserver
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

		int serverPort = Integer.valueOf(args[0]);
		int statisticPort = Integer.valueOf(args[1]);

		while (true) {
			try {
				Thread serverThread = new Thread(new ServerWorker(serverPort));
				Thread statisticThread = new Thread(new StatisticWorker(statisticPort));

				serverThread.start();
				System.out.println("Started server thread on " + serverPort
						+ " Port.");

				statisticThread.start();
				System.out.println("Started statistic thread on " + statisticPort
						+ " Port.");
				
				serverThread.join();
				statisticThread.join();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}

	}
}
