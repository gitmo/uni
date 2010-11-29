package uebung3.aufgabe1b;

import java.util.List;
import java.util.Set;

import uebung2.aufgabe1.Dictionary;

public interface IFilter<T> {
	public Set<T> filter(IRemoteSet<String> globalOcurrence,
			List<String> lines, Dictionary dictionary);
}
