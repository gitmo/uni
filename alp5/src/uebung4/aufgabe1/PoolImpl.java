package uebung4.aufgabe1;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PoolImpl<T> extends UnicastRemoteObject implements Pool<T> {

	private static final long serialVersionUID = -8050635024858332765L;

	Queue<T> queue = new ConcurrentLinkedQueue<T>();

	protected PoolImpl() throws RemoteException {
		super();
	}

	public T get() throws RemoteException {
		return queue.poll();
	}

	public void put(T t) throws RemoteException {
		queue.offer(t);
	}
}
