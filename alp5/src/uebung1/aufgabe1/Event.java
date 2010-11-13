package uebung1.aufgabe1;

import java.io.OutputStream;

/**
 * @author xxx & xxx (Gruppe xxx)
 */
public interface Event {
	interface Ticket {
	}

	// register s with newly created Ticket
	Ticket register(OutputStream s);

	// cancel registration associated with t
	void cancel(Ticket t);

	// write event notification s to all registered OutputStreams,
	// except the stream associated with t
	void propagate(String s, Ticket t);
}
