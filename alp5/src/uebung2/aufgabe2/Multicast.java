package uebung2.aufgabe2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Multicast Chat-Client
 * 
 * @author xxx & xxx (Gruppe xxx)
 */
public class Multicast {
	private static final String PROMPT = "> ";
	private static final int PORT = 56789;
	InetAddress group;
	MulticastSocket socket;
	String name;

	public static void main(String[] args) throws UnknownHostException,
			IOException, InterruptedException {

		if (args.length < 1) {
			System.err.println("Usage: java Multicast <name>");
			System.exit(2);
		}
		new Multicast(args[0]);
	}

	public Multicast(String name) throws IOException, InterruptedException {
		// Set client name
		this.name = name;
		// join a Multicast group
		// 224.0.0.1 - 239.255.255.255
		group = InetAddress.getByName("229.1.2.3");
		socket = new MulticastSocket(PORT);
		socket.joinGroup(group);

		Thread tr = new Thread(receiver);
		Thread ts = new Thread(sender);

		tr.start();
		ts.start();

		ts.join();
		tr.join();

		// OK, I'm done talking - leave the group...
		socket.leaveGroup(group);
	}

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
					// EOF (^D) received
					if (readLine == null)
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
				e.printStackTrace();
			}
		}
	};
}
