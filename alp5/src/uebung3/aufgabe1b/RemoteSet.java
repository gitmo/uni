package uebung3.aufgabe1b;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.TreeSet;

public class RemoteSet<T> extends TreeSet<T> implements IRemoteSet<T>{
	private static final long serialVersionUID = 2601212913645664134L;

	public boolean remoteAdd(T object) throws RemoteException {
		return this.add(object);
	}

	public boolean remoteAddAll(Collection<T> collection) throws RemoteException {
		return this.addAll(collection);
	}
}
