/*
 * Assignment 6: Threaded Tickets
 * Names: Malvika Gupta and Garret Blevins
 * UTEID: mg42972 and geb628
 * Lab Section: Thursday 2 pm
 */
package assignment6;

/*
 * this class is used to hold the information for each seat in the concert hall
 */

public class Seat
{
	// the number of the seat
	Integer seatNum;
	// the row of the seat
	char seatRow;
	// whether or not the seat is taken
	boolean reserved;

	// constructor for the seat, needs number and row
	Seat(int newSeatNum, char newSeatRow)
	{
		seatNum = newSeatNum;
		seatRow = newSeatRow;
		reserved = false;
	}
}
