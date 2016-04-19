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
	
	//@Test
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
	
	//@Test
	public void twoServersTestWithQueue()
	{
		System.out.println("Starting test with two servers.");

		try {
			TicketServer.start(16792, new String("Office A"));
			TicketServer.start(16793, new String("Office B"));
		} catch (Exception e) {
			fail();
		}
		//creates the queue and initializes variables
		Queue<TicketClient> queue = new LinkedList<TicketClient>();
		int totalCustomers = 0;
		int customerCount = ((int)(Math.random()*900))+100;
		totalCustomers = customerCount;
		int i =0;
		while(i < customerCount)
		{	
			String customerName = "Customer "+ ((Integer)(i+1)).toString();
			if(i % 2 == 0)
				queue.add(new TicketClient(customerName, 16792));
			else
				queue.add(new TicketClient(customerName, 16793));
			i++;
		}
		ArrayList<Thread> threads = new ArrayList<Thread>();
		
		while(TicketServer.hasTickets)
		{
			//queue.element().requestTicket();
			//queue.remove();
			if (queue.peek() != null)
			{
				TicketClient c = queue.remove();
				//ThreadedTicketClient t = c.tc;
				//t.run();
				//Thread th = c.tc;
				Thread t = new Thread() {
					public void run() {
						c.requestTicket();
					}
				};
				threads.add(t);
				t.start();
				//try {
				//	t.join();
				//} catch (Exception e) {
				//	e.printStackTrace();
				//}
			}

			//this makes sure that the queue never has less than 100 people
			//if it does, then
			if(customerCount <= 100)
			{
				System.out.println("More customers added");
				int addCustomer = ((int)(Math.random()*900))+100; 
				int j = 0;
				while(j < addCustomer)
				{
					String customerName = "Customer "+ ((Integer)(j+1+totalCustomers)).toString();
					if((j+totalCustomers)%2==0)
						queue.add(new TicketClient(customerName, 16792));
					else
						queue.add(new TicketClient(customerName, 16793));
					j++;
				}
				totalCustomers += addCustomer;
				customerCount += addCustomer;
			}
		}
		// join the threads so that they finish
		Iterator<Thread> it = threads.iterator();
		while (it.hasNext())
		{
			Thread th = it.next();
			try {
				th.join();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		System.err.println("All of the tickets have been sold. Exiting.");
		System.exit(0);
	}
	
	//@Test
	public void anotherTwoServersTestWithQueue()
	{
		System.out.println("Starting another test with two servers.");

		try {
			TicketServer.start(16792, new String("Office A"));
			TicketServer.start(16793, new String("Office B"));
		} catch (Exception e) {
			fail();
		}
		//creates the queue and initializes variables
		Queue<TicketClient> queue = new LinkedList<TicketClient>();
		int i =0;
		while(i < 800)
		{	
			String customerName = "Customer "+ ((Integer)(i+1)).toString();
			if(i % 2 == 0)
				queue.add(new TicketClient(customerName, 16792));
			else
				queue.add(new TicketClient(customerName, 16793));
			i++;
		}
		ArrayList<Thread> threads = new ArrayList<Thread>();
		
		while(TicketServer.hasTickets)
		{
			//queue.element().requestTicket();
			//queue.remove();
			if (queue.peek() != null)
			{
				TicketClient c = queue.remove();
				//ThreadedTicketClient t = c.tc;
				//t.run();
				//Thread th = c.tc;
				Thread t = new Thread() {
					public void run() {
						c.requestTicket();
					}
				};
				threads.add(t);
				t.start();
				//try {
				//	t.join();
				//} catch (Exception e) {
				//	e.printStackTrace();
				//}
			}

			//this makes sure that the queue never has less than 100 people
			//if it does, then
		}
		// join the threads so that they finish
		Iterator<Thread> it = threads.iterator();
		while (it.hasNext())
		{
			Thread th = it.next();
			try {
				th.join();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		System.err.println("All of the tickets have been sold. Exiting.");
		System.exit(0);
	}
	@Test
	public void ThreeServersTestWithQueue()
	{
		System.out.println("Starting another test with three servers.");

		try {
			TicketServer.start(16792, new String("Office A"));
			TicketServer.start(16793, new String("Office B"));
			TicketServer.start(16794, new String("Office C"));
		} catch (Exception e) {
			fail();
		}
		//creates the queue and initializes variables
		Queue<TicketClient> queue = new LinkedList<TicketClient>();
		int i =0;
		int totalCustomers = (int)(Math.random()*50+750);
		while(i < totalCustomers )
		{	
			int rand = (int)(Math.random()*100);
			String customerName = "Customer "+ ((Integer)(i+1)).toString();
			if(rand< 33)
			{
				queue.add(new TicketClient(customerName, 16792));
			}
			else if(rand<67)
			{
				queue.add(new TicketClient(customerName, 16793));
			}
				
			else
				queue.add(new TicketClient(customerName, 16794));
			i++;
		}
		ArrayList<Thread> threads = new ArrayList<Thread>();
		
		while(TicketServer.hasTickets)
		{
			//queue.element().requestTicket();
			//queue.remove();
			if (queue.peek() != null)
			{
				TicketClient c = queue.remove();
				//ThreadedTicketClient t = c.tc;
				//t.run();
				//Thread th = c.tc;
				Thread t = new Thread() {
					public void run() {
						c.requestTicket();
					}
				};
				threads.add(t);
				t.start();
				//try {
				//	t.join();
				//} catch (Exception e) {
				//	e.printStackTrace();
				//}
			}


		}
		// join the threads so that they finish
		Iterator<Thread> it = threads.iterator();
		while (it.hasNext())
		{
			Thread th = it.next();
			try {
				th.join();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("All of the tickets have been sold. Exiting."+ThreadedTicketClient.buyCount);
		System.exit(0);
	}

}
