package vertex;

import time.Interval;

import java.util.*;

public class Vertex {

    public String name;
    int capacity;
    public int initialOccupancy;
    public List<Interval> intervalList;

    // tmp variable for dijkstra's algorithm
    public double cost;
    public boolean visited;

    Vertex(String name, int capacity, int initialOccupancy) {
        this.name = name;
        this.capacity = capacity;
        this.initialOccupancy = initialOccupancy;
        intervalList = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "name='" + name + '\'' +
                ", capacity=" + capacity +
                ", initialOccupancy=" + initialOccupancy +
                ", cost=" + cost +
                ", visited=" + visited +
                ", intervalList=" + intervalList +
                '}';
    }

    void debug() {
        StringBuilder sb = new StringBuilder(name);
        sb.append(": ");
        sb.append("initial = ");
        sb.append(initialOccupancy);
        if (intervalList.size() > 0) {
            for (Interval interval : intervalList) {
                sb.append(interval.toString());
            }
        }
        System.out.println(sb.toString());
    }
}
