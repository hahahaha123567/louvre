package edge;

import time.Interval;
import vertex.Vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EdgeManager {

    private List<Edge> edgeList = new ArrayList<>();

    public void newEdge(Vertex from, Vertex to, int capacity, double time) {
        if (capacity < 0 || time < 0) {
            throw new RuntimeException(String.format("illegal edge: %s -> %s", from.name, to.name));
        }
        Edge edge = new Edge(from, to, time, capacity);
        edgeList.add(edge);
    }

    public List<Edge> getEdgeListByFromVertex(Vertex vertex) {
        return edgeList.stream()
                .filter(e -> Objects.equals(e.from, vertex))
                .collect(Collectors.toList());
    }

    public List<Edge> getEdgeListByToVertex(Vertex vertex) {
        return edgeList.stream()
                .filter(e -> Objects.equals(e.to, vertex))
                .collect(Collectors.toList());
    }

    public Edge getEdgeListByTwoVertex(Vertex from, Vertex to) {
        return edgeList.stream()
                .filter(e -> Objects.equals(e.from, from) && Objects.equals(e.to, to))
                .findAny()
                .orElseThrow(() -> new RuntimeException(String.format("getEdgeListByTwoVertexError, from = %s, to = %s", from.toString(), to.toString())));
    }

    public void linkSuperVertex(Vertex superVertex, List<Vertex> sourceList, boolean ahead) {
        for (Vertex v : sourceList) {
            if (ahead) {
                newEdge(superVertex, v, Integer.MAX_VALUE, 0);
            } else {
                newEdge(v, superVertex, Integer.MAX_VALUE, 0);
            }
        }
    }

    public int getCapacityByTime(Edge edge, double time) {
        List<Interval> intervalList = edge.intervalList;
        int capacity = edge.capacity;
        for (Interval interval : intervalList) {
            double startTime = interval.time;
            if (startTime == time) {
                capacity -= interval.delta;
            }
        }
        return capacity;
    }

    public void changeInterval(Edge edge, double startTime, int delta) {
        edge.intervalList.add(new Interval(startTime, delta));
    }

    public void remove(Edge edge) {
        edgeList.remove(edge);
    }

    public void print() {
        System.out.println("edgeList");
        edgeList.forEach(System.out::println);
    }

    public void debug() {
        System.out.println("EdgeManager.debug():");
        for (Edge edge : edgeList) {
            edge.debug();
        }
    }
}
