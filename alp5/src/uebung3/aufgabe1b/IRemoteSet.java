package uebung3.aufgabe1b;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Set;

interface IRemoteSet<T> extends Remote {

	public boolean remoteAddAll(Collection<T> collection)
			throws RemoteException;

	public Set<T> remoteGetAll()
			throws RemoteException;

}
