/*
 * Assignment 6: Threaded Tickets
 * Names: Malvika Gupta and Garret Blevins
 * UTEID: mg42972 and geb628
 * Lab Section: Thursday 2 pm
 */


package assignment6;

/*
 * testing for the servers, clients, and concert hall data type
 * starts up servers (booths) and puts clients (customers) in line
 * to get tickets
 * 
 * WARNING: always tell the servers and clients what there port numbers are
 */

import static org.junit.Assert.fail;
import java.awt.List;
import java.util.ArrayList;
import java.util.Iterator;
//import java.util.AbstractQueue;
//import java.util.ArrayList;
import java.util.LinkedList;
//import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
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
		System.out.println("The number of seats in the hall is: " + concertHall.seats.size());
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
	// this test runs with two servers (so two ticket offices)
	//@Test
	public void TwoServersTest()
	{
		System.out.println("Starting test with two servers.");

		try {
			TicketServer.start(16792, new String("Office A"));
			TicketServer.start(16793, new String("Office B"));
		} catch (Exception e) {
			fail();
		}

		// start three clients to buy tickets
		TicketClient c1 = new TicketClient("Line 1", 16792);
		TicketClient c2 = new TicketClient("Line 2", 16793);

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

		t1.start();
		t2.start();

		try {
			t1.join();
			t2.join();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.err.println("All of the tickets have been sold. Exiting.");
		System.err.println("Tickets sold: " + ThreadedTicketClient.buyCount);
		System.exit(0);
	}	

	// the clients are also threaded to wait for each to finish before test finishes but threads
	// execute seemingly randomly	
	// this test runs with three servers (so three ticket offices)
	@Test
	public void ThreeServersTest()
	{
		System.out.println("Starting test with three servers.");

		try {
			TicketServer.start(16792, new String("Office A"));
			TicketServer.start(16793, new String("Office B"));
			TicketServer.start(16794, new String("Office C"));
		} catch (Exception e) {
			fail();
		}

		// start three clients to buy tickets
		TicketClient c1 = new TicketClient("Line 1", 16792);
		TicketClient c2 = new TicketClient("Line 2", 16793);
		TicketClient c3 = new TicketClient("Line 3", 16794);

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

		System.err.println("All of the tickets have been sold. Exiting.");
		System.err.println("Tickets sold: " + ThreadedTicketClient.buyCount);
		System.exit(0);
	}
}