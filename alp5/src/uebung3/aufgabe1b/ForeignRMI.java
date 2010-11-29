package uebung3.aufgabe1b;

import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import uebung2.aufgabe1.Dictionary;
import uebung3.aufgabe1a.Foreign;

public class ForeignRMI extends Foreign {
	private static String myName = ForeignRMI.class.getName();

	private static int port;

	private List<String> hostList;

	public ForeignRMI(String fileName, List<String> hostList)
			throws FileNotFoundException {
		super(fileName);
		this.hostList = hostList;
	}

	IRemoteSet<String> globalOcurrences;

	private Dictionary dictionary = new Dictionary();

	public void analyse() {
		Registry registry = null;
		String name = myName + "-globalSet";
		try {
			IRemoteSet<String> engine = new RemoteSet<String>();
			globalOcurrences = (IRemoteSet<String>) UnicastRemoteObject
					.exportObject(engine, 0);
			registry = LocateRegistry.getRegistry(port);
			registry.rebind(name, globalOcurrences);
			System.out.println("globalOcurrences bound");
		} catch (Exception e) {
			System.err.println("globalOcurrences exception:");
			e.printStackTrace();
		}

		Thread[] threads = new Thread[hostList.size()];

		int i = 0;
		for (String hs : hostList) {
			(threads[i++] = new Worker(hs + "-"
					+ FilterImpl.myName)).start();
		}

		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		try {
			printOcurrences(globalOcurrences.remoteGetAll());
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		try {
			registry.unbind(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getRealHostName(String hostname) {
		String realHostName = null;
		try {
			realHostName = InetAddress.getByName(hostname).getHostName();
		} catch (UnknownHostException e) {
		}
		return realHostName;
	}

	class Worker extends Thread {

		private String name;

		public Worker(String name) {
			this.name = name;
		}

		@Override
		public void run() {
			try {
				String name = this.name;
				System.out.println(name);
				System.out.println(port);
				Registry reg = LocateRegistry.getRegistry(port);
				IFilter filter = (IFilter) reg.lookup(name);

				filter.filter(globalOcurrences, lines, dictionary);

				reg.unbind(name);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {

		if (args.length < 2) {
			System.err.println("Usage: java " + myName + "port host [host...]");
			System.exit(2);
		}
		port = Integer.parseInt(args[0]);

		List<String> hostList = new ArrayList<String>();
		for (int i = 1; i < args.length; i++) {
			hostList.add(args[i]);
		}

		ForeignRMI remoteForeign;
		try {
			remoteForeign = new ForeignRMI("Blatt3.html.txt", hostList);
			remoteForeign.analyse();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
