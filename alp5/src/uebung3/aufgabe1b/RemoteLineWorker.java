package uebung3.aufgabe1b;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import uebung2.aufgabe1.Dictionary;

public class RemoteLineWorker implements Runnable {
	ArrayList<String> lines = null;
	Set<String> localOcurrence = new TreeSet<String>();
	IRemoteSet<String> globalOcurrence = null;
	Dictionary dictionary;

	public RemoteLineWorker(IRemoteSet<String> globalOcurrence,
			ArrayList<String> lines) {
		this.lines = lines;
		this.globalOcurrence = globalOcurrence;
		this.dictionary = new Dictionary();
	}

	public void run() {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		try {
			String name = "filter";
			Registry registry = LocateRegistry.getRegistry("localhost");
			Filter filter = (Filter) registry.lookup(name);
			globalOcurrence.remoteAddAll(filter.filter(globalOcurrence, lines,
					dictionary));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
