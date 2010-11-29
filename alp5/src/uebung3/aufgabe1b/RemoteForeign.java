package uebung3.aufgabe1b;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import uebung2.aufgabe1.Dictionary;
import uebung3.aufgabe1a.Foreign;
import uebung3.aufgabe1b.RemoteLineWorker;

public class RemoteForeign extends Foreign {
	IRemoteSet<String> globalOcurrences;

	/**
	 * 
	 */
	private static Dictionary dictionary = new Dictionary();


	public RemoteForeign() {
		super();
		
		dictionary = new Dictionary();
		
		if (System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());
   
        try {
            String name = "globalSet";
            IRemoteSet<String> engine = new RemoteSet<String>();
			IRemoteSet<String> globalOcurrences =
                (IRemoteSet<String>) UnicastRemoteObject.exportObject(engine, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, globalOcurrences);
            System.out.println("globalOcurrences bound");
        } catch (Exception e) {
            System.err.println("globalOcurrences exception:");
            e.printStackTrace();
        }
	}
	
	public void analyse(String fileName, int numberOfThreads) {
		try {
			lines = this.loadFile(fileName);
		} catch (FileNotFoundException e) {
			System.err.println("File not found");
			return;
		}
		
		
		Thread[] workers = new Thread[1];
		
		workers[0] = new Thread(new RemoteLineWorker(globalOcurrences, lines));
		
		for(Thread thread : workers)
			if(thread !=  null)
				thread.start();
		
		for(Thread thread : workers) {
			if(thread !=  null) {
				try {
					thread.join();
				} catch (InterruptedException e) { e.printStackTrace();	}
			}
		}
		
		printOcurrences(globalOcurrences);
	}
	
	public static void main(String[] args) {
		RemoteForeign remoteForeign = new RemoteForeign();
		remoteForeign.analyse("test.txt", 2);
	}
}
