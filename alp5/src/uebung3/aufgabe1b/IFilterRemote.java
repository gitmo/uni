package uebung3.aufgabe1b;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

import uebung2.aufgabe1.Dictionary;

public interface IFilterRemote extends Remote{
	public Set<String> filter(IRemoteSet<String> globalOcurrence,
			List<String> lines, Dictionary dictionary) throws RemoteException;
}
