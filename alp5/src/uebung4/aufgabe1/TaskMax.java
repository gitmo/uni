package uebung4.aufgabe1;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class TaskMax implements Task<Integer[], Integer> {

	private static final long serialVersionUID = -319489546477999677L;

	public Integer exec(Integer[] a) {

		// trace(a);

		int max = Integer.MIN_VALUE;
		for (Integer i : a)
			if (i > max)
				max = i;
		return max;
	}

	@SuppressWarnings("unused")
	private void trace(Integer[] a) {
		try {
			System.out.println(InetAddress.getLocalHost()
					+ ": TaskMax.exec(): " + Arrays.toString(a));
		} catch (UnknownHostException e) {
		}
	}
}
