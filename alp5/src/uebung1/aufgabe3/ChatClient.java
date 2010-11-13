package uebung1.aufgabe3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Einfacher Chat-Client
 * 
 * @author xxx & xxx (Gruppe xxx)
 */
public class ChatClient {
	public static void main(String[] args) throws UnknownHostException,
			IOException {

		if (args.length < 3) {
			System.err
					.println("You have to specify at least host, port and client name.");
			return;
		}

		Socket socket = new Socket(args[0], Integer.parseInt(args[1]));

		System.out.println("Socket initialized");

		final BufferedReader input = new BufferedReader(new InputStreamReader(
				System.in));
		final BufferedReader incoming = new BufferedReader(
				new InputStreamReader(socket.getInputStream()));
		final PrintStream output = new PrintStream(socket.getOutputStream(),
				true);

		String name = args[2];
		output.println(name);

		// Thread für das Versenden von eingegeben Nachrichten
		Runnable inputWorker = new Runnable() {
			@Override
			public void run() {
				while (true) {
					String inputMessage = null;

					// Lese eine eingegeben Zeile
					try {
						inputMessage = input.readLine();
						System.out.print("> ");
					} catch (IOException e) {
						e.printStackTrace();
					}

					// Wenn etwas eingegeben wurde, schicke es an den Server
					if (inputMessage != null)
						output.append(inputMessage + "\n");

					// Um CPU-Last zu sparen
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
					}
				}
			}
		};

		// Thread für die Abarbeitung einkommender Nachrichten
		Runnable incomingWorker = new Runnable() {
			@Override
			public void run() {
				while (true) {
					String incomingMessage = null;

					// Versuche eine vom Server an den Client verschickte
					// Nachrichten-Zeile zu lesen
					try {
						incomingMessage = incoming.readLine();
					} catch (IOException e) {
						e.printStackTrace();
					}

					// Wenn Daten vorhanden sind gebe sie aus
					if (incomingMessage != null) {
						System.out.println(incomingMessage);
						System.out.print("> ");
					}

					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
					}
				}
			}
		};

		new Thread(incomingWorker).start();
		new Thread(inputWorker).start();
	}
}
