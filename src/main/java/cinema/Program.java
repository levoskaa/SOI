package cinema;

import javax.xml.ws.BindingProvider;

import seatreservation.CinemaService;
import seatreservation.ICinema;
import seatreservation.Seat;

public class Program {
    public static void main(String[] args) {
	String url = args[0];
	String row = args[1].toUpperCase();
	String column = args[2];
	String operation = args[3].toUpperCase();

	CinemaService cinemaService = new CinemaService();
	ICinema cinema = cinemaService.getICinemaHttpSoap11Port();
	BindingProvider bp = (BindingProvider) cinema;
	bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
		url);

	Seat seat = new Seat();
	seat.setRow(row);
	seat.setColumn(column);
	String lockId;
	switch (operation) {
	case "LOCK":
	    try {
		lockId = cinema.lock(seat, 1);
		System.out.println("Lock successful, lockId = " + lockId);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	    break;
	case "RESERVE":
	    try {
		lockId = cinema.lock(seat, 1);
		cinema.reserve(lockId);
		System.out.println("Reserve successful");
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	    break;
	case "BUY":
	    try {
		lockId = cinema.lock(seat, 1);
		cinema.buy(lockId);
		System.out.println("Buy successful");
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	    break;
	}
    }
}
