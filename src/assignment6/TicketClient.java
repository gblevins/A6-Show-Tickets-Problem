/*
 * Assignment 6: Threaded Tickets
 * Names: Malvika Gupta and Garret Blevins
 * UTEID: mg42972 and geb628
 * Lab Section: Thursday 2 pm
 */
package assignment6;

/*
 * the client represents the customer waiting in line to get up to a booth
 */

import java.io.DataInputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.io.DataOutputStream;
import java.io.InputStream;

class ThreadedTicketClient implements Runnable
{
	//counts how many actual tickets have been bought
	static int buyCount =0;
	// name of the connection and client
	String hostname = "127.0.0.1";
	// the name of the line
	String threadname;
	// the number of the current customer in line
	Integer customer = 1;
	// port number of the server the the client is trying to connect to
	int PORT;
	
	// constructor names the client
	public ThreadedTicketClient(String threadName, int portNumber)
	{
		PORT = portNumber;
		threadname = threadName;
	}

	// where the client requests a ticket and waits for the server to respond
	public void run()
	{
		while (TicketServer.hasTickets)
		{
		System.out.flush();
		try
		{
			/*
			 * Many print statements and I/O connections have been commented out, 
			 * they were used for testing and understanding, they can be uncommented
			 * if preferred
			 */
			//System.out.println(threadname + " is waiting for a booth to accept them.");
			Socket client = new Socket(hostname, PORT);

			// send the name of the customer to the office
			OutputStream outToServer = client.getOutputStream();
			DataOutputStream out = new DataOutputStream(outToServer);
			out.writeUTF(threadname + " Customer " + customer.toString());
			customer++;
			InputStream inFromServer = client.getInputStream();
			DataInputStream in = new DataInputStream(inFromServer);
			String input = in.readUTF();
			if (input.equals("Sorry.")){
				client.close();
			}
			else {
				System.out.println(input + ".");
				buyCount++;
				client.close();
			}

		} catch (ConnectException e1) {
			System.err.println("Connection closed. Out of tickets."); }
		
			catch (SocketException e2) {
				System.err.println("Connection closed. Out of tickets."); }
			catch (Exception e3) {
				e3.printStackTrace();
			}
		}
	}
}

public class TicketClient
{
	ThreadedTicketClient tc;
	
	// constructor creates the client thread
	TicketClient(String threadName, int portNumber)
	{
		tc = new ThreadedTicketClient(threadName, portNumber);
	}

	// the client requests to buy a ticket
	void requestTicket()
	{
		tc.run();
	}

	// sleep the client thread
	void sleep()
	{
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
