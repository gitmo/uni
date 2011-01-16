package uebung5.aufgabe1;

import java.util.Date;

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

		Thread serverThread = null, statisticThread = null;

		while (true) {
			try {
				if(serverThread == null || !serverThread.isAlive()) {
					serverThread = new Thread(new ServerWorker(serverPort));
					serverThread.start();
					System.out.println(new Date() + "\tStarted server thread on " + serverPort
							+ " Port.");
				}

				if(statisticThread == null || !statisticThread.isAlive()) {
					statisticThread = new Thread(new StatisticWorker(statisticPort));
					statisticThread.start();
					System.out.println(new Date() + "\tStarted statistic thread on " + statisticPort
							+ " Port.");
				}
				
				serverThread.join(1000);
				statisticThread.join(1000);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}

	}
}
