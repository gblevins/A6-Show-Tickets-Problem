package assignment6;

/*
 * testing for the servers, clients, and concert hall data type
 * starts up servers (booths) and puts clients (customers) in line
 * to get tickets
 * 
 * WARNING: always tell the servers and clients what there port numbers are
 */

import static org.junit.Assert.fail;
import org.junit.Test;

public class TestTicketOffice {
	// this test ensures both that the ordering of the seats is correct and that the
	// get best seat method is working properly
	//@Test
	public void concertHallTest()
	{
		System.out.println("Starting Concert Hall Test. Printing from best ticket to worst ticket:");
		
		ConcertHall concertHall = new ConcertHall();
		Seat bestSeat = concertHall.bestAvailableSeat();
		
		while (bestSeat != null)
		{
			System.out.println("Row: " + bestSeat.seatRow + " Number: " + bestSeat.seatNum.toString());
			bestSeat = concertHall.bestAvailableSeat();
		}
		
		System.out.println("Out of seats in the concert hall.");
	}

	// simply starts a server and a client and sees if one transaction can take place
	//@Test
	public void basicServerTest()
	{
		System.out.println("Starting basic server and request test.");

		try {
			TicketServer.start(16789, new String("Office A"));
		} catch (Exception e) {
			fail();
		}
		TicketClient client = new TicketClient("Customer 1", 16792);
		client.requestTicket();
	}

	// now tries for two transactions
	//@Test
	public void testServerCachedHardInstance()
	{
		System.out.println("Starting basic server test and double request test.");
		
		try {
			TicketServer.start(16790, new String("Office A"));
		} catch (Exception e) {
			fail();
		}
		TicketClient client1 = new TicketClient("Customer 1", 16792);
		TicketClient client2 = new TicketClient("Customer 2", 16792);
		client1.requestTicket();
		client2.requestTicket();
	}

	// three clients now
	//@Test
	public void twoNonConcurrentServerTest()
	{
		try {
			TicketServer.start(16791, new String("Office A"));
		} catch (Exception e) {
			fail();
		}
		TicketClient c1 = new TicketClient("Customer 1", 16792);
		TicketClient c2 = new TicketClient("Customer 2", 16792);
		TicketClient c3 = new TicketClient("Customer 3", 16792);
		c1.requestTicket();
		c2.requestTicket();
		c3.requestTicket();
	}

	// the clients are threaded to wait for each to finish before test finishes but threads
	// execute seemingly randomly
	//@Test
	public void twoConcurrentServerTest()
	{
		try {
			TicketServer.start(16792, new String("Office A"));
		} catch (Exception e) {
			fail();
		}
		final TicketClient c1 = new TicketClient("Customer 1", 16792);
		final TicketClient c2 = new TicketClient("Customer 2", 16792);
		final TicketClient c3 = new TicketClient("Customer 3", 16792);
		Thread t1 = new Thread() {
			public void run() {
				c1.requestTicket();
			}
		};
		Thread t2 = new Thread() {
			public void run() {
				c2.requestTicket();
			}
		};
		Thread t3 = new Thread() {
			public void run() {
				c3.requestTicket();
			}
		};
		t1.start();
		t2.start();
		t3.start();
		try {
			t1.join();
			t2.join();
			t3.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void twoServersTest()
	{
		System.out.println("Starting test with two servers.");

		try {
			TicketServer.start(16792, new String("Office A"));
			TicketServer.start(16793, new String("Office B"));
		} catch (Exception e) {
			fail();
		}
		final TicketClient c1 = new TicketClient("Customer 1", 16792);
		final TicketClient c2 = new TicketClient("Customer 2", 16793);
		final TicketClient c3 = new TicketClient("Customer 3", 16793);
		Thread t1 = new Thread() {
			public void run() {
				c1.requestTicket();
			}
		};
		Thread t2 = new Thread() {
			public void run() {
				c2.requestTicket();
			}
		};
		Thread t3 = new Thread() {
			public void run() {
				c3.requestTicket();
			}
		};
		t1.start();
		t2.start();
		t3.start();
		try {
			t1.join();
			t2.join();
			t3.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
