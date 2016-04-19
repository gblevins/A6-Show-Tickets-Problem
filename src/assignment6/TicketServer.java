package assignment6;

/*
 * represents a booth that is selling tickets to clients
 * contains access to the concert hall so that it can sell the tickets
 * booth opens up and starts accepting clients one at a time
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TicketServer
{
	// no matter how many concurrent requests,
	// do not have more than three servers running concurrently
	final static int MAXPARALLELTHREADS = 3;
	static int threadsStarted = 0;
	// variable for the JUnit to access to see when out of tickets 
	static boolean hasTickets = true;

	// constructs and starts thread, also makes sure that there aren't too many servers up
	public static void start(int portNumber, String officeName) throws IOException
	{
		threadsStarted += 1;
		Runnable serverThread = new ThreadedTicketServer(officeName, portNumber);
		Thread t = new Thread(serverThread);
		if (threadsStarted <= MAXPARALLELTHREADS) {
			t.start();
		}
		else {
			System.err.println("Attempt to initialize too many servers blocked.");
		}
	}
}

// the actual thread that runs and does work
class ThreadedTicketServer implements Runnable
{
	// name of connection and server
	String hostname = "127.0.0.1";
	String threadname;
	int PORT;

	// the hall is static so that it is the same across servers
	static ConcertHall concertHall;
	
	// constructors names the server and initializes the concert hall, if there is already
	// a server up it does not reinitialize the hall
	ThreadedTicketServer(String officeName, int portNumber)
	{
		PORT = portNumber;
		threadname = officeName;
		if (TicketServer.threadsStarted == 1) {
			//System.out.println("The first server is initializing the Concert Hall.");
			concertHall = new ConcertHall();
		}
		else {
			//System.out.println("A server is being initialized but the Hall has already been initialized.");
		}
	}

	// where the server waits to accept a client at a time and sell each a ticket until
	// there are no more tickets
	public void run()
	{
		// start a server
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		// keep selling tickets until there are none
		while(true)
		{
			try {
				/*
				 * Many print statements and I/O connections have been commented out, 
				 * they were used for testing and understanding, they can be uncommented
				 * if preferred
				 */
				//System.out.println(threadname + " is ready for a customer.");
				Socket clientSocket = serverSocket.accept();
				
				// accept and print the name of the customer that is at the booth
				DataInputStream in = new DataInputStream(clientSocket.getInputStream());
				String customerName = new String(in.readUTF());
				//System.out.println(customerName + " is at " + threadname + ".");

				Seat bestSeat = concertHall.bestAvailableSeat();
				if (bestSeat == null) {
					DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
					out.writeUTF("Sorry.");
					System.err.println("Out of seats. " + threadname + " is closing.");
					TicketServer.hasTickets = false;
					clientSocket.close();
					break;
				}

				//System.out.println("Hi " + customerName + ", the best seat available is: Row "
				//					+ bestSeat.seatRow + ", Chair " + bestSeat.seatNum.toString());

				// give the customer the seat they will buy
				DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
				out.writeUTF(customerName + " bought the seat Row " + bestSeat.seatRow + ", Chair " + bestSeat.seatNum.toString() + " from " + threadname);

				clientSocket.close();

			} catch (IOException e) {
				System.err.println("Error.");
				e.printStackTrace();
			}
		}
		
		// close the server after all tickets have been sold
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
