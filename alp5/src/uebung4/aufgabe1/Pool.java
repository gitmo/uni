package uebung4.aufgabe1;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Pool<T> extends Remote {

	/* get and remove some item - or get null if no item */
	T get() throws RemoteException;

	/* deposit item */
	void put(T t) throws RemoteException;

}