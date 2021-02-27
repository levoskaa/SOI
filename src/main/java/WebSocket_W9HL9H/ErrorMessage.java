package WebSocket_W9HL9H;

public class ErrorMessage {
    private String type;
    private String message;

    public ErrorMessage() {
	this("");
    }

    public ErrorMessage(String message) {
	this.type = "error";
	this.message = message;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
    }
}
