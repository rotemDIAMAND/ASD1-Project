package graph;

import java.util.Date;

public final class Message {

    public final byte[] data;
    public final String asText;
    public final double asDouble;
    public final Date date;

    public Message(byte[] data) {
        this.data = data.clone(); // defensive copy
        this.asText = new String(this.data);
        this.asDouble = parseDoubleSafe(this.asText);
        this.date = new Date();
    }

    public Message(String text) {
        this.data = text.getBytes();
        this.asText = text;
        this.asDouble = parseDoubleSafe(text);
        this.date = new Date();
    }

    public Message(double value) {
        this(Double.toString(value));
    }

    private double parseDoubleSafe(String text) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }
}
