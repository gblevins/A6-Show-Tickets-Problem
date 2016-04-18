package assignment6;

public class Seat
{
	Integer seatNum;
	char seatRow;
	boolean reserved;

	Seat(int newSeatNum, char newSeatRow)
	{
		seatNum = newSeatNum;
		seatRow = newSeatRow;
		reserved = false;
	}
}
