package time;

public class Interval {
    public double time;
    public int delta;

    public Interval(double time, int delta) {
        this.time = time;
        this.delta = delta;
    }

    @Override
    public String toString() {
        return "{" +
                "time=" + time +
                ", delta=" + delta +
                '}';
    }

    public String debug() {
        return String.format("%f, %d", time, delta);
    }
}
