import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Digraph;
/**
 * The {@code SAP} class represent a data type to find a directed path from v to a common ancestor x,
 * together with a directed path from w to the same ancestor x of the minimum total length.
 *
 * This implementation uses breadth first search.
 */
public class SAP {
    private Digraph G;
    private static BreadthFirstDirectedPaths source;
    private static BreadthFirstDirectedPaths dest;
    /**
     * Constructor takes a digraph (not necessarily a DAG)
     * @param G a digraph
     */
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException("input Digraph is null");
        this.G = new Digraph(G);
    }

    //

    /**
     * Return the length of shortest ancestral path between vertices {@code v} and {@code w}
     * @param v a vertex in the digraph
     * @param w a vertex in the digraph
     * @return the length of shortest ancestral path between vertices {@code v} and {@code w}; -1 if no such path
     */
    public int length(int v, int w) {
        isValid(v);
        isValid(w);

        int ancestor = ancestor(v, w);
        if (ancestor == -1) {
            return -1;
        }
        return source.distTo(ancestor) + dest.distTo(ancestor);
    }

    //

    /**
     * Find a common ancestor of {@code v} and {@code w} that participates in a shortest ancestral path
     * @param v a vertex in the digraph
     * @param w a vertex in the digraph
     * @return a common ancestor of {@code v} and {@code w} that participates in a shortest ancestral path; -1 if no such path
     */
    public int ancestor(int v, int w) {
        isValid(v);
        isValid(w);
        source = new BreadthFirstDirectedPaths(G, v);
        dest = new BreadthFirstDirectedPaths(G, w);

        int ancestor = -1;
        int ancestorLength = Integer.MAX_VALUE;
        int tempLength = 0;

        for (int vertex = 0; vertex < G.V(); vertex++) {
            if (source.hasPathTo(vertex) && dest.hasPathTo(vertex)) {
                tempLength = source.distTo(vertex) + dest.distTo(vertex);
                if (tempLength < ancestorLength) {
                    ancestor = vertex;
                    ancestorLength = tempLength;
                }
            }
        }
        return ancestor;
    }

    /**
     * Find the length of shortest ancestral path between any vertices in {@code v} and {@code w}
     * @param v a vertex in the digraph
     * @param w a vertex in the digraph
     * @return the length of shortest ancestral path between any vertices in {@code v} and {@code w}; -1 if no such path
     */
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }
        isValid(v);
        isValid(w);
        int ancestor = ancestor(v, w);
        if (ancestor == -1) {
            return -1;
        }
        return source.distTo(ancestor) + dest.distTo(ancestor);
    }

    /**
     * Find a common ancestor that participates in shortest ancestral path; -1 if no such path
     * @param v a vertex in the digraph
     * @param w a vertex in the digraph
     * @return a common ancestor that participates in shortest ancestral path; -1 if no such path
     */
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }
        isValid(v);
        isValid(w);
        source = new BreadthFirstDirectedPaths(G, v);
        dest = new BreadthFirstDirectedPaths(G, w);

        int ancestor = -1;
        int ancestorLength = Integer.MAX_VALUE;
        int tempLength = 0;

        for (int vertex = 0; vertex < G.V(); vertex++) {
            if (source.hasPathTo(vertex) && dest.hasPathTo(vertex)) {
                tempLength = source.distTo(vertex) + dest.distTo(vertex);
                if (tempLength < ancestorLength) {
                    ancestor = vertex;
                    ancestorLength = tempLength;
                }
            }
        }
        return ancestor;
    }


    /**
     * A helper method that checks if the given vertex is in the bound.
     * @param v the vertex in the digraph to be checked.
     */
    private void isValid(int v) {
        if ((v < 0) || (v >= G.V()))
            throw new IllegalArgumentException("input invalid");
    }

    /**
     * A helper method that checks if every vertex in the given iterable is in the bound.
     * @param v a iterable to be checked
     */
    private void isValid(Iterable<Integer> v) {
        for (Integer i : v)
            if (i == null || (i < 0) || (i >= G.V()))
                throw new IllegalArgumentException("input invalid");
    }
}