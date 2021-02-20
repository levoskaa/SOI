import java.util.AbstractMap;
import java.util.HashMap;
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
    private static Seat[][] seats = new Seat[0][0];
    private static SeatStatus[][] seatStatuses;
    private static Map<String, Seat[]> locks = new HashMap<String, Seat[]>();

    @Override
    public void init(int rows, int columns) throws ICinemaInitCinemaException {
	CinemaException ce;
	if (rows < 1 || rows > 26) {
	    ce = createCinemaException(
		    "Number of rows must be between 1 and 26", 400);
	    throw new ICinemaInitCinemaException(ce.getErrorMessage(), ce);
	}
	if (columns < 1 || columns > 100) {
	    ce = createCinemaException(
		    "Number of columns must be between 1 and 100", 400);
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
	Map.Entry<Integer, Integer> seatIndex;
	try {
	    seatIndex = tryGetSeatIndex(seat);
	} catch (IllegalArgumentException e) {
	    CinemaException ce = createCinemaException(e.getMessage(), 400);
	    throw new ICinemaGetSeatStatusCinemaException(ce.getErrorMessage(),
		    ce);
	}
	return seatStatuses[seatIndex.getKey()][seatIndex.getValue()];
    }

    @Override
    public String lock(Seat seat, int count) throws ICinemaLockCinemaException {
	CinemaException ce;
	if (count < 1) {
	    ce = createCinemaException("Number of seats must be greater than 0",
		    400);
	}
	Map.Entry<Integer, Integer> seatIndex;
	try {
	    seatIndex = tryGetSeatIndex(seat);
	} catch (IllegalArgumentException e) {
	    ce = createCinemaException(e.getMessage(), 400);
	    throw new ICinemaLockCinemaException(ce.getErrorMessage(), ce);
	}
	int row = seatIndex.getKey();
	int column = seatIndex.getValue();
	boolean outOfBounds = column + count - 1 >= seatStatuses[0].length;
	if (outOfBounds) {
	    ce = createCinemaException("Not enough free seats in range", 400);
	    throw new ICinemaLockCinemaException(ce.getErrorMessage(), ce);
	}
	Seat[] seatsToBeLocked = new Seat[count];
	for (int i = 0; i < count; ++i) {
	    boolean isSeatFree = seatStatuses[row][column
		    + i] == SeatStatus.FREE;
	    if (!isSeatFree) {
		ce = createCinemaException("Not enough free seats in range",
			400);
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
	CinemaException ce;
	if (!locks.containsKey(lockId)) {
	    ce = createCinemaException("No lock found with the given id", 404);
	    throw new ICinemaUnlockCinemaException(ce.getErrorMessage(), ce);
	}
	Seat[] lockedSeats = locks.get(lockId);
	Map.Entry<Integer, Integer> seatIndex;
	for (int i = 0; i < lockedSeats.length; ++i) {
	    seatIndex = getSeatIndex(lockedSeats[i]);
	    if (seatStatuses[seatIndex.getKey()][seatIndex
		    .getValue()] == SeatStatus.RESERVED
		    || seatStatuses[seatIndex.getKey()][seatIndex
			    .getValue()] == SeatStatus.SOLD) {
		ce = createCinemaException("Only locks can be unlocked", 400);
		throw new ICinemaUnlockCinemaException(ce.getErrorMessage(),
			ce);
	    }
	}
	for (int i = 0; i < lockedSeats.length; ++i) {
	    seatIndex = getSeatIndex(lockedSeats[i]);
	    seatStatuses[seatIndex.getKey()][seatIndex
		    .getValue()] = SeatStatus.FREE;
	}
	locks.remove(lockId);
    }

    @Override
    public void reserve(String lockId) throws ICinemaReserveCinemaException {
	CinemaException ce;
	if (!locks.containsKey(lockId)) {
	    ce = createCinemaException("No lock found with the given id", 404);
	    throw new ICinemaReserveCinemaException(ce.getErrorMessage(), ce);
	}
	Seat[] lockedSeats = locks.get(lockId);
	Map.Entry<Integer, Integer> seatIndex;
	for (int i = 0; i < lockedSeats.length; ++i) {
	    seatIndex = getSeatIndex(lockedSeats[i]);
	    seatStatuses[seatIndex.getKey()][seatIndex
		    .getValue()] = SeatStatus.RESERVED;
	}
    }

    @Override
    public void buy(String lockId) throws ICinemaBuyCinemaException {
	CinemaException ce;
	if (!locks.containsKey(lockId)) {
	    ce = createCinemaException("No lock found with the given id", 404);
	    throw new ICinemaBuyCinemaException(ce.getErrorMessage(), ce);
	}
	Seat[] lockedSeats = locks.get(lockId);
	Map.Entry<Integer, Integer> seatIndex;
	for (int i = 0; i < lockedSeats.length; ++i) {
	    seatIndex = getSeatIndex(lockedSeats[i]);
	    seatStatuses[seatIndex.getKey()][seatIndex
		    .getValue()] = SeatStatus.SOLD;
	}
    }

    private Map.Entry<Integer, Integer> getSeatIndex(Seat seat) {
	int row = seat.getRow().charAt(0) - 'A';
	int column = Integer.parseInt(seat.getColumn()) - 1;
	return new AbstractMap.SimpleEntry<>(row, column);
    }

    private Map.Entry<Integer, Integer> tryGetSeatIndex(Seat seat)
	    throws IllegalArgumentException {
	int row;
	int column;
	try {
	    row = seat.getRow().charAt(0) - 'A';
	    column = Integer.parseInt(seat.getColumn()) - 1;
	    if (row >= seats.length || column >= seats[0].length) {
		throw new Exception();
	    }
	} catch (Exception e) {
	    throw new IllegalArgumentException("Seat position invalid");
	}
	return new AbstractMap.SimpleEntry<>(row, column);
    }

    private CinemaException createCinemaException(String message,
	    int errorCode) {
	CinemaException ce = new CinemaException();
	ce.setErrorCode(errorCode);
	ce.setErrorMessage(message);
	return ce;
    }
}
