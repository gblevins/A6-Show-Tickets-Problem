package assignment6;

import java.util.ArrayList;

public class ConcertHall
{
	ArrayList<Seat> seats1;
	ArrayList<Seat> seats2;
	ArrayList<Seat> seats3;
	ArrayList<Seat> seats4;
	
	ConcertHall()
	{
		seats1 = new ArrayList<Seat>();
		seats2 = new ArrayList<Seat>();
		seats3 = new ArrayList<Seat>();
		seats4 = new ArrayList<Seat>();
		
		int i, k;
		for(i = 0; i < 13; i++)
		{
			for(k = 0; k < 14; k++)
			{
				seats1.add(new Seat(108 + k, new String("A" + i)));
			}
		}
		
		for(i = 0; i < 13; i++)
		{
			for(k = 0; k < 7; k++)
			{
				seats2.add(new Seat(101 + k, new String("A" + i)));
			}
		}
		for(i = 0; i < 13; i++)
		{
			for(k = 0; k < 7; k++)
			{
				seats2.add(new Seat(122 + k, new String("A" + i)));
			}
		}
		
		for(i = 0; i < 13; i++)
		{
			for(k = 0; k < 14; k++)
			{
				seats3.add(new Seat(108 + k, new String("N" + 1)));
			}
		}
		
		for(i = 0; i < 13; i++)
		{
			for(k = 0; k < 7; k++)
			{
				seats4.add(new Seat(101 + k, new String("N" + i)));
			}
		}
		for(i = 0; i < 13; i++)
		{
			for(k = 0; k < 7; k++)
			{
				seats4.add(new Seat(122 + k, new String("N" + i)));
			}
		}
	}
	
	public int getSeatsNum()
	{
		return seats1.size() + seats2.size() + seats3.size() + seats4.size();
	}
}
