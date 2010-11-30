package uebung3.aufgabe1b;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import uebung2.aufgabe1.Dictionary;

public class FilterRemote implements IFilterRemote {
	SortedSet<String> localOcurrence = new TreeSet<String>();
	public static final String SALT = "TEAM.AWESOME.";
	public static String myName = SALT + FilterRemote.class.getName();
	public static int port = 1099;

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

	public static void main(String[] args) {

		if (args.length < 1) {
			System.err.println("Usage: java " + FilterRemote.class.getName()
					+ " [port]");
			System.exit(2);
		}

		port = Integer.parseInt(args[0]);

		try {
			String name = myName;
			IFilterRemote ci = new FilterRemote();
			IFilterRemote stub = (IFilterRemote) UnicastRemoteObject
					.exportObject(ci, 0);
			Registry reg = LocateRegistry.createRegistry(port);
			reg.rebind(name, stub);
			System.out
					.println("FilterImpl.main() Engine bound on port " + port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
