package com.fastline.hubservice.application.service;

import org.springframework.stereotype.Component;

import java.util.*;
@Component
public class ShortestPath {
    public List<UUID> dijkstra(Graph g, UUID start, UUID goal) {
        Map<UUID, Double> dist = new HashMap<>();
        Map<UUID, UUID> prev = new HashMap<>();
        PriorityQueue<UUID> pq = new PriorityQueue<>(Comparator.comparingDouble(dist::get));

        dist.put(start, 0.0);
        pq.add(start);

        while (!pq.isEmpty()) {
            UUID u = pq.poll();
            if (u.equals(goal)) break;
            for (Graph.Edge e : g.getAdj().getOrDefault(u, List.of())) {
                double nd = dist.get(u) + e.getDistance();
                if (nd < dist.getOrDefault(e.getTo(), Double.POSITIVE_INFINITY)) {
                    dist.put(e.getTo(), nd);
                    prev.put(e.getTo(), u);
                    pq.remove(e.getTo());
                    pq.add(e.getTo());
                }
            }
        }
        if (!prev.containsKey(goal) && !start.equals(goal)) return List.of(); // no path
        // 역추적
        LinkedList<UUID> path = new LinkedList<>();
        for (UUID at = goal; at != null; at = prev.get(at)) path.addFirst(at);
        return path;
    }
}
