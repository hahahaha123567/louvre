package vertex;

import time.Interval;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class VertexManager {

    private static String MAX_INTEGER_STR = String.valueOf(Integer.MAX_VALUE);
    private static String ZERO_INTEGER_STR = String.valueOf(0);

    public List<Vertex> vertexList = new ArrayList<>();
    public List<Vertex> sourceList = new ArrayList<>();
    public List<Vertex> destinationList = new ArrayList<>();
    public Vertex superSource;
    public Vertex superDestination;

    public int getTotalPeople() {
        return sourceList.stream().map(vertex -> vertex.initialOccupancy).reduce(0, (a, b) -> a + b);
    }

    /**
     * new destination vertex
     */
    public void newVertex(String name) {
        newVertex(name, MAX_INTEGER_STR, ZERO_INTEGER_STR);
    }

    /**
     * new inner vertex
     */
    public void newVertex(String name, String capacityStr) {
        newVertex(name, capacityStr, ZERO_INTEGER_STR);
    }

    /**
     * new source vertex
     */
    public void newVertex(String name, String capacityStr, String initialOccupancyStr) {
        int capacity = Integer.parseInt(capacityStr);
        int initialOccupancy = Integer.parseInt(initialOccupancyStr);
        if (capacity < 0 || initialOccupancy < 0) {
            throw new RuntimeException(String.format("illegal vertex: %s", name));
        }
        Vertex vertex = new Vertex(name, capacity, initialOccupancy);
        vertexList.add(vertex);
        if (initialOccupancy > 0) {
            sourceList.add(vertex);
        }
        if (capacityStr.equals(MAX_INTEGER_STR)) {
            destinationList.add(vertex);
        }
    }

    public Vertex getVertexByName(String name) {
        if (name == null || name.length() == 0) {
            throw new RuntimeException("invalid vertex name");
        }
        Optional<Vertex> vertex = vertexList.stream().filter(v -> Objects.equals(name, v.name)).findAny();
        return vertex.orElseThrow(() -> new RuntimeException(String.format("vertex not found, name: %s", name)));
    }

    public void generateSuperVertex() {
        superSource = new Vertex("SuperSource", Integer.MAX_VALUE, 0);
        superDestination = new Vertex("SuperDestination", Integer.MAX_VALUE, 0);
        vertexList.add(superSource);
        vertexList.add(superDestination);
    }

    public int getCapacityByTime(Vertex vertex, double time) {
        List<Interval> intervalList = vertex.intervalList;
        int capacity = vertex.capacity - vertex.initialOccupancy;
        for (Interval interval : intervalList) {
            double startTime = interval.time;
            if (startTime == time) {
                capacity -= interval.delta;
            }
        }
        return capacity;
    }

    public void changeInterval(Vertex vertex, double startTime, int delta) {
        if (vertex.initialOccupancy > 0) {
            vertex.initialOccupancy += delta;
        }
        vertex.intervalList.add(new Interval(startTime, delta));
    }

    public void print() {
        System.out.println("vertexList");
        vertexList.forEach(System.out::println);
        System.out.println("sourceList");
        sourceList.forEach(System.out::println);
        System.out.println("destinationList");
        destinationList.forEach(System.out::println);
    }

    public void debug() {
        System.out.println("VertexManager.debug():");
        for (Vertex vertex : vertexList) {
            vertex.debug();
        }
    }
}
