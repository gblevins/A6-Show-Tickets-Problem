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
			out.writeUTF(threadname);

			InputStream inFromServer = client.getInputStream();
			DataInputStream in = new DataInputStream(inFromServer);
			String input = in.readUTF();
			if (input.equals("Sorry.")){
				client.close();
			}
			else {
				System.out.println(input + ".");
				client.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
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
