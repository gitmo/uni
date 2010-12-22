package uebung1.aufgabe1;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author xxx & xxx (Gruppe xxx)
 */
public class ConcurrentEvent implements Event {
	/**
	 * Internal representation: (Ticket,OutputStream) as key/value
	 */
	Map<Ticket, OutputStream> map = new HashMap<Ticket, OutputStream>();

	/**
	 * Löscht ein Ticket von der Event-Liste
	 */
	public synchronized void cancel(Ticket t) {
		if (this.map.containsKey(t))
			this.map.remove(t);
	}

	/**
	 * Sendet an alle angemeldeten Outputstreams, ausser dem Sender, den String
	 * s
	 */
	public synchronized void propagate(String s, Ticket t) {
		Set<Ticket> keys = this.map.keySet();
		for (Ticket key : keys) {
			if (key != t) { // Write to all registered except self.
				PrintStream ps = new PrintStream(this.map.get(key));
				ps.append(s);
				ps.flush();
			}
		}
	}

	/**
	 * Registriert einen neuen OutputStream
	 */
	public synchronized Ticket register(OutputStream s) {
		Ticket ticket = new Ticket() {
		};
		this.map.put(ticket, s);
		return ticket;
	}

}
