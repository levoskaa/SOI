import java.util.AbstractMap;
import java.util.Map;
import java.util.UUID;

import javax.jws.WebService;

import seatreservation.ArrayOfSeat;
import seatreservation.CinemaException;
import seatreservation.ICinema;
import seatreservation.ICinemaBuyCinemaException;
import seatreservation.ICinemaGetAllSeatsCinemaException;
import seatreservation.ICinemaGetSeatStatusCinemaException;
import seatreservation.ICinemaInitCinemaException;
import seatreservation.ICinemaLockCinemaException;
import seatreservation.ICinemaReserveCinemaException;
import seatreservation.ICinemaUnlockCinemaException;
import seatreservation.Seat;
import seatreservation.SeatStatus;

@WebService(
	name = "CinemaService",
	portName = "ICinema_HttpSoap11_Port",
	targetNamespace = "http://www.iit.bme.hu/soi/hw/SeatReservation",
	endpointInterface = "seatreservation.ICinema",
	wsdlLocation = "WEB-INF/wsdl/SeatReservation.wsdl")
public class Cinema implements ICinema {
    private static Seat[][] seats;
    private static SeatStatus[][] seatStatuses;
    private static Map<String, Seat[]> locks;

    @Override
    public void init(int rows, int columns) throws ICinemaInitCinemaException {
	CinemaException ce = new CinemaException();
	if (rows < 1 || rows > 26) {
	    ce.setErrorCode(400);
	    ce.setErrorMessage("Number of rows must be between 1 and 26");
	    throw new ICinemaInitCinemaException(ce.getErrorMessage(), ce);
	}
	if (columns < 1 || columns > 100) {
	    ce.setErrorCode(400);
	    ce.setErrorMessage("Number of columns must be between 1 and 100");
	    throw new ICinemaInitCinemaException(ce.getErrorMessage(), ce);
	}
	seats = new Seat[rows][columns];
	Seat seat;
	char A = 'A';
	for (int i = 0; i < rows; ++i) {
	    for (int j = 0; j < columns; ++j) {
		seat = new Seat();
		seat.setRow(String.valueOf((char) (A + i)));
		seat.setColumn(String.valueOf(j + 1));
		seats[i][j] = seat;
	    }
	}
	seatStatuses = new SeatStatus[rows][columns];
	for (int i = 0; i < rows; ++i) {
	    for (int j = 0; j < columns; ++j) {
		seatStatuses[i][j] = SeatStatus.FREE;
	    }
	}
    }

    @Override
    public ArrayOfSeat getAllSeats() throws ICinemaGetAllSeatsCinemaException {
	ArrayOfSeat seatArray = new ArrayOfSeat();
	for (int i = 0; i < seats.length; ++i) {
	    for (int j = 0; j < seats[0].length; ++j) {
		seatArray.getSeat().add(seats[i][j]);
	    }
	}
	return seatArray;
    }

    @Override
    public SeatStatus getSeatStatus(Seat seat)
	    throws ICinemaGetSeatStatusCinemaException {
	Map.Entry<Integer, Integer> seatIndex = getSeatIndex(seat);
	return seatStatuses[seatIndex.getKey()][seatIndex.getValue()];
    }

    @Override
    public String lock(Seat seat, int count) throws ICinemaLockCinemaException {
	Map.Entry<Integer, Integer> seatIndex;
	CinemaException ce;
	try {
	    seatIndex = getSeatIndex(seat);
	} catch (ICinemaGetSeatStatusCinemaException e) {
	    ce = new CinemaException();
	    ce.setErrorCode(400);
	    ce.setErrorMessage(e.getFaultInfo().getErrorMessage());
	    throw new ICinemaLockCinemaException(e.getMessage(), ce);
	}
	int row = seatIndex.getKey();
	int column = seatIndex.getValue();
	Seat[] seatsToBeLocked = new Seat[count];
	for (int i = 0; i < count; ++i) {
	    boolean outOfBounds = column + i >= seatStatuses[0].length;
	    boolean isSeatFree = seatStatuses[row][column
		    + i] == SeatStatus.FREE;
	    if (outOfBounds || !isSeatFree) {
		ce = new CinemaException();
		ce.setErrorCode(400);
		ce.setErrorMessage("Not enough free seats in range");
		throw new ICinemaLockCinemaException(ce.getErrorMessage(), ce);
	    }
	    seatsToBeLocked[i] = seats[row][column + i];
	}
	for (int i = 0; i < count; ++i) {
	    seatStatuses[row][column + i] = SeatStatus.LOCKED;
	}
	String lockId = UUID.randomUUID().toString();
	locks.put(lockId, seatsToBeLocked);
	return lockId;
    }

    @Override
    public void unlock(String lockId) throws ICinemaUnlockCinemaException {
	// TODO Auto-generated method stub

    }

    @Override
    public void reserve(String lockId) throws ICinemaReserveCinemaException {
	// TODO Auto-generated method stub

    }

    @Override
    public void buy(String lockId) throws ICinemaBuyCinemaException {
	// TODO Auto-generated method stub

    }

    private Map.Entry<Integer, Integer> getSeatIndex(Seat seat)
	    throws ICinemaGetSeatStatusCinemaException {
	int row;
	int column;
	try {
	    row = seat.getRow().charAt(0) - 'A';
	    column = Integer.parseInt(seat.getColumn());
	} catch (Exception e) {
	    CinemaException ce = new CinemaException();
	    ce.setErrorCode(400);
	    ce.setErrorMessage("Seat position invalid");
	    throw new ICinemaGetSeatStatusCinemaException(ce.getErrorMessage(),
		    ce);
	}
	return new AbstractMap.SimpleEntry<>(row, column);
    }

}
