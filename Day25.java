package com.alibaba.logistics.station;

import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.scoring.EdgeBetweennessCentrality;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.Map;
import java.util.function.Function;

public class Test {

    /**
     * NOTE: Use JGraphT library
     */

    public static void main(String[] args) {
        var graph = parseInputIntoGraph();

        new EdgeBetweennessCentrality<>(graph).getScores().entrySet().stream()
            .sorted(Map.Entry.<DefaultEdge, Double>comparingByValue().reversed())
            .limit(3)
            .forEach(e -> {
                var edge = e.getKey();
                System.err.println("Cut edge: " + edge);
                graph.removeEdge(edge);
            });

        var connectivity = new ConnectivityInspector<>(graph);
        var graph1Size = connectivity.connectedSets().get(0).size();
        var graph2Size = connectivity.connectedSets().get(1).size();
        System.err.println("Result: " + (graph1Size * graph2Size));
    }

    private static Graph<String, DefaultEdge> parseInputIntoGraph() {
        Graph<String, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);

        for (var line : Util.readFileToLines()) {
            var parts = line.split(":");
            var sourceVertex = parts[0];
            var adVertices = Util.parseLine(parts[1], " ", Function.identity());

            graph.addVertex(sourceVertex);

            for (var adVertex : adVertices) {
                graph.addVertex(adVertex);
                graph.addEdge(sourceVertex, adVertex);
            }
        }

        return graph;
    }
}
