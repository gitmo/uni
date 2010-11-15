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
	private static final int PORT = 56789;
	InetAddress group;
	MulticastSocket socket;

	public static void main(String[] args) throws UnknownHostException,
			IOException, InterruptedException {

		new Multicast();
	}

	public Multicast() throws IOException, InterruptedException {
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

	Runnable receiver = new Runnable() {
		byte[] buf = new byte[1024];
		DatagramPacket recv = new DatagramPacket(buf, buf.length);

		@Override
		public void run() {
			try {
				while (true) {
					System.out.println("Ready to receive");
					// get their responses!
					socket.receive(recv);
					String recvStr = new String(recv.getData(), 0,
							recv.getLength());
					System.out.println("Received: " + recvStr);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	Runnable sender = new Runnable() {
		DatagramPacket send;

		@Override
		public void run() {
			try {
				for (int i = 0; i < 10; i++) {
					// send the group salutations
					String msg = "Hello" + i;
					send = new DatagramPacket(msg.getBytes(), msg.length(),
							group, PORT);
					socket.send(send);
					System.out.println("Sending");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};
}