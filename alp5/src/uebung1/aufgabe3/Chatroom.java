package uebung1.aufgabe3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import uebung1.aufgabe1.ConcurrentEvent;
import uebung1.aufgabe1.Event;

/**
 * Einfacher Chatroom-Server
 * 
 * @author xxx & xxx (Gruppe xxx)
 */
public class Chatroom {
	public static void main(String[] args) throws IOException {

		ServerSocket listen = null;

		// Finde einen freien Port und starte den Server auf diesem
		int port = 50000;
		while (listen == null) {
			try {
				listen = new ServerSocket(port);
			} catch (BindException e) {
				port++;
			}
		}

		final String subject = (args.length > 0) ? args[0] : "";

		System.out.println("Sever socket initialized on port " + port);

		final ConcurrentEvent events = new ConcurrentEvent();

		while (true) {

			// Blockierendes Warten bis ein Client sich verbindet
			final Socket socket = listen.accept();

			final String hostName = socket.getInetAddress().getHostName();
			System.out.println("New client \"" + hostName + "\" connected");

			Runnable workerClient = new Runnable() {
				public void run() {
					Event.Ticket ticket = null;
					BufferedReader br = null;
					String name = "nobody";

					// Registriere den Client
					try {
						OutputStream os = socket.getOutputStream();
						InputStream is = socket.getInputStream();
						ticket = events.register(os);
						br = new BufferedReader(new InputStreamReader(is));
						// Get client's name
						name = br.readLine();
						System.out.printf("Host %s is '%s'\n", hostName, name);
						// Send subject line
						PrintStream ps = new PrintStream(os, true);
						ps.printf("Wecome %s! Subject is '%s'\n", name, subject);
					} catch (IOException e) {
						e.printStackTrace();
						return;
					}

					// Nehme solange Daten entgegen, bis der Client die
					// Verbindung abgebrochen hat
					while (true) {
						String message = null;

						// Warte bis vom Client eine Nachricht geschickt wurde
						try {
							message = br.readLine();
							if (message != null)
								System.out.printf(
										"Message from '%s' <%s>: %s\n", name,
										hostName, message);
						} catch (IOException e) {
							e.printStackTrace();
						}

						// Client hat die Verbindung abgebrochen
						if (message == null || socket.isClosed())
							break;

						// Sende die Nachricht an alle anderen
						// Chat-Teilnehmer_innen
						events.propagate(name + ": " + message + "\n", ticket);
					}

					// Entferne den Client vom Chatraum und räume auf
					events.cancel(ticket);
					try {
						br.close();
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

					System.out.println("Client " + hostName + " disconnected");

				}
			};

			new Thread(workerClient).start();
		}
	}
}
