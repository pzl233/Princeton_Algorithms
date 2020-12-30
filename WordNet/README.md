## [WordNet](https://coursera.cs.princeton.edu/algs4/assignments/wordnet/specification.php)
_WordNet is a semantic lexicon for the English language that groups words into sets of synonyms called synsets. It describes semantic relationships between synsets. One such relationship is the is-a relationship, which connects a hyponym (more specific synset) to a hypernym (more general synset)._
* This implementation uses a rooted DAG(Directed acyclic graph). 
- It also support find a directed path from v to a common ancestor x, together with a directed path from w to the same ancestor x of the minimum total length uses breadth first search and identify which noun is the least related to the others.
