package assignment6;

/*
 * this class holds every seat in the concert hall in an array list such that the best
 * seat is at the beginning of the list and the worst seat is at the end
 */

import java.util.ArrayList;
import java.util.Iterator;

public class ConcertHall
{
	// the seats in the array list are ordered from best seat to worst seat
	ArrayList<Seat> seats;

	// the constructor builds the array in a brute force method, building the four zones
	// of desirability and then putting them all in an array list
	ConcertHall()
	{
		// 4 zones in decreasing desirability
		ArrayList<Seat> seats1 = new ArrayList<Seat>();
		ArrayList<Seat> seats2 = new ArrayList<Seat>();
		ArrayList<Seat> seats3 = new ArrayList<Seat>();
		ArrayList<Seat> seats4 = new ArrayList<Seat>();

		// initialize zone 1: the middle front
		int i, k;
		for(i = 0; i < 13; i++)
		{
			for(k = 0; k < 14; k++)
			{
				seats1.add(new Seat(108 + k, (char) (65 + i)));
			}
		}
		
		// initialize zone 2: the front sides
		for(i = 0; i < 13; i++)
		{
			for(k = 0; k < 7; k++)
			{
				seats2.add(new Seat(101 + k, (char) (65 + i)));
			}
		}
		for(i = 0; i < 13; i++)
		{
			for(k = 0; k < 7; k++)
			{
				seats2.add(new Seat(122 + k, (char) (65 + i)));
			}
		}
		
		// initialize zone 3: the back middle
		for(i = 0; i < 13; i++)
		{
			for(k = 0; k < 14; k++)
			{
				seats3.add(new Seat(108 + k, (char) (78 + i)));
			}
		}
		
		// initialize zone 4: the back sides
		for(i = 0; i < 13; i++)
		{
			for(k = 0; k < 7; k++)
			{
				seats4.add(new Seat(101 + k, (char) (78 + i)));
			}
		}
		for(i = 0; i < 13; i++)
		{
			for(k = 0; k < 7; k++)
			{
				seats4.add(new Seat(122 + k, (char) (78 + i)));
			}
		}

		// add all of the zones to one array
		seats = new ArrayList<Seat>();

		seats.addAll(seats1);
		seats.addAll(seats2);
		seats.addAll(seats3);
		seats.addAll(seats4);
	}

	// called  by the server to sell a client a ticket, returns the best ticket
	// available or null if no tickets are left
	// synchronized so that multiple servers cannot try and sell the same ticket at the same time
	public synchronized Seat bestAvailableSeat()
	{
		
		for(Seat bestSeat:seats)
		{
			if (bestSeat.reserved == false)
			{
				bestSeat.reserved = true;
				return bestSeat;
			}
		}
		return null;	
	}
}
