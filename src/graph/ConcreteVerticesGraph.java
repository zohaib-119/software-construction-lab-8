package graph;

import java.util.*;

/**
 * A mutable weighted directed graph with String vertex labels.
 * Uses a list of Vertex objects to represent vertices and their edges.
 * 
 * Abstraction Function:
 *   - Each `Vertex` in the `vertices` list represents a vertex in the graph.
 *   - Each `Vertex` object maintains its own outgoing edges, mapping targets to weights.
 * 
 * Representation Invariant:
 *   - Each vertex label in `vertices` is unique.
 *   - Each edge weight is positive.
 * 
 * Safety from Rep Exposure:
 *   - The `vertices` list is private and final.
 *   - Defensive copying is used in `vertices()`, `sources()`, and `targets()` methods.
 */
public class ConcreteVerticesGraph implements Graph<String> {
    
    private final List<Vertex> vertices = new ArrayList<>();
    
    // Check representation invariant
    private void checkRep() {
        Set<String> labels = new HashSet<>();
        for (Vertex vertex : vertices) {
            assert vertex != null : "Vertices list should not contain null entries";
            assert labels.add(vertex.getLabel()) : "Duplicate vertex labels found";
            for (Integer weight : vertex.getEdges().values()) {
                assert weight > 0 : "Edge weight must be positive";
            }
        }
    }
    
    @Override
    public boolean add(String vertex) {
        for (Vertex v : vertices) {
            if (v.getLabel().equals(vertex)) {
                return false;
            }
        }
        vertices.add(new Vertex(vertex));
        checkRep();
        return true;
    }
    
    @Override
    public int set(String source, String target, int weight) {
        Vertex srcVertex = null, tgtVertex = null;
        
        for (Vertex vertex : vertices) {
            if (vertex.getLabel().equals(source)) {
                srcVertex = vertex;
            }
            if (vertex.getLabel().equals(target)) {
                tgtVertex = vertex;
            }
        }
        
        if (srcVertex == null) {
            srcVertex = new Vertex(source);
            vertices.add(srcVertex);
        }
        if (tgtVertex == null) {
            tgtVertex = new Vertex(target);
            vertices.add(tgtVertex);
        }
        
        int previousWeight = srcVertex.setEdge(target, weight);
        checkRep();
        return previousWeight;
    }
    
    @Override
    public boolean remove(String vertex) {
        Vertex vertexToRemove = null;
        
        for (Vertex v : vertices) {
            if (v.getLabel().equals(vertex)) {
                vertexToRemove = v;
            } else {
                v.removeEdge(vertex);
            }
        }
        
        if (vertexToRemove != null) {
            vertices.remove(vertexToRemove);
            checkRep();
            return true;
        }
        return false;
    }
    
    @Override
    public Set<String> vertices() {
        Set<String> labels = new HashSet<>();
        for (Vertex vertex : vertices) {
            labels.add(vertex.getLabel());
        }
        return labels;
    }
    
    @Override
    public Map<String, Integer> sources(String target) {
        Map<String, Integer> sources = new HashMap<>();
        for (Vertex vertex : vertices) {
            Integer weight = vertex.getEdge(target);
            if (weight != null) {
                sources.put(vertex.getLabel(), weight);
            }
        }
        return sources;
    }
    
    @Override
    public Map<String, Integer> targets(String source) {
        for (Vertex vertex : vertices) {
            if (vertex.getLabel().equals(source)) {
                return vertex.getEdges();
            }
        }
        return Collections.emptyMap();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ConcreteVerticesGraph:\n");
        for (Vertex vertex : vertices) {
            sb.append(vertex).append("\n");
        }
        return sb.toString();
    }
}


/**
 * A mutable vertex in a directed graph, with edges to other vertices and weights for each edge.
 * 
 * Abstraction Function:
 *   - Represents a vertex labeled `label` in a directed graph.
 *   - `edges` is a map where each key is a target vertex label and each value is the weight of the edge to that target.
 * 
 * Representation Invariant:
 *   - `label` is non-null.
 *   - Each edge weight in `edges` is positive.
 * 
 * Safety from Rep Exposure:
 *   - `label` is immutable and final.
 *   - Defensive copies are used where needed.
 */
class Vertex {
    
    private final String label;
    private final Map<String, Integer> edges = new HashMap<>();

    // Constructor
    public Vertex(String label) {
        this.label = label;
        checkRep();
    }

    // Check representation invariant
    private void checkRep() {
        assert label != null : "Label must not be null";
        for (Integer weight : edges.values()) {
            assert weight > 0 : "Edge weight must be positive";
        }
    }

    // Getters
    public String getLabel() {
        return label;
    }

    public Map<String, Integer> getEdges() {
        return new HashMap<>(edges);
    }

    // Set or update an edge to a target vertex with a given weight
    public int setEdge(String target, int weight) {
        int previousWeight = edges.getOrDefault(target, 0);
        if (weight == 0) {
            edges.remove(target);
        } else {
            edges.put(target, weight);
        }
        checkRep();
        return previousWeight;
    }

    // Get weight of the edge to a target vertex, or null if no edge exists
    public Integer getEdge(String target) {
        return edges.get(target);
    }

    // Remove an edge to the target vertex
    public void removeEdge(String target) {
        edges.remove(target);
        checkRep();
    }

    @Override
    public String toString() {
        return String.format("Vertex %s with edges %s", label, edges);
    }
}
