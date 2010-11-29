package uebung3.aufgabe1b;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import uebung2.aufgabe1.Dictionary;

public class FilterImpl implements IFilter {
	SortedSet<String> localOcurrence = new TreeSet<String>();
	public static final String SALT = "TEAM.AWESOME.";
	public static int port = 1099;
	public static String myName = SALT + FilterImpl.class.getName();

	public Set<String> filter(IRemoteSet<String> globalOcurrence,
			List<String> lines, Dictionary dictionary) {
		String[] words = null;

		for (String currentLine : lines) {
			words = currentLine.split("\\b");

			for (String currentWord : words) {
				if (localOcurrence.contains(currentWord))
					continue;
				else if (dictionary.contains(currentWord)) {
					// Add word to local set and sync with global set.
					localOcurrence.add(currentWord);
					try {
						globalOcurrence.remoteAddAll(localOcurrence);
						localOcurrence.addAll(globalOcurrence.remoteGetAll());
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}
		}

		return localOcurrence;
	}

	private static String getHostName() {
		String hostname = null;
		try {
			hostname = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
		}
		return hostname;
	}

	public static void main(String[] args) {

		if (args.length < 1) {
			System.err.println("Usage: java " + myName + " host [port]");
			System.exit(2);
		} else if (args.length > 1) {
			port = Integer.parseInt(args[1]);
			System.out.println(port);
		}

		try {
			String name = getHostName() + "-" + myName;
			System.out.println("FilterImpl.main(): " + name);
			IFilter ci = new FilterImpl();
			IFilter stub = (IFilter) UnicastRemoteObject.exportObject(ci, 0);
			Registry reg = LocateRegistry.createRegistry(port);
			reg.rebind(name, stub);
			System.out.println("FilterImpl.main() Engine bound!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
