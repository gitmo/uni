package uebung4.aufgabe2;

import java.rmi.RemoteException;

import thirdparty.Painter;
import uebung4.aufgabe1.Master;
import uebung4.aufgabe1.Pool;
import uebung4.aufgabe1.PoolImpl;
import uebung4.aufgabe1.WorkerImpl;

public class MandelMaster extends Master {

	private static Pool<Segment> argpool;
	private static Pool<Segment> respool;
	private static MandelTask mandelTask;

	public static void main(String[] args) throws RemoteException {

		if (args.length < 3) {
			System.out.println("Usage: java MandelMaster Segments Depth host"
					+ " [host ...]\n");
			System.exit(2);
		}

		System.out.println("MandelMaster.main(): start");

		// Number of problem partitions
		int segments = Integer.valueOf(args[0]);

		// Dimension of image
		int heigth = 1024;
		int width = (int) (heigth * MandelTask.dx_dy);

		// Iteration depth
		int iterationDepth = Integer.valueOf(args[1]);

		MandelMaster cli = new MandelMaster(width, heigth, iterationDepth);

		// Fill the argument pool.
		cli.feed(width, heigth, segments);

		// Start MandelTask on each host.
		for (int i = 2; i < args.length; i++) {
			cli.start(args[i] + ":" + WorkerImpl.PORT, mandelTask, argpool,
					respool);
		}

		// Evaluate the result pool.
		cli.gather(segments);

		System.out.println("MandelMaster.main(): done");
	}

	private Painter painter;

	public MandelMaster(int width, int heigth, int iterationDepth)
			throws RemoteException {
		argpool = new PoolImpl<Segment>();
		respool = new PoolImpl<Segment>();
		mandelTask = new MandelTask(width, heigth, iterationDepth);
		String title = width + "x" + heigth;
		painter = new Painter(title, width, heigth);
	}

	private void draw(Segment r) {
		painter.update(r.getX(), r.getY(), r.getWidth(), r.getHeigth(),
				r.getRgbArray(), 0, r.getWidth());
	}

	private void feed(int width, int heigth, int segments)
			throws RemoteException {

		// Truncated division: Some of the last pixel rows might be missing.
		int segmentsize = heigth / segments;

		System.out.printf("MandelMaster.feed(): Splitting bitmap of %d rows "
				+ "in %d segments of %d pixel rows each.\n", heigth, segments,
				segmentsize);

		Segment r;

		for (int y = 0; y < segmentsize * segments; y += segmentsize) {
			r = new Segment(0, y, width, segmentsize);
			argpool.put(r);
		}
	}

	private void gather(int segments) throws RemoteException {
		Segment r;
		for (int i = 0; i < segments; i++) {
			do {
				delay(20);
				r = respool.get();
			} while (r == null);
			draw(r);
		}
	}
}
