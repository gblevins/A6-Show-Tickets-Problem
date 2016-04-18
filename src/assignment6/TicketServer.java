package assignment6;

//import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TicketServer
{
	static int PORT = 2222;

	// no matter how many concurrent requests,
	// do not have more than three servers running concurrently
	final static int MAXPARALLELTHREADS = 3;
	static int threadsStarted = 0;

	public static void start(int portNumber, String officeName) throws IOException
	{
		PORT = portNumber;
		Runnable serverThread = new ThreadedTicketServer(officeName);
		Thread t = new Thread(serverThread);
		threadsStarted += 1;
		if (threadsStarted <= MAXPARALLELTHREADS) {
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
	String threadname;
	//String testcase;
	//TicketClient sc;

	static ConcertHall concertHall;
	
	ThreadedTicketServer(String officeName)
	{
		threadname = officeName;
		concertHall = new ConcertHall();
	}

	public void run()
	{
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(TicketServer.PORT);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		while(true)
		{
			//ServerSocket serverSocket;
			try {
				//serverSocket = new ServerSocket(TicketServer.PORT);
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
		
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
