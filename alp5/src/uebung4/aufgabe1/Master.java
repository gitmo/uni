package uebung4.aufgabe1;

import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.Random;

public class Master {

	/**
	 * Usage: java Master n host [host ...] Search for the biggest number in an
	 * array divided in n chunks on hosts.
	 */
	public static void main(String[] args) throws RemoteException {

		if (args.length < 2) {
			System.out.println("Usage: java Master N host [host ...]\n"
					+ "\t Search for the biggest number in an array\n"
					+ "\t divided in N chunks on the given hosts.");
			System.exit(2);
		}

		System.out.println("Master.main(): start");

		// Number of problem partitions
		int n = Integer.valueOf(args[0]);

		// Fill an array with random values.
		byte[] arr = new byte[73];
		new Random().nextBytes(arr);

		Master cli = new Master();

		// Fill the argument pool.
		cli.feed(n, arr);

		// Start TaskMax on each host.
		for (int i = 1; i < args.length; i++) {
			cli.start(args[i] + ":" + WorkerImpl.PORT, new TaskMax(), argpool,
					respool);
		}

		// Evaluate the result pool.
		int max = cli.gather(n);
		cli.exit(argpool, respool);

		// Re-calculate biggest element for reference
		Arrays.sort(arr);
		int expected = arr[arr.length - 1];

		System.out.printf("Master.main(): Computed result: %d (expected %d)\n",
				max, expected);
		System.out.println("Master.main(): done");
	}

	/** Pool of arguments for unprocessed computation */
	private static Pool<Integer[]> argpool;

	/** Pool of processed results */
	private static Pool<Integer> respool;

	public Master() throws RemoteException {
		argpool = new PoolImpl<Integer[]>();
		respool = new PoolImpl<Integer>();
	}

	public void delay(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e1) {
		}
	}

	public <A, R> void exit(Pool<A> argpool, Pool<R> respool) {
		try {
			// Remove the pools from the RMI runtime
			UnicastRemoteObject.unexportObject(argpool, false);
			UnicastRemoteObject.unexportObject(respool, false);
		} catch (NoSuchObjectException e) {
			e.printStackTrace();
		}
	}

	private void feed(int n, byte[] arr) throws RemoteException {

		// Ceiled division: How big are the chunks to contain all N items?
		int chunksize = (arr.length + n - 1) / n;

		System.out.printf("Master.feed(): Splitting array of %d numbers "
				+ "in %d chunks of %d elements each.\n", arr.length, n,
				chunksize);

		Integer[] chunk;

		for (int segment = 0; segment < chunksize * n; segment += chunksize) {
			/*
			 * Fill up a chunk. If the last chunk is bigger and cannot be
			 * completed fill up with some elements from the start of the array.
			 */
			chunk = new Integer[chunksize];

			for (int offset = 0; offset < chunksize; offset++) {
				chunk[offset] = (int) arr[(segment + offset) % arr.length];
			}

			argpool.put(chunk);
		}
	}

	private Integer gather(int n) throws RemoteException {
		Integer res, max = Integer.MIN_VALUE;
		for (int i = 0; i < n; i++) {
			do {
				delay(20);
				res = respool.get();
			} while (res == null);

			System.out.println("Master.gather(): " + res);
			if (res > max)
				max = res;
		}
		return max;
	}

	public <Argument, Result> void start(String host, Task<Argument, Result> t,
			Pool<Argument> argpool, Pool<Result> respool) {

		// Reference of the remote object.
		Worker obj = null;

		System.out.println("Master.start(): Connecting to worker on " + host);
		while (obj == null)
			try {
				obj = (Worker) Naming.lookup("//" + just(host) + "/RmiServer");
				obj.start(t, argpool, respool);
			} catch (Exception e) {
				System.out.println("Master.start(): Retrying " + host);
				delay(1000);
			}
		System.out.println("Master.start(): Successful connection to " + host);
	}

	// Discards a user@ prefix from host string.
	private String just(String host) {
		String[] split = host.split("@");
		return split[split.length - 1];
	}
}
