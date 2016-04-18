package assignment6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TicketServer
{
	static int PORT = 2222;

	// no matter how many concurrent requests,
	// do not have more than three servers running concurrently
	final static int MAXPARALLELTHREADS = 3;
	static int threadsStarted = 0;

	public static void start(int portNumber) throws IOException
	{
		PORT = portNumber;
		Runnable serverThread = new ThreadedTicketServer();
		Thread t = new Thread(serverThread);
		threadsStarted += 1;
		if (threadsStarted >= MAXPARALLELTHREADS) {
			t.start();
		}
		else {
			System.err.println("Attempt to initialize too many servers.");
		}
	}
}

class ThreadedTicketServer implements Runnable
{
	String hostname = "127.0.0.1";
	String threadname = "X";
	String testcase;
	TicketClient sc;

	static ConcertHall concertHall;

	public void init()
	{
		concertHall = new ConcertHall();
	}

	public void run()
	{
		while(true)
		{
			ServerSocket serverSocket;
			try {
				serverSocket = new ServerSocket(TicketServer.PORT);
				Socket clientSocket = serverSocket.accept();
				// if (getBestSeat() == null) {
				// 		system.err.println("Out of seats." + boxOfficeName + "is closing.");
				//		break;
				// }
				// now one client is talking to the server 
				// this is where we can find the best seat in the hall and 
				// give that seat to the client that the server is talking to 
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
