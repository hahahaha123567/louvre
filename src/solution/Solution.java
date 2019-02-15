package solution;

import graph.GraphManager;
import vertex.Vertex;

import java.io.FileNotFoundException;
import java.util.List;

public class Solution {

    private static final GraphManager graphManager = new GraphManager();

    private static int count = 1;
    private static int delta;
    private static List<Vertex> shortestPath;

    public static void main(String[] args) throws FileNotFoundException {

        graphManager.initGraph("./resource/louvre/n32.txt");
        while (!graphManager.allSaved()) {
            shortestPath = graphManager.findShortestPath();
            delta = graphManager.getDelta(shortestPath);
            graphManager.changeDelta(shortestPath, delta);
            log();
        }
    }

    private static void log() {
        System.out.println(String.format("%d:", count++));
        shortestPath.forEach(vertex -> System.out.print(" -> " + vertex.name));
        System.out.println();
        System.out.println(String.format("number: %d, time: %.0fs", delta, shortestPath.get(shortestPath.size() - 1).cost));
    }
}
