package uebung1.aufgabe1;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

/**
 * @author xxx & xxx (Gruppe xxx)
 */
public class ConcurrentEventTest {
	ConcurrentEvent events;

	@Before
	public void setUp() throws Exception {
		this.events = new ConcurrentEvent();
	}

	@Test
	public void testRegister() {
		Event.Ticket ticket = this.events.register(System.out);
		if (!this.events.map.containsKey(ticket))
			fail("Ticket not in list");
	}

	@Test
	public void testCancel() {
		Event.Ticket ticket = this.events.register(System.out);
		this.events.cancel(ticket);

		if (this.events.map.containsKey(ticket))
			fail("Ticket in list");
	}

	@Test
	public void testPropagate() {
		final LinkedList<Integer> buffer = new LinkedList<Integer>();

		OutputStream os = new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				buffer.add(b);
			}
		};

		String testStr = "ASD";
		this.events.register(os);
		this.events.propagate(testStr, null);

		byte[] tmp = testStr.getBytes();
		for (int i = 0; i < tmp.length; i++) {
			System.out.println(tmp[i]);
			System.out.println(buffer.get(i));
			if (buffer.get(i) != (tmp[i]))
				fail("wrong data");
		}

	}

}
