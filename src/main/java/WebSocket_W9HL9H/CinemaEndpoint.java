package WebSocket_W9HL9H;

import java.io.StringReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/cinema")
public class CinemaEndpoint {
    private static SeatStatus[][] seats = new SeatStatus[0][0];
    private static Map<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void open(Session session) {
	System.out.println("WebSocket opened: " + session.getId());
	sessions.put(session.getId(), session);
    }

    @OnClose
    public void close(Session session) {
	System.out.println("WebSocket closed: " + session.getId());
	sessions.remove(session.getId());
    }

    @OnError
    public void error(Throwable t) {
	System.out.println("WebSocket error: " + t.getMessage());
    }

    @OnMessage
    public void message(Session session, String msg) {
	System.out.println("WebSocket message: " + msg);
	JsonReader jsonReader = Json.createReader(new StringReader(msg));
	JsonObject message = jsonReader.readObject();
	String messageType = message.getString("type");
	switch (messageType) {
	case "initRoom":
	    initRoom(session, message);
	    break;
	case "getRoomSize":
	    getRoomSize(session);
	    break;
	case "updateSeats":
	    updateSeats(session);
	    break;
	}
    }

    private void initRoom(Session session, JsonObject message) {
	int rows = 0;
	int columns = 0;
	JsonObject error = null;
	boolean isError = false;
	try {
	    rows = Integer.parseInt(message.get("rows").toString());
	    columns = Integer.parseInt(message.get("columns").toString());
	    if (rows <= 0 || columns <= 0) {
		throw new Exception();
	    }
	    seats = new SeatStatus[rows][columns];
	} catch (Exception e) {
	    error = Json.createObjectBuilder().add("type", "error")
		    .add("message",
			    "Row or column count is not positive integer")
		    .build();
	    isError = true;
	}
	if (isError) {
	    try {
		session.getBasicRemote().sendText(error.toString());
	    } catch (Exception e) {
	    }
	}
	seats = new SeatStatus[rows][columns];
	for (int i = 0; i < rows; ++i) {
	    for (int j = 0; j < columns; ++j) {
		seats[i][j] = SeatStatus.FREE;
	    }
	}
    }

    private void getRoomSize(Session session) {
	JsonObject roomSize = Json.createObjectBuilder().add("type", "roomSize")
		.add("rows", seats.length).add("columns", seats[0].length)
		.build();
	try {
	    session.getBasicRemote().sendText(roomSize.toString());
	} catch (Exception e) {
	}
    }

    private void updateSeats(Session session) {
	int rows = seats.length;
	int columns = seats[0].length;
	JsonObject seatStatus;
	for (int i = 0; i < rows; ++i) {
	    for (int j = 0; j < columns; ++j) {
		try {
		    seatStatus = Json.createObjectBuilder()
			    .add("type", "seatStatus").add("row", i + 1)
			    .add("column", j + 1)
			    .add("status", seats[i][j].toString()).build();
		    session.getBasicRemote().sendText(seatStatus.toString());
		} catch (Exception e) {
		}
	    }
	}
    }
}
