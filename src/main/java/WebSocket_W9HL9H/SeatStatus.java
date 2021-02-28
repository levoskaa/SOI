package WebSocket_W9HL9H;

public enum SeatStatus {
    FREE("free"), LOCKED("locked"), RESERVED("reserved");

    private String value;

    SeatStatus(final String value) {
	this.value = value;
    }

    public String getValue() {
	return value;
    }

    @Override
    public String toString() {
	return this.getValue();
    }
}