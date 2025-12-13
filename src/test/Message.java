package test;
import java.util.Arrays;
import java.util.Date;

public final class Message {

    public final byte[] data;
    public final double asDouble;
    public final Date date;

    /* ===== בנאי מרכזי ===== */
    public Message(String text) {
        this.data = text.getBytes();

        double value;
        try {
            value = Double.parseDouble(text);
        } catch (NumberFormatException e) {
            value = Double.NaN;
        }
        this.asDouble = value;

        this.date = new Date();
    }

    /* ===== reuse באמצעות this() ===== */
    public Message(byte[] data) {
        this(new String(data));
    }

    public Message(double number) {
        this(Double.toString(number));
    }

    /* ===== getters ===== */

    public byte[] getData() {
        return Arrays.copyOf(data, data.length);
    }

    public String asText() {
        return new String(data);
    }

    public double asDouble() {
        return asDouble;
    }

    public Date getDate() {
        return new Date(date.getTime());
    }
}


