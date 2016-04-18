package assignment6;

//import java.io.BufferedReader;
import java.io.DataInputStream;
//import java.io.InputStreamReader;
import java.io.OutputStream;
//import java.io.PrintWriter;
//import java.io.PrintWriter;
import java.net.Socket;
import java.io.DataOutputStream;
//import org.omg.CORBA.portable.OutputStream;
import java.io.InputStream;

class ThreadedTicketClient implements Runnable
{
	String hostname = "127.0.0.1";
	//String threadname = "X";
	String threadname;
	//TicketClient sc;

//	public ThreadedTicketClient(TicketClient sc, String hostname, String threadname)
//	{
//		this.sc = sc;
//		this.hostname = hostname;
//		this.threadname = threadname;
//	}
	
	public ThreadedTicketClient(String threadName)
	{
		threadname = threadName;
	}

	public void run()
	{
		System.out.flush();
		try
		{
			Socket client = new Socket(hostname, TicketServer.PORT);
			OutputStream outToServer = client.getOutputStream();
			DataOutputStream out = new DataOutputStream(outToServer);
			InputStream inFromServer = client.getInputStream();
			DataInputStream in = new DataInputStream(inFromServer);
			System.out.println("Server says " + in.readUTF());
			client.close();
			
			//PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
			//BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
			
			//System.out.println(threadname + " is buying a ticket from " + in.readLine());
			
			//echoSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

public class TicketClient
{
	ThreadedTicketClient tc;
	//String result = "dummy";
	//String hostName = "";
	//String threadName = "";
	
//	TicketClient(String hostname, String threadname)
//	{
//		//tc = new ThreadedTicketClient(this, hostname, threadname);
//		tc = new ThreadedTicketClient(threadname);
//		// okay idk look at this tomorrow
//		hostName = hostname;
//		threadName = threadname;
//	}
	
	TicketClient(String threadName)
	{
		tc = new ThreadedTicketClient(threadName);
	}

//	TicketClient(String name) {
//		this("localhost", name);
//	}

//	TicketClient() {
//		this("localhost", "unnamed client");
//	}

	void requestTicket()
	{
		tc.run();
		//System.out.println(hostName + "," + threadName + " got one ticket");
		System.out.println(tc.threadname + " got one ticket");
	}

	void sleep()
	{
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
