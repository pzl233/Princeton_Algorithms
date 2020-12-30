import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * The {@code} WordNet} class represents a a semantic lexicon for the English language that
 * groups words into sets of synonyms called synsets.
 * It describes semantic relationships between synsets. One such relationship is the is-a relationship,
 * which connects a hyponym (more specific synset) to a hypernym (more general synset).
 * This implementation uses a rooted DAG(Directed acyclic graph).
 */

public class WordNet {
    private HashMap<String , ArrayList<Integer>> nounSynsets;
    private HashMap<Integer , String> synsetHypernyms;
    private Digraph netGraph;
    private SAP sap;

    /**
     * Constructor takes the name of the two input files
     * @param synsetsPath the path to the synsets csv file
     * @param hypernymsPath the path to the hypernyms csv file
     */
    public WordNet(String synsetsPath, String hypernymsPath) {
        readSynsets(synsetsPath);
        readHypernyms(hypernymsPath);
        isRooted();
        isDAG();
        sap = new SAP(netGraph);
    }


    /**
     * Returns all nouns in the WordNet
     * @return an iterable set of strings containing all nouns in the WordNet.
     */
    public Iterable<String> nouns() {
        return nounSynsets.keySet();
    }

    /**
     * Checks if the given word is a noun in our WordNet
     * @param word the word to be checked
     * @return {@code true} if our WordNet contains this word, {@code false} otherwise
     */
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException("Invalid NUll input.");
        return nounSynsets.containsKey(word);
    }

    /**
     * Returns the length of shortest ancestral path of subsets A and B
     * @param nounA subset A
     * @param nounB subset B
     * @return the length of shortest ancestral path of subsets A and B
     */
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("input is not a wordnet noun");
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException("input is null");
        return sap.length(nounSynsets.get(nounA), nounSynsets.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)

    /**
     * Return a synset (second field of synsets.txt) that is the common ancestor of {@code nounA} and {@code nounB}
     * @param nounA
     * @param nounB
     * @return a synset (second field of synsets.txt) that is the common ancestor of {@code nounA} and {@code nounB}
     */
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("At least one of the input is not a noun in our WordNet");
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException("At least one of the inputs is null");
        return synsetHypernyms.get(sap.ancestor(nounSynsets.get(nounA), nounSynsets.get(nounB)));
    }


    /**
     * A helper method that reads the synsets csv file from stdin line by line,
     * and initialize the {@code nounSynsets} maps by putting each synset as key and its associated ID as values,
     * then add the associated ID-synset relation to the {@code synsetHypernyms} map to get
     * @param path the path to the synsets csv file
     */
    private void readSynsets(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Invalid argument passed");
        }
        In in = new In(path);
        nounSynsets = new HashMap<String, ArrayList<Integer>>();
        synsetHypernyms = new HashMap<Integer, String>();
        while(!in.isEmpty()) {
            String line = in.readLine();
            String[] wholeLine = line.split(",");
            String[] words = wholeLine[1].split(" ");
            for (String w : words) {
                if (!nounSynsets.containsKey(w)) {
                    nounSynsets.put(w, new ArrayList<Integer>());
                }
                nounSynsets.get(w).add(Integer.parseInt(wholeLine[0]));
            }
            synsetHypernyms.put(Integer.parseInt(wholeLine[0]), wholeLine[1]);
        }
    }

    /**
     * A helper method that read the hypernyms csv file from stdin line by line,
     * and initialize the netGraph by adding edges whose vetices are the IDs for the synsets and hypernyms.
     * @param path the path to the hypernyms csv file
     */
    private void readHypernyms(String path) {
        if (path == null) throw new IllegalArgumentException("input name is null");
        In in = new In(path);
        netGraph = new Digraph(synsetHypernyms.size());
        while(!in.isEmpty()) {
            String line = in.readLine();
            String[] IDs = line.split(",");
            for (int i = 1; i < IDs.length; i++) {
                netGraph.addEdge(Integer.parseInt(IDs[0]), Integer.parseInt(IDs[i]));
            }
        }
    }

    /**
     * A helper method that checks if the digraph we created is a rooted graph as required.
     * @return the ID for the root vertex
     */
    private int isRooted() {
        int root = 0;
        int rootNum = 0;
        for (int i = 0; i < netGraph.V(); i++) {
            if(netGraph.outdegree(i) == 0) {
                root = i;
                rootNum++;
            }
        }
        if (rootNum != 1)
            throw new IllegalArgumentException("Invalid root number, the graph is not rooted!");
        return root;
    }

    /**
     * A helper method that checks if the graph we created is a required Directed Acyclic Graph.
     * It will throw an exception if a cycle is found.
     */
    private void isDAG() {
        DirectedCycle dc = new DirectedCycle(netGraph);
        if(dc.hasCycle())
            throw new IllegalArgumentException("The graph has cycle(s), it is not a DAG!");
    }

    public static void main(String[] args) {
//        solution.WordNet wn = new solution.WordNet(args[0], args[1]);
//        StdOut.print("distance :  " + wn.distance("a", "drug") + "\n");
//        StdOut.print("solution.SAP : " + wn.sap("a", "c") + "\n");
    }
}