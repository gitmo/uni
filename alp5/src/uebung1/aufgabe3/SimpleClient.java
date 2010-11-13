package uebung1.aufgabe3;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Einfacher Client, der jede halbe Sekunde eine Nachricht an einen Port sendet
 * 
 * @author xxx & xxx (Gruppe xxx)
 * 
 */
public class SimpleClient {
	public static void main(String[] args) throws IOException {
		Socket socket = new Socket("localhost", 9955);

		PrintStream output = new PrintStream(socket.getOutputStream());

		final String str = "Teststring\n";
		while (true) {
			output.append(str);
			System.out.println("Sent \"" + str + "\"");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
