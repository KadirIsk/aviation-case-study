package com.aviation.routing.flight.path.engine.domain.service.impl;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aviation.routing.flight.path.engine.domain.model.projection.PathEdge;
import com.aviation.routing.flight.path.engine.domain.model.projection.RouteCandidate;
import com.aviation.routing.flight.path.engine.domain.model.route.finder.EdgeInfo;
import com.aviation.routing.flight.path.engine.domain.port.NeighborProviderPort;
import com.aviation.routing.flight.path.engine.domain.port.RouteFinderPort;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RouteFinderService implements RouteFinderPort {
    private final NeighborProviderPort neighborProviderPort;
    private static final int MAX_EDGES = 3;

    @Override
    public List<RouteCandidate> findRoutes(Long originId, Long destinationId, short requestedDayMask) {
        List<RouteCandidate> validRoutes = new ArrayList<>();
        Deque<PathEdge> candidatePath = new ArrayDeque<>();
        Set<Long> visitedNodes = new HashSet<>();

        visitedNodes.add(originId);
        dfs(originId, destinationId, candidatePath, requestedDayMask, visitedNodes, validRoutes);
        return validRoutes;
    }

    private void dfs(
        Long currentNode,
        Long destinationId,
        Deque<PathEdge> candidatePath,
        short requestedDayMask,
        Set<Long> visitedNodes,
        List<RouteCandidate> validRoutes
    ) {
        if (isReached(currentNode, destinationId, candidatePath)) {
            validRoutes.add(RouteCandidate.builder().edges(new ArrayList<>(candidatePath)).build());
            return;
        }

        if (candidatePath.size() >= MAX_EDGES) {
            return;
        }

        Map<Long, EdgeInfo> neighbors = neighborProviderPort.getNeighbors(currentNode, requestedDayMask);
        for (Map.Entry<Long, EdgeInfo> entry : neighbors.entrySet()) {
            Long nextNodeId = entry.getKey();

            if (!isVisitedBefore(visitedNodes, nextNodeId)) {
                PathEdge edge = createPathEdge(currentNode, entry);

                candidatePath.addLast(edge);
                visitedNodes.add(nextNodeId);

                dfs(nextNodeId, destinationId, candidatePath, requestedDayMask, visitedNodes, validRoutes);

                backtrack(candidatePath, visitedNodes, nextNodeId);
            }
        }
    }

    private static boolean isVisitedBefore(Set<Long> visitedNodes, Long nextNodeId) {
        return visitedNodes.contains(nextNodeId);
    }

    private static boolean isReached(Long currentNode, Long destinationId, Deque<PathEdge> candidatePath) {
        return currentNode.equals(destinationId) && !candidatePath.isEmpty();
    }

    private static void backtrack(Deque<PathEdge> candidatePath, Set<Long> visitedNodes, Long nextNodeId) {
        candidatePath.removeLast();
        visitedNodes.remove(nextNodeId);
    }

    private static PathEdge createPathEdge(Long originId, Map.Entry<Long, EdgeInfo> entry) {
        return PathEdge.builder()
            .originId(originId)
            .destinationId(entry.getKey())
            .transportationType(entry.getValue().type())
            .build();
    }

    @Builder
    private record PathContext(
        Long currentNode,
        List<PathEdge> edges,
        Set<Long> visitedNodes
    ) { }
}