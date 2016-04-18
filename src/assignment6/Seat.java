package assignment6;

public class Seat
{
	int seatNum;
	String seatRow;
	boolean reserved;

	Seat(int newSeatNum, String newSeatRow)
	{
		seatNum = newSeatNum;
		seatRow = new String(newSeatRow);
		reserved = false;
	}
}
