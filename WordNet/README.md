<div id="nav2">[![](./Programming Assignment 1_ WordNet_files/logo.png) WordNet](https://coursera.cs.princeton.edu/algs4/assignments/wordnet/specification.php)

*   [Spec](https://coursera.cs.princeton.edu/algs4/assignments/wordnet/specification.php)
*   [FAQ](https://coursera.cs.princeton.edu/algs4/assignments/wordnet/faq.php)
*   [Project](https://coursera.cs.princeton.edu/algs4/assignments/wordnet/wordnet.zip)
*   [Submit](https://www.coursera.org/learn/algorithms-part2/programming/BCNsp/wordnet/submission)

</div>

[WordNet](http://wordnet.princeton.edu/) is a semantic lexicon for the English language that computational linguists and cognitive scientists use extensively. For example, WordNet was a key component in IBM’s Jeopardy-playing [Watson](http://en.wikipedia.org/wiki/Watson_(computer)) computer system. WordNet groups words into sets of synonyms called _synsets_. For example, { _AND circuit_, _AND gate_ } is a synset that represent a logical gate that fires only when all of its inputs fire. WordNet also describes semantic relationships between synsets. One such relationship is the _is-a_ relationship, which connects a _hyponym_ (more specific synset) to a _hypernym_ (more general synset). For example, the synset { _gate_, _logic gate_ } is a hypernym of { _AND circuit_, _AND gate_ } because an AND gate is a kind of logic gate.

**The WordNet digraph.** Your first task is to build the WordNet digraph: each vertex _v_ is an integer that represents a synset, and each directed edge _v→w_ represents that _w_ is a hypernym of _v_. The WordNet digraph is a _rooted DAG_: it is acyclic and has one vertex—the _root_—that is an ancestor of every other vertex. However, it is not necessarily a tree because a synset can have more than one hypernym. A small subgraph of the WordNet digraph appears below.

<div style="text-align:center;">![](./Programming Assignment 1_ WordNet_files/wordnet-event.png)</div>

**The WordNet input file formats.** We now describe the two data files that you will use to create the WordNet digraph. The files are in _comma-separated values_ (CSV) format: each line contains a sequence of fields, separated by commas.

*   _List of synsets._ The file [synsets.txt](https://coursera.cs.princeton.edu/algs4/assignments/wordnet/files/synsets.txt) contains all noun synsets in WordNet, one per line. Line _i_ of the file (counting from 0) contains the information for synset _i_. The first field is the _synset id_, which is always the integer _i_; the second field is the synonym set (or _synset_); and the third field is its dictionary definition (or _gloss_), which is not relevant to this assignment.

    <div style="text-align:center;">![](./Programming Assignment 1_ WordNet_files/wordnet-synsets.png)</div>

    For example, line 36 means that the synset { `AND_circuit`, `AND_gate` } has an id number of 36 and its gloss is `a circuit in a computer that fires only when all of its inputs fire`. The individual nouns that constitute a synset are separated by spaces. If a noun contains more than one word, the underscore character connects the words (and not the space character).

*   _List of hypernyms._ The file [hypernyms.txt](https://coursera.cs.princeton.edu/algs4/assignments/wordnet/files/hypernyms.txt) contains the hypernym relationships. Line _i_ of the file (counting from 0) contains the hypernyms of synset _i_. The first field is the synset id, which is always the integer _i_; subsequent fields are the id numbers of the synset’s hypernyms.

    <div style="text-align:center;">![](./Programming Assignment 1_ WordNet_files/wordnet-hypernyms.png)</div>

    For example, line 36 means that synset 36 (`AND_circuit AND_Gate`) has 42338 (`gate logic_gate`) as its only hypernym. Line 34 means that synset 34 (`AIDS acquired_immune_deficiency_syndrome`) has two hypernyms: 47569 (`immunodeficiency`) and 48084 (`infectious_disease`).

**WordNet data type.** Implement an immutable data type `WordNet` with the following API:

> <pre>**public class WordNet {**
> 
>    <font color="gray">// constructor takes the name of the two input files</font>
>    **public WordNet(String synsets, String hypernyms)**
> 
>    <font color="gray">// returns all WordNet nouns</font>
>    **public Iterable<String> nouns()**
> 
>    <font color="gray">// is the word a WordNet noun?</font>
>    **public boolean isNoun(String word)**
> 
>    <font color="gray">// distance between nounA and nounB (defined below)</font>
>    **public int distance(String nounA, String nounB)**
> 
>    <font color="gray">// a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB</font>
>    <font color="gray">// in a shortest ancestral path (defined below)</font>
>    **public String sap(String nounA, String nounB)**
> 
>    <font color="gray">// do unit testing of this class</font>
>    **public static void main(String[] args)**
> **}**
> </pre>

_Corner cases. _ Throw an `IllegalArgumentException` in the following situations:

*   Any argument to the constructor or an instance method is `null`
*   The input to the constructor does not correspond to a rooted DAG.
*   Any of the noun arguments in `distance()` or `sap()` is not a WordNet noun.

You may assume that the input files are in the specified format.

_Performance requirements. _ Your data type should use space linear in the input size (size of synsets and hypernyms files). The constructor should take time linearithmic (or better) in the input size. The method `isNoun()` should run in time logarithmic (or better) in the number of nouns. The methods `distance()` and `sap()` should run in time linear in the size of the WordNet digraph. For the analysis, assume that the number of nouns per synset is bounded by a constant.

**Shortest ancestral path.** An _ancestral path_ between two vertices _v_ and _w_ in a digraph is a directed path from _v_ to a common ancestor _x_, together with a directed path from _w_ to the same ancestor _x_. A _shortest ancestral path_ is an ancestral path of minimum total length. We refer to the common ancestor in a shortest ancestral path as a _shortest common ancestor_. Note also that an ancestral path is a path, but not a directed path.

<div style="text-align:center;">![](./Programming Assignment 1_ WordNet_files/wordnet-sca.png)</div>

We generalize the notion of shortest common ancestor to _subsets_ of vertices. A shortest ancestral path of two subsets of vertices _A_ and _B_ is a shortest ancestral path over all pairs of vertices _v_ and _w_, with _v_ in _A_ and _w_ in _B_. The figure ([digraph25.txt](https://coursera.cs.princeton.edu/algs4/assignments/wordnet/files/digraph25.txt)) below shows an example in which, for two subsets, red and blue, we have computed several (but not all) ancestral paths, including the shortest one.

<div style="text-align:center;">![](./Programming Assignment 1_ WordNet_files/wordnet-sca-set.png)</div>

**SAP data type.** Implement an immutable data type `SAP` with the following API:

> <pre>**public class SAP {**
> 
>    <font color="gray">// constructor takes a digraph (not necessarily a DAG)</font>
>    **public SAP(Digraph G)**
> 
>    <font color="gray">// length of shortest ancestral path between v and w; -1 if no such path</font>
>    **public int length(int v, int w)**
> 
>    <font color="gray">// a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path</font>
>    **public int ancestor(int v, int w)**
> 
>    <font color="gray">// length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path</font>
>    **public int length(Iterable<Integer> v, Iterable<Integer> w)**
> 
>    <font color="gray">// a common ancestor that participates in shortest ancestral path; -1 if no such path</font>
>    **public int ancestor(Iterable<Integer> v, Iterable<Integer> w)**
> 
>    <font color="gray">// do unit testing of this class</font>
>    **public static void main(String[] args)**
> **}**
> 
> </pre>

_Corner cases. _ Throw an `IllegalArgumentException` in the following situations:

*   Any argument is `null`
*   Any vertex argument is outside its prescribed range
*   Any iterable argument contains a `null` item

_Performance requirements. _ All methods (and the constructor) should take time at most proportional to _E_ + _V_ in the worst case, where _E_ and _V_ are the number of edges and vertices in the digraph, respectively. Your data type should use space proportional to _E_ + _V_.

**Test client.** The following test client takes the name of a digraph input file as as a command-line argument, constructs the digraph, reads in vertex pairs from standard input, and prints out the length of the shortest ancestral path between the two vertices and a common ancestor that participates in that path:

> <pre>public static void main(String[] args) {
>     In in = new In(args[0]);
>     Digraph G = new Digraph(in);
>     SAP sap = new SAP(G);
>     while (!StdIn.isEmpty()) {
>         int v = StdIn.readInt();
>         int w = StdIn.readInt();
>         int length   = sap.length(v, w);
>         int ancestor = sap.ancestor(v, w);
>         StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
>     }
> }
> </pre>

Here is a sample execution:

> <pre>% **cat digraph1.txt**              % **java-algs4 SAP digraph1.txt**
> 13                              **3 11**
> 11                              length = 4, ancestor = 1
>  7  3                            
>  8  3                           **9 12**
>  3  1                           length = 3, ancestor = 5
>  4  1
>  5  1                           **7 2**
>  9  5                           length = 4, ancestor = 0
> 10  5
> 11 10                           **1 6**
> 12 10                           length = -1, ancestor = -1
>  1  0
>  2  0
> </pre>

**Measuring the semantic relatedness of two nouns**. Semantic relatedness refers to the degree to which two concepts are related. Measuring semantic relatedness is a challenging problem. For example, you consider _George W. Bush_ and _John F. Kennedy_ (two U.S. presidents) to be more closely related than _George W. Bush_ and _chimpanzee_ (two primates). It might not be clear whether _George W. Bush_ and _Eric Arthur Blair_ are more related than two arbitrary people. However, both _George W. Bush_ and _Eric Arthur Blair_ (a.k.a. George Orwell) are famous communicators and, therefore, closely related.

We define the semantic relatedness of two WordNet nouns _x_ and _y_ as follows:

*   _A_ = set of synsets in which _x_ appears
*   _B_ = set of synsets in which _y_ appears
*   _distance_(_x, y_) = length of shortest ancestral path of subsets _A_ and _B_
*   _sca_(_x, y_) = a shortest common ancestor of subsets _A_ and _B_

This is the notion of distance that you will use to implement the `distance()` and `sap()` methods in the `WordNet` data type.

<div style="text-align:center;">![](./Programming Assignment 1_ WordNet_files/wordnet-distance.png)</div>

**Outcast detection.** Given a list of WordNet nouns _x_<sub>1</sub>, _x_<sub>2</sub>, ..., _x_<sub>_n_</sub>, which noun is the least related to the others? To identify _an outcast_, compute the sum of the distances between each noun and every other one:

> _d_<sub>_i_</sub>   =   _distance_(_x_<sub>_i_</sub>, _x_<sub>1</sub>)   +   _distance_(_x_<sub>_i_</sub>, _x_<sub>2</sub>)   +   ...   +   _distance_(_x_<sub>_i_</sub>, _x_<sub>_n_</sub>)

and return a noun _x_<sub>_t_</sub> for which _d_<sub>_t_</sub> is maximum. Note that _distance_(_x_<sub>_i_</sub>, _x_<sub>_i_</sub>) = 0, so it will not contribute to the sum.

Implement an immutable data type `Outcast` with the following API:

> <pre>**public class Outcast {**
>    **public Outcast(WordNet wordnet)**         <font color="gray">// constructor takes a WordNet object</font>
>    **public String outcast(String[] nouns)**   <font color="gray">// given an array of WordNet nouns, return an outcast</font>
>    **public static void main(String[] args)**  <font color="gray">// see test client below</font>
> **}**
> </pre>

Assume that argument to `outcast()` contains only valid wordnet nouns (and that it contains at least two such nouns).

The following test client takes from the command line the name of a synset file, the name of a hypernym file, followed by the names of outcast files, and prints out an outcast in each file:

> <pre>public static void main(String[] args) {
>     WordNet wordnet = new WordNet(args[0], args[1]);
>     Outcast outcast = new Outcast(wordnet);
>     for (int t = 2; t < args.length; t++) {
>         In in = new In(args[t]);
>         String[] nouns = in.readAllStrings();
>         StdOut.println(args[t] + ": " + outcast.outcast(nouns));
>     }
> }
> </pre>

Here is a sample execution:

> <pre>% **cat outcast5.txt**
> horse zebra cat bear table
> 
> % **cat outcast8.txt**
> water soda bed orange_juice milk apple_juice tea coffee
> 
> % **cat outcast11.txt**
> apple pear peach banana lime lemon blueberry strawberry mango watermelon potato
> 
> % **java-algs4 Outcast synsets.txt hypernyms.txt outcast5.txt outcast8.txt outcast11.txt**
> outcast5.txt: table
> outcast8.txt: bed
> outcast11.txt: potato
> </pre>

**Analysis of running time (optional).** Analyze the effectiveness of your approach to this problem by giving estimates of its time requirements.

*   Give the order of growth of the _worst-case_ running time of the `length()` and `ancestor()` methods in `SAP` as a function of the number of vertices _V_ and the number of edges _E_ in the digraph.
*   Give the order of growth of the _best-case_ running time of the same methods.

**Web submission.** Submit a .zip file containing `WordNet.java`, `SAP.java`, `Outcast.java`, any other supporting files (excluding `algs4.jar`). You may not call any library functions except those in `java.lang`, `java.util`, and `algs4.jar`.

<address><small>This assignment was developed by Alina Ene and Kevin Wayne.  
Copyright © 2006.</small></address>

<table></table>
