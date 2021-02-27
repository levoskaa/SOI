package WebSocket_W9HL9H;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/cinema")
public class CinemaEndpoint {
    @OnOpen
    public void open(Session session) {
	System.out.println("WebSocket opened: " + session.getId());
    }

    @OnClose
    public void close(Session session) {
	System.out.println("WebSocket closed: " + session.getId());
    }

    @OnError
    public void error(Throwable t) {
	System.out.println("WebSocket error: " + t.getMessage());
    }

    @OnMessage
    public String message(String msg) {
	System.out.println("WebSocket message: " + msg);
	return "Hello: " + msg;
    }
}
