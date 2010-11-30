package uebung3.aufgabe1b;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class RemoteSet<T> implements IRemoteSet<T> {
	private static final long serialVersionUID = 2601212913645664134L;
	private SortedSet<T> set = Collections.synchronizedSortedSet(new TreeSet<T>());

	public boolean remoteAddAll(Collection<T> collection) {
		return set.addAll(collection);
	}

	@Override
	public Set<T> remoteGetAll() throws RemoteException {
		return set;
	}
}
