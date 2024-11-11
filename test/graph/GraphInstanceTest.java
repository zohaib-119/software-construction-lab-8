package graph;

import static org.junit.Assert.*;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

/**
 * Tests for instance methods of Graph.
 * 
 * <p>PS2 instructions: you MUST NOT add constructors, fields, or non-@Test
 * methods to this class, or change the spec of {@link #emptyInstance()}.
 * Your tests MUST only obtain Graph instances by calling emptyInstance().
 * Your tests MUST NOT refer to specific concrete implementations.
 */
public abstract class GraphInstanceTest {

    // Provides a new empty instance of Graph<String> for testing.
    public abstract Graph<String> emptyInstance();
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testInitialVerticesEmpty() {
        assertEquals("expected new graph to have no vertices",
                Collections.emptySet(), emptyInstance().vertices());
    }

    @Test
    public void testAddVertex() {
        Graph<String> graph = emptyInstance();
        assertTrue("expected true when adding a new vertex", graph.add("A"));
        assertEquals("expected one vertex in the graph", Set.of("A"), graph.vertices());
        assertFalse("expected false when adding an existing vertex", graph.add("A"));
    }

    @Test
    public void testAddEdgeAndGetTargets() {
        Graph<String> graph = emptyInstance();
        graph.add("A");
        graph.add("B");
        assertEquals("expected previous weight of 0 when adding new edge", 0, graph.set("A", "B", 5));
        assertEquals("expected updated weight", 5, graph.set("A", "B", 7));

        Map<String, Integer> targets = graph.targets("A");
        assertEquals("expected target B with weight 7", Map.of("B", 7), targets);
    }

    @Test
    public void testRemoveEdge() {
        Graph<String> graph = emptyInstance();
        graph.add("A");
        graph.add("B");
        graph.set("A", "B", 3);
        assertEquals("expected weight 3 before removing edge", 3, graph.set("A", "B", 0));

        assertTrue("expected no targets from A", graph.targets("A").isEmpty());
    }

    @Test
    public void testRemoveVertex() {
        Graph<String> graph = emptyInstance();
        graph.add("A");
        graph.add("B");
        graph.set("A", "B", 3);
        assertTrue("expected true when removing vertex", graph.remove("A"));
        assertFalse("expected false when removing non-existent vertex", graph.remove("A"));

        assertFalse("expected vertex B still exists", graph.vertices().contains("A"));
        assertTrue("expected no edges remaining", graph.targets("A").isEmpty());
        assertTrue("expected no edges to B", graph.sources("B").isEmpty());
    }

    @Test
    public void testGetVertices() {
        Graph<String> graph = emptyInstance();
        graph.add("A");
        graph.add("B");
        graph.add("C");
        assertEquals("expected vertices A, B, C", Set.of("A", "B", "C"), graph.vertices());
    }

    @Test
    public void testSources() {
        Graph<String> graph = emptyInstance();
        graph.add("A");
        graph.add("B");
        graph.set("A", "B", 4);
        graph.set("C", "B", 5);
        
        Map<String, Integer> sources = graph.sources("B");
        assertEquals("expected sources A and C with weights 4 and 5", Map.of("A", 4, "C", 5), sources);
    }

    @Test
    public void testTargets() {
        Graph<String> graph = emptyInstance();
        graph.add("A");
        graph.add("B");
        graph.add("C");
        graph.set("A", "B", 2);
        graph.set("A", "C", 3);

        Map<String, Integer> targets = graph.targets("A");
        assertEquals("expected targets B and C with weights 2 and 3", Map.of("B", 2, "C", 3), targets);
    }
}
