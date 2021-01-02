package solution;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Digraph;

public class SAP {
    private Digraph mG;


    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G)
    {
        if (G == null) throw new IllegalArgumentException("input Digraph is null");
        mG = new Digraph(G);
//        StdOut.print(mG + "\n");
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w)
    {
        isValid(v);
        isValid(w);

        myBFS bfs = new myBFS(mG, v, w);

        return bfs.sap();
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w)
    {

        isValid(v);
        isValid(w);
        myBFS bfs = new myBFS(mG, v, w);
        return bfs.ancestor();
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w)
    {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }
        isValid(v);
        isValid(w);
        myBFS bfs = new myBFS(mG, v, w);
        return bfs.sap();
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w)
    {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }
        isValid(v);
        isValid(w);
        myBFS bfs = new myBFS(mG, v, w);
        return bfs.ancestor();
    }


    private void isValid(int v)
    {
        if ((v < 0) || (v >= mG.V()))
            throw new IllegalArgumentException("input invalid");
    }

    private void isValid(Iterable<Integer> v)
    {
        for (Integer i : v)
            if (i == null || (i < 0) || (i >= mG.V()))
                throw new IllegalArgumentException("input invalid");
    }


    // do unit testing of this class
    public static void main(String[] args)
    {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);

        while (!StdIn.isEmpty())
        {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}