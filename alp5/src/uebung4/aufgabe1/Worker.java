package uebung4.aufgabe1;

import java.rmi.Remote;
import java.rmi.RemoteException;

interface Worker extends Remote {

	<Argument, Result> void start(Task<Argument, Result> t,
			Pool<Argument> argpool, Pool<Result> respool)
			throws RemoteException;

}