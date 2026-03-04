package com.aviation.routing.flight.path.engine.domain.model.route.finder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph<T> {
    private final Map<Vertex<T>, List<Connection<T>>> adjList = new HashMap<>();

    public void addEdge(Vertex<T> from, Vertex<T> to, EdgeInfo info) {
        adjList.computeIfAbsent(from, k -> new ArrayList<>()).add(new Connection<>(to, info));
    }

    public List<Connection<T>> getConnections(Vertex<T> v) {
        return adjList.getOrDefault(v, List.of());
    }

    public boolean containsVertex(Vertex<T> vertex) {
        return adjList.containsKey(vertex);
    }
}