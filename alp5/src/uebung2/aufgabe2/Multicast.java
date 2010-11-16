package uebung2.aufgabe2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Multicast chat system
 * 
 * @author xxx & xxx (Gruppe xxx)
 */
public class Multicast {
	// Multicast: 224.0.0.1 - 239.255.255.255
	private static final String CHATGROUP = "229.1.2.3";
	private static final int PORT = 56789;

	// Chat prompt and command to leave chat.
	private static final String PROMPT = "> ";
	private static final String QUIT = ":q"; // Enter to leave chat

	// Private fields used in between threads.
	private InetAddress group;
	private MulticastSocket socket;
	private String name;

	/**
	 * Run a Multicast chat session.
	 * 
	 * @param args
	 *            Chat client's name.
	 */
	public static void main(String[] args) throws IOException,
			InterruptedException {

		if (args.length < 1) {
			System.err.println("Usage: java Multicast <name>");
			System.exit(2);
		}
		new Multicast(args[0]);
	}

	/**
	 * Control flow of chat system. Setup and tear down of Multicast
	 * communication.
	 */
	public Multicast(String name) throws IOException, InterruptedException {
		// Set client name
		this.name = name;
		// join a Multicast group
		group = InetAddress.getByName(CHATGROUP);
		socket = new MulticastSocket(PORT);
		socket.joinGroup(group);
		// Start receiving and sending threads
		Thread tr, ts;
		(tr = new Thread(receiver)).start();
		(ts = new Thread(sender)).start();

		// Wait for sender to quit.
		System.out.println("Start chatting. Ctrl-D or " + QUIT + " to quit.");
		ts.join();

		// OK, I'm done talking - leave the group...
		System.out.println("Leaving group.");
		socket.leaveGroup(group);
		// Hangup
		socket.close();
		// Waiting for receiver to give up.
		tr.join();
		System.out.println("Done.");
	}

	/**
	 * Sending thread. Sends every line from console until Ctrl-D or ':q'.
	 */
	Runnable sender = new Runnable() {
		DatagramPacket send;
		BufferedReader input = new BufferedReader(new InputStreamReader(
				System.in));

		@Override
		public void run() {
			System.out.print(PROMPT);
			try {
				while (true) {
					String readLine = input.readLine();
					// EOF (^D) or ':q' received
					if (readLine == null || readLine.equalsIgnoreCase(QUIT))
						break;

					String msg = name + ": " + readLine;
					send = new DatagramPacket(msg.getBytes(), msg.length(),
							group, PORT);
					socket.send(send);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	/**
	 * Receiving thread. Quits on error or socket closed.
	 */
	Runnable receiver = new Runnable() {
		byte[] buf = new byte[1024];
		DatagramPacket recv = new DatagramPacket(buf, buf.length);

		@Override
		public void run() {
			try {
				while (true) {
					// get their responses!
					socket.receive(recv);
					String recvStr = new String(recv.getData(), 0,
							recv.getLength());
					System.out.println();
					System.out.println(recvStr);
					System.out.print(PROMPT);
				}
			} catch (IOException e) {
				System.out.println("Receiver quit.");
			}
		}
	};
}
