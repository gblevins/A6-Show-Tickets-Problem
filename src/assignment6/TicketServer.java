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
	// the port number is used to request connections
	static int PORT = 2222;
	// no matter how many concurrent requests,
	// do not have more than three servers running concurrently
	final static int MAXPARALLELTHREADS = 3;
	static int threadsStarted = 0;

	// constructs and starts thread, also makes sure that there aren't too many servers up
	public static void start(int portNumber, String officeName) throws IOException
	{
		PORT = portNumber;
		threadsStarted += 1;
		Runnable serverThread = new ThreadedTicketServer(officeName);
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

	// the hall is static so that it is the same across servers
	static ConcertHall concertHall;
	
	// constructors names the server and initializes the concert hall, if there is already
	// a server up it does not reinitialize the hall
	ThreadedTicketServer(String officeName)
	{
		threadname = officeName;
		if (TicketServer.threadsStarted == 1) {
			System.out.println("The first server is initializing the Concert Hall.");
			concertHall = new ConcertHall();
		}
		else {
			System.out.println("A server is being initialized but the Hall has already been initialized.");
		}
	}

	// where the server waits to accept a client at a time and sell each a ticket until
	// there are no more tickets
	public void run()
	{
		// start a server
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(TicketServer.PORT);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		// keep selling tickets until there are none
		while(true)
		{
			try {
				System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
				Socket clientSocket = serverSocket.accept();
				System.out.println(threadname + " just connected to " + clientSocket.getRemoteSocketAddress());
				
				DataInputStream in = new DataInputStream(clientSocket.getInputStream());
				System.out.println(in.readUTF());

				Seat bestSeat = concertHall.bestAvailableSeat();
				if (bestSeat == null) {
					System.err.println("Out of seats. " + threadname + " is closing.");
					break;
				}

				System.out.println(threadname + " is selling a ticket.");
				System.out.println("The best seat available is: Row " + bestSeat.seatRow + ", Chair " + bestSeat.seatNum.toString());
				
				DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
				out.writeUTF(threadname + " says thank you for connecting to " + clientSocket.getLocalSocketAddress() + "\nGoodbye!");
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
