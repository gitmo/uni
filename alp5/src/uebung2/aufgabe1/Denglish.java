package uebung2.aufgabe1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

		if (args.length < 4) {
			System.err.println("Usage: java Denglish host1 host2 file1 file2");
			System.exit(2); // POSIX
		}

		String[] hosts = { args[0], args[1] };
		String[] files = { args[2], args[3] };

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
		String hostname, command;

		// Standard output and error of remote process
		BufferedReader stdout = null, stderr = null;

		public FilterWorker(String hostname, String filename) {
			this.hostname = hostname;
			this.command = String.format("%s %s %s %s", SSH, hostname, CMD,
					filename);
		}

		@Override
		public void run() {
			System.out.println("About to run: " + this.command);
			exec();
			process();
		}

		private void exec() {
			try {
				Process p = Runtime.getRuntime().exec(this.command);
				stdout = new BufferedReader(new InputStreamReader(
						p.getInputStream()));
				stderr = new BufferedReader(new InputStreamReader(
						p.getErrorStream()));
			} catch (IOException e) {
				System.err.println("ERROR while executing: " + this.command);
				e.printStackTrace();
			}
		}

		private void process() {
			String line = null;
			try {
				if (SSHDEBUG)
					while ((line = stderr.readLine()) != null)
						System.err.printf("%s: %s\n", this.hostname, line);
				
				while ((line = stdout.readLine()) != null) {
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
