package uebung3.aufgabe1b;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import uebung2.aufgabe1.Dictionary;

public class Filter implements IFilter<String> {
	SortedSet<String> localOcurrence = new TreeSet<String>();

	public Set<String> filter(IRemoteSet<String> globalOcurrence,
			List<String> lines, Dictionary dictionary) {
		String[] words = null;

		for (String currentLine : lines) {
			words = currentLine.split("\\s");

			for (String currentWord : words) {
				if (!dictionary.contains(currentWord))
					continue;

				if (!localOcurrence.contains(currentWord)) {
					localOcurrence.add(currentWord);

					try {
						globalOcurrence.remoteAddAll(localOcurrence);
					} catch (RemoteException e) {
						e.printStackTrace();
					}

					localOcurrence.addAll(globalOcurrence);
				}
			}
		}

		return localOcurrence;
	}

}
