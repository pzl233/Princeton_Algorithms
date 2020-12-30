/**
 * The {@code Outcast} class represents a data type to identify which noun is the least related to the others.
 * It computes the sum of the distances between each noun and every other one:
 *      di   =   distance(xi, x1)   +   distance(xi, x2)   +   ...   +   distance(xi, xn)
 * and return a noun xt for which dt is maximum. Note that distance(xi, xi) = 0, so it will not contribute to the sum.
 */
public class Outcast {
    private WordNet wn;
    /**
     * Constructor takes a WordNet object
     * @param wordnet a WordNet object
     */
    public Outcast(WordNet wordnet) {
        wn = wordnet;
    }

    /**
     * Given an array of WordNet nouns, return an outcast
     * @param nouns an array of WordNet nounc
     * @return a noun that is the least related to the others?
     */
    public String outcast(String[] nouns) {
        int[] dist = new int[nouns.length];
        for (int i = 0; i < nouns.length; i++)
            for (int j = 0; j < nouns.length; j++) {
                if (i != j) {
                    dist[i] += wn.distance(nouns[i], nouns[j]);
                }
            }
        int maxID = 0;
        for (int i = 0; i < nouns.length; i++)
        {
            if (dist[maxID] < dist[i])
                maxID = i;
        }
        return nouns[maxID];
    }

}