package assignment6;

import java.util.ArrayList;
import java.util.Iterator;

public class ConcertHall
{
	ArrayList<Seat> seats;

	ConcertHall()
	{
		ArrayList<Seat> seats1 = new ArrayList<Seat>();
		ArrayList<Seat> seats2 = new ArrayList<Seat>();
		ArrayList<Seat> seats3 = new ArrayList<Seat>();
		ArrayList<Seat> seats4 = new ArrayList<Seat>();

		int i, k;
		for(i = 0; i < 13; i++)
		{
			for(k = 0; k < 14; k++)
			{
				seats1.add(new Seat(108 + k, (char) (65 + i)));
			}
		}
		
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
		
		for(i = 0; i < 13; i++)
		{
			for(k = 0; k < 14; k++)
			{
				seats3.add(new Seat(108 + k, (char) (78 + i)));
			}
		}
		
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

		seats = new ArrayList<Seat>();

		seats.addAll(seats1);
		seats.addAll(seats2);
		seats.addAll(seats3);
		seats.addAll(seats4);
	}

	public synchronized Seat bestAvailableSeat()
	{
		Seat bestSeat = null;
		
		Iterator<Seat> it = seats.iterator();
		
		bestSeat = it.next();
		
		while (it.hasNext())
		{
			if (bestSeat.reserved == false)
			{
				bestSeat.reserved = true;
				return bestSeat;
			}
			
			bestSeat = it.next();
		}
		
		return bestSeat = null;
	}
	
	public int getSeatsNum()
	{
		return seats.size();
	}
}
