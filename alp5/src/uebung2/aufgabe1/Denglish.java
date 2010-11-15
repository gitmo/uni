package uebung2.aufgabe1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

public class Denglish {

	static final String SSH = "ssh"; // OpenSSH client
	static final boolean SSHDEBUG = true;

	// Classpath: if there are any "Class not found" problems fix here!
	static final String pwd = System.getProperty("user.dir");
	static final String cp = "bin:" + pwd + "/bin:" + pwd
			+ "/../../../bin:git/uni/alp5/bin";

	// Remote command
	static final String pkg = Denglish.class.getPackage().getName();
	static final String CMD = String.format("java -cp %s %s.Filter", cp, pkg);

	// Merge histogram of filtered words
	final ConcurrentHashMap<String, Integer> histogram = new ConcurrentHashMap<String, Integer>();

	public static void main(String[] args) throws IOException {

		System.out.println(pwd);
		if (args.length < 4) {
			System.err.println("Usage: java Denglish host1 host2 file1 file2");
			System.exit(-2); // POSIX
		}

		String[] hosts = { args[0], args[1] };
		String[] files = { args[2], args[3] };
		System.out.println(Arrays.toString(files));

		new Denglish(hosts, files);
	}

	public Denglish(String[] hosts, String[] files) {
		Thread threads[] = new Thread[2];

		for (int i = 0; i < 2; i++)
			(threads[i] = new Thread(new FilterWorker(hosts[i], files[i])))
					.start();

		for (Thread t : threads)
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		printHistogram();
	}

	class FilterWorker implements Runnable {
		// Standard output and error of remote process
		BufferedReader stdout, stderr;
		String hostname, command;

		public FilterWorker(String hostname, String filename) {
			this.hostname = hostname;
			this.command = String.format("%s %s %s %s", SSH, hostname, CMD,
					filename);
			System.out.println("About to run: " + this.command);
		}

		@Override
		public void run() {
			Process p = null;
			try {
				p = Runtime.getRuntime().exec(this.command);
			} catch (IOException e) {
				System.err.println("ERROR while executing: " + this.command);
				e.printStackTrace();
			}
			this.stdout = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			this.stderr = new BufferedReader(new InputStreamReader(
					p.getErrorStream()));

			String line = null;
			try {
				if (SSHDEBUG)
					while ((line = this.stderr.readLine()) != null)
						System.err.printf("%s: %s\n", this.hostname, line);
				while ((line = this.stdout.readLine()) != null) {
					if (histogram.containsKey(line))
						histogram.put(line, histogram.get(line) + 1);
					else
						histogram.put(line, 1);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	void printHistogram() {
		for (String word : histogram.keySet())
			System.out.println(word + ":\t" + histogram.get(word));
	}
}
