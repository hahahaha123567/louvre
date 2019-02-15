package edge;

import time.Interval;
import vertex.Vertex;

import java.util.ArrayList;
import java.util.List;

public class Edge {
    public Vertex from;
    public Vertex to;
    public double time;
    public List<Interval> intervalList;
    int capacity;

    Edge(Vertex from, Vertex to, double time, int capacity) {
        this.from = from;
        this.to = to;
        this.time = time;
        this.capacity = capacity;
        intervalList = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Edge{" +
                "from=" + from +
                ", to=" + to +
                ", time=" + time +
                ", capacity=" + capacity +
                '}';
    }

    void debug() {
        StringBuilder sb = new StringBuilder(from.name + " - " + to.name);
        sb.append(": ");
        if (intervalList.size() > 0) {
            for (Interval interval : intervalList) {
                sb.append(interval.toString());
            }
        }
        System.out.println(sb.toString());
    }
}
