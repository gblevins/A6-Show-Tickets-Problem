package assignment6;

/*
 * the client represents the customer waiting in line to get up to a booth
 */

import java.io.DataInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.io.DataOutputStream;
import java.io.InputStream;

class ThreadedTicketClient implements Runnable
{
	// name of the connection and client
	String hostname = "127.0.0.1";
	String threadname;
	
	// constructor names the client
	public ThreadedTicketClient(String threadName)
	{
		threadname = threadName;
	}

	// where the client requests a ticket and waits for the server to respond
	public void run()
	{
		System.out.flush();
		try
		{
			System.out.println(threadname + " is attempting to connect to a server...");
			Socket client = new Socket(hostname, TicketServer.PORT);
			System.out.println(threadname + " just connected to " + client.getRemoteSocketAddress());
			OutputStream outToServer = client.getOutputStream();
			DataOutputStream out = new DataOutputStream(outToServer);
			out.writeUTF(threadname + " says hello from " + client.getLocalSocketAddress());
			InputStream inFromServer = client.getInputStream();
			DataInputStream in = new DataInputStream(inFromServer);
			System.out.println("Server says " + in.readUTF());
			client.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

public class TicketClient
{
	ThreadedTicketClient tc;
	
	// constructor creates the client thread
	TicketClient(String threadName)
	{
		tc = new ThreadedTicketClient(threadName);
	}

	// the client requests to buy a ticket
	void requestTicket()
	{
		tc.run();
		System.out.println(tc.threadname + " got one ticket");
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
