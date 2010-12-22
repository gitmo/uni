package uebung4.aufgabe1;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.UnmarshalException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class WorkerImpl extends UnicastRemoteObject implements Worker {

	private static final long serialVersionUID = -6391712628582309146L;

	/** Port of the RMI registry */
	public static final int PORT = 10999;

	/** Hostname of this worker. */
	private static String host;

	/**
	 * Launch this on the remote worker machines.
	 */
	public static void main(String[] args) throws UnknownHostException {

		host = InetAddress.getLocalHost().toString();
		System.out.println(host + ": WorkerImpl.main(): RMI server started.");

		try { // special exception handler for registry creation
			LocateRegistry.createRegistry(PORT);
			System.out.println(host + ": Java RMI registry created.");
		} catch (RemoteException e) {
			// not good, error means registry already exists
			System.out.println(host + ": Java RMI registry already exists"
					+ " on port " + PORT + ". Choose a different port!");
			System.exit(1);
		}

		try {
			// Instantiate WorkerImpl
			WorkerImpl obj = new WorkerImpl();

			// Bind this object instance to the name "RmiServer"
			Naming.rebind("//localhost:" + PORT + "/RmiServer", obj);

			System.out.println(host + ": RmiServer bound in registry");
		} catch (Exception e) {
			System.err.println(host + ": RMI server exception:" + e);
			e.printStackTrace();
		}
	}

	/**
	 * Creates a WorkerImpl object by calling the super constructor of
	 * UnicastRemoteObject which exports it to the RMI.
	 */
	protected WorkerImpl() throws RemoteException {
		super();
	}

	/**
	 * Small delay for testing so no other worker machine will be starved.
	 */
	@SuppressWarnings("unused")
	private void artificialDelay() {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
		}
	}

	/**
	 * Provides a clean exit from the RMI system so the program can terminate.
	 */
	public void exit() {
		try {
			// Unregister ourself
			Naming.unbind("//localhost:" + PORT + "/RmiServer");

			// Unexport; this will also remove us from the RMI runtime
			UnicastRemoteObject.unexportObject(this, true);

			System.out.println(host + ": RmiServer exiting.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a new thread for the computation and return immediately to the
	 * caller.
	 */
	public <Argument, Result> void start(final Task<Argument, Result> t,
			final Pool<Argument> argpool, final Pool<Result> respool)
			throws RemoteException {

		System.out.println(host + ": WorkerImpl.start()");

		Runnable runnable = new Runnable() {

			public void run() {

				int c = 0;
				try {
					Argument arg;
					while ((arg = argpool.get()) != null) {
						respool.put(t.exec(arg));
						c++;
						// artificialDelay();
					}
					// If argpool is not reachable, we're done.
				} catch (NoSuchObjectException e) { // Master has quit
				} catch (UnmarshalException e) { // ditto
				} catch (ConnectException e) { // ditto
				} catch (RemoteException e) {
					System.out.println(host + ": WorkerImpl.start(...)."
							+ "new Runnable() {...}.run(): " + e);
				}
				System.out.println(host + ": Chunks processed: " + c);
				exit(); // Cleanup to quit this worker.
			}

		};
		new Thread(runnable).start();
	}
}
