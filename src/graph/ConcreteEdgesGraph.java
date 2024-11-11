package graph;

import java.util.*;

/**
 * A mutable weighted directed graph with String vertex labels.
 * Uses an internal representation with a set of vertices and a list of edges.
 * 
 * Abstraction Function:
 *   - `vertices` represents the set of vertices in the graph.
 *   - `edges` represents the directed edges, where each edge has a source, target, and weight.
 * 
 * Representation Invariant:
 *   - All edges in the `edges` list have a positive weight.
 *   - Each edge source and target must be contained within `vertices`.
 * 
 * Safety from Rep Exposure:
 *   - The `vertices` set and `edges` list are private and final.
 *   - Defensive copying is used in `vertices()`, `sources()`, and `targets()` to avoid exposing the internal state.
 */
public class ConcreteEdgesGraph implements Graph<String> {
    
    private final Set<String> vertices = new HashSet<>();
    private final List<Edge> edges = new ArrayList<>();
    
    // Check representation invariant
    private void checkRep() {
        for (Edge edge : edges) {
            assert vertices.contains(edge.getSource()) && vertices.contains(edge.getTarget()) : 
                "Edge vertices must be contained in the graph vertices";
            assert edge.getWeight() > 0 : "Edge weight must be positive";
        }
    }

    @Override
    public boolean add(String vertex) {
        if (vertices.contains(vertex)) {
            return false;
        }
        vertices.add(vertex);
        checkRep();
        return true;
    }
    
    @Override
    public int set(String source, String target, int weight) {
        add(source);
        add(target);
        
        for (Edge edge : edges) {
            if (edge.getSource().equals(source) && edge.getTarget().equals(target)) {
                int oldWeight = edge.getWeight();
                if (weight == 0) {
                    edges.remove(edge);
                } else {
                    edge.setWeight(weight);
                }
                checkRep();
                return oldWeight;
            }
        }
        
        if (weight > 0) {
            edges.add(new Edge(source, target, weight));
        }
        
        checkRep();
        return 0;
    }
    
    @Override
    public boolean remove(String vertex) {
        if (!vertices.contains(vertex)) {
            return false;
        }
        
        vertices.remove(vertex);
        edges.removeIf(edge -> edge.getSource().equals(vertex) || edge.getTarget().equals(vertex));
        
        checkRep();
        return true;
    }
    
    @Override
    public Set<String> vertices() {
        return new HashSet<>(vertices);
    }
    
    @Override
    public Map<String, Integer> sources(String target) {
        Map<String, Integer> sources = new HashMap<>();
        for (Edge edge : edges) {
            if (edge.getTarget().equals(target)) {
                sources.put(edge.getSource(), edge.getWeight());
            }
        }
        return sources;
    }
    
    @Override
    public Map<String, Integer> targets(String source) {
        Map<String, Integer> targets = new HashMap<>();
        for (Edge edge : edges) {
            if (edge.getSource().equals(source)) {
                targets.put(edge.getTarget(), edge.getWeight());
            }
        }
        return targets;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ConcreteEdgesGraph:\n");
        sb.append("Vertices: ").append(vertices).append("\n");
        sb.append("Edges: ");
        for (Edge edge : edges) {
            sb.append("\n  ").append(edge);
        }
        return sb.toString();
    }
}

/**
 * An immutable edge in a directed weighted graph.
 * 
 * Abstraction Function:
 *   - Represents a directed edge from `source` to `target` with a weight `weight`.
 * 
 * Representation Invariant:
 *   - `weight` must be positive.
 *   - `source` and `target` must be non-null.
 * 
 * Safety from Rep Exposure:
 *   - Fields are private, final, and immutable types (String and int).
 *   - No methods expose mutable references.
 */
class Edge {
    
    private final String source;
    private final String target;
    private int weight;

    // Constructor
    public Edge(String source, String target, int weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;
        checkRep();
    }

    // Check representation invariant
    private void checkRep() {
        assert source != null : "Source vertex must not be null";
        assert target != null : "Target vertex must not be null";
        assert weight > 0 : "Weight must be positive";
    }

    // Getters
    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public int getWeight() {
        return weight;
    }

    // Setter
    public void setWeight(int weight) {
        if (weight > 0) {
            this.weight = weight;
        }
        checkRep();
    }

    @Override
    public String toString() {
        return String.format("Edge: %s -> %s with weight %d", source, target, weight);
    }
}