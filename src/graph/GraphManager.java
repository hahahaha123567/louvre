package graph;

import edge.Edge;
import edge.EdgeManager;
import vertex.Vertex;
import vertex.VertexManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class GraphManager {

    private static final VertexManager vertexManager = new VertexManager();
    private static final EdgeManager edgeManager = new EdgeManager();

    private static double startTime = 0;

    public void initGraph(String path) throws FileNotFoundException {
        // input
        FileInputStream inputStream = new FileInputStream(path);
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.startsWith("#")) {
                continue;
            }
            String[] split = line.split("\\s");
            switch (split.length) {
                case 1: {
                    vertexManager.newVertex(split[0]);
                    break;
                }
                case 2: {
                    vertexManager.newVertex(split[0], split[1]);
                    break;
                }
                case 3: {
                    vertexManager.newVertex(split[0], split[1], split[2]);
                    break;
                }
                case 4: {
                    edgeManager.newEdge(vertexManager.getVertexByName(split[0]),
                            vertexManager.getVertexByName(split[1]),
                            Integer.parseInt(split[2]), Integer.parseInt(split[3]));
                    break;
                }
            }
        }
        vertexManager.generateSuperVertex();
        edgeManager.linkSuperVertex(vertexManager.superSource, vertexManager.sourceList, true);
        edgeManager.linkSuperVertex(vertexManager.superDestination, vertexManager.destinationList, false);

        System.out.println("total people = " + vertexManager.getTotalPeople());
    }

    public List<Vertex> findShortestPath() {
        List<Vertex> shortestPath = findShortestPath(startTime);
        while (shortestPath.size() == 0) {
            startTime += 1;
            System.out.println(String.format("#### start time = %f", startTime));
            shortestPath = findShortestPath(startTime);
        }
        return shortestPath;
    }

    private List<Vertex> findShortestPath(double startTime) {
//        debug();
        if (vertexManager.superSource == null || vertexManager.superDestination == null) {
            throw new RuntimeException("find shortest path but no super source or destination");
        }
        // reset vertex
        for (Vertex vertex : vertexManager.vertexList) {
            vertex.cost = Double.MAX_VALUE;
            vertex.visited = false;
        }
        // need to wait
        for (Edge edge : edgeManager.getEdgeListByFromVertex(vertexManager.superSource)) {
            edge.time = startTime;
        }
        // calculate cost of each vertex
        Vertex vertex = vertexManager.superSource;
        vertex.cost = 0;
        while (vertex != null) {
            vertex.visited = true;
            double time = vertex.cost;
            List<Edge> edgeList = edgeManager.getEdgeListByFromVertex(vertex).stream()
                    .filter(e -> {
                        double edgeCapacity = edgeManager.getCapacityByTime(e, time);
                        double vertexCapacity = vertexManager.getCapacityByTime(e.to, time);
                        return !e.to.visited && edgeCapacity > 0 && vertexCapacity > 0;
                    }).collect(Collectors.toList());
            for (Edge edge : edgeList) {
                Vertex v = edge.to;
                if (v.cost > vertex.cost + edge.time) {
                    v.cost = vertex.cost + edge.time;
                }
            }
            vertex = vertexManager.vertexList.stream()
                    .filter(v -> !v.visited)
                    .min(Comparator.comparingDouble(v -> v.cost))
                    .orElse(null);
        }
        if (vertexManager.superDestination.cost == Double.MAX_VALUE) {
            return new ArrayList<>();
        }
        // get the path sequence
        List<Vertex> path = new LinkedList<>();
        Vertex last = vertexManager.superDestination;
        while (last != vertexManager.superSource) {
            path.add(0, last);
            for(Edge edge : edgeManager.getEdgeListByToVertex(last)) {
                if (edge.time + edge.from.cost == last.cost && edgeManager.getCapacityByTime(edge, edge.from.cost) > 0) {
                    last = edge.from;
                    break;
                }
            }
        }
        path.remove(path.size() - 1);
        return path;
    }

    public int getDelta(List<Vertex> path) {
        if (path == null || path.size() <= 1) {
            throw new RuntimeException("illegal shortest path");
        }

        int delta = path.get(0).initialOccupancy;
        for (int i = 0; i <= path.size() - 2; ++i) {
            Vertex vertex = path.get(i);
            double time = vertex.cost;
            Edge edge = edgeManager.getEdgeListByTwoVertex(path.get(i), path.get(i + 1));
            int vertexCapacity = vertexManager.getCapacityByTime(vertex, time);
            delta = vertexCapacity < delta ? vertexCapacity : delta;
            int edgeCapacity = edgeManager.getCapacityByTime(edge, time);
            delta = edgeCapacity < delta ? edgeCapacity : delta;
        }
        return delta;
    }

    public void changeDelta(List<Vertex> path, int delta) {
        for (int i = 0; i <= path.size() - 2; ++i) {
            Edge edge = edgeManager.getEdgeListByTwoVertex(path.get(i), path.get(i + 1));
            vertexManager.changeInterval(path.get(i), path.get(i).cost, i == 0 ? -delta : delta);
            if (i == 0 && path.get(i).initialOccupancy == 0) {
                edgeManager.remove(edgeManager.getEdgeListByTwoVertex(vertexManager.superSource, path.get(i)));
            }
            edgeManager.changeInterval(edge, path.get(i).cost, delta);
        }
    }

    public boolean allSaved() {
        return vertexManager.sourceList.stream().allMatch(v -> v.initialOccupancy == 0);
    }

    public void print() {
        vertexManager.print();
        edgeManager.print();
    }

    private void debug() {
        vertexManager.debug();
        edgeManager.debug();
    }
}
