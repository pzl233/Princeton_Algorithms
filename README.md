# Princeton_Algorithms
Code for programming assignments in Java for mainly the coursera course, [Algorithms Part II](https://www.coursera.org/learn/algorithms-part2).
KDTrees was the only assignment avaliable to me for Part I due to a disk failure.
All assignments were finished a few month ago, but at that time I didn't have a habit of maintaining codes using version controls.
Rewrote some methods and added a few comments to improve the readability of the code.

Week 1 - Directed and Undirected Graphs
--------------------------------
 - [**WordNet**](https://github.com/pzl233/Princeton_Algorithms/tree/master/WordNet) - a semantic lexicon for English language that is used extensively by computational linguists 
 and cognitive scientists; for example, it was a key component in IBM's [Watson](http://en.wikipedia.org/wiki/Watson_(computer)). 
 WordNet groups words into sets of synonyms called *synsets* and describes semantic relationships between them. 
 One such relationship is the *is-a* relationship, which connects a *hyponym* (more specific synset) to a *hypernym* (more general synset). 
 For example, *animal* is a hypernym of both *bird* and *fish*; *bird* is a hypernym of *eagle*, *pigeon*, and *seagull*.
 A small subgraph of the WordNet digraph appears below.
![](https://coursera.cs.princeton.edu/algs4/assignments/wordnet/wordnet-event.png)
 The implementation built a rooted Directed acyclic graph: each vertex v is an integer that represents a synset, and each directed edge v->w represents that w is a hypernym of v. The WordNet digraph is a rooted DAG: it is acyclic and has one vertex—the root—that is an ancestor of every other vertex. However, it is not necessarily a tree because a synset can have more than one hypernym. A small subgraph of the WordNet digraph appears below.

Week 2 - Minimum Spanning Trees and Shortest Paths
--------------------------------
 - [**SeamCarver**](https://github.com/pzl233/Princeton_Algorithms/tree/master/Seam%20Carving) - a data type that re-sizes a W-by-H image using the seam-carving technique. 
 
 Seam-carving is a content-aware image resizing technique where the image is reduced in size by one pixel of height (or width) at a time. 
 A vertical seam in an image is a path of pixels connected from the top to the bottom with one pixel in each row; a horizontal seam is a path of pixels connected 
 from the left to the right with one pixel in each column. Below left is the original 505-by-287 pixel image; below right is the result after removing 150 vertical seams, 
 resulting in a 30% narrower image. Unlike standard content-agnostic resizing techniques (such as cropping and scaling), seam carving preserves the most interest 
 features (aspect ratio, set of objects present, etc.) of the image.
 ![](https://coursera.cs.princeton.edu/algs4/assignments/seam/HJoceanSmall.png) | ![](https://coursera.cs.princeton.edu/algs4/assignments/seam/HJoceanSmallShrunk.png)

We calculate the energy (the importance)using the dual-gradient energy function of each pixel, and use the idea of shortest path to find the seams of minimum total energy.

 ![](https://coursera.cs.princeton.edu/algs4/assignments/seam/HJoceanSmallEnergy.png) |  ![](https://coursera.cs.princeton.edu/algs4/assignments/seam/HJoceanSmallVerticalSeam.png)

 
 Week 3 - Maximum Flow and Minimum Cut
 --------------------------------
  - [**BaseballElimination.java**](https://github.com/pzl233/Princeton_Algorithms/tree/master/BaseballElimination) - a data type that solves baseball elimination problem. 
   In the [baseball elimination problem](https://en.wikipedia.org/wiki/Maximum_flow_problem#Baseball_elimination), there is a division consisting of `n` teams. At some point during the season, team `i` has `w[i]` wins,  `l[i]` losses, `r[i]` remaining games, and `g[i][j]` games left to play against team `j`. A team is mathematically eliminated if it cannot possibly finish the season in (or tied for) first place. The goal is to determine exactly which teams are mathematically eliminated.  For simplicity, we assume that no games end in a tie (as is the case in Major League Baseball) and that there are no rainouts (i.e.,  every scheduled game is played).  The problem is not as easy as many sports writers would have you believe, in part because the answer depends not only on the number of games won and left to play, but also on the schedule of remaining games. To see the complication, consider the following scenario: 

  
  | i |  team        | wins | loss | left | Atl | Phi | NY | Mon |
  |:---:| :---:    |:---:|:---:|:---:|:---:|:---:|:---:|:---:|
  | 0 | Atlanta      | 83   | 71   | 8    | -   | 1   | 6  | 1   |
  | 1 | Philadelphia | 80   | 79   | 3    | 1   | -   | 0  | 2   |
  | 2 | New York     | 78   | 78   | 6    | 6   | 0   | -  | 0   |
  | 3 | Montreal     | 77   | 82   | 3    | 1   | 2   | 0  | -   |
  
  Montreal is mathematically eliminated since it can finish with at most 80 wins and Atlanta already has 83 wins. 
  This is the simplest reason for elimination. However, there can be more complicated reasons. For example, Philadelphia 
  is also mathematically eliminated. It can finish the season with as many as 83 wins, which appears to be enough to tie 
  Atlanta. But this would require Atlanta to lose all of its remaining games, including the 6 against New York, in which 
  case New York would finish with 84 wins. We note that New York is not yet mathematically eliminated despite the fact that 
  it has fewer wins than Philadelphia.

In this assignment, we can modeling the problem by creating a flow network if a team can win more than the number of wins of other teams, and using Ford–Fulkerson algorithm to computes the min cut to determine which teams are eliminated.
 ![](https://coursera.cs.princeton.edu/algs4/assignments/baseball/baseball.png)
  
Week 4 - Tries and Substring Search
--------------------------------
- [**Boggle**](https://github.com/pzl233/Princeton_Algorithms/blob/master/Boogle/pic.png) - a data type that wins the Boggle word game. 
 [Boggle](https://en.wikipedia.org/wiki/Boggle) is a word game designed by Allan Turoff and distributed by Hasbro. 
 It involves a board made up of 16 cubic dice, where each die has a letter printed on each of its 6 sides. 
 At the beginning of the game, the 16 dice are shaken and randomly distributed into a 4-by-4 tray, with only the top 
 sides of the dice visible. The player wins if they find all the letter combinations which are the valid words. 
Here are some examples of valid and invalid words: 
 ![](https://github.com/pzl233/Princeton_Algorithms/blob/master/Boogle/pic.png)

We can use a custom implementation of 26-way suffix trie data structure and traversal algorithms to search for all valid word in theboogle board.

Week 5 - Data Compression
--------------------------------
- [**Burrows-Wheeler**](https://github.com/pzl233/Princeton_Algorithms/tree/master/BurrowsWheeler) - a data type which implements Burrows–Wheeler data compression algorithm. 
 [Burrows–Wheeler data compression algorithm](https://en.wikipedia.org/wiki/Burrows%E2%80%93Wheeler_transform) is a revolutionary 
algorithm outcompresses gzip and PKZIP. It forms the basis of the Unix compression utility [bzip2](http://www.bzip.org/). It 
consists of three algorithmic components, which are applied in succession:
1. Burrows–Wheeler transform. Given a typical English text file, transform it into a text file in which sequences of the same 
character occur near each other many times.
2. [Move-to-front encoding]: Given a text file in which sequences of the same character occur near each other many times, convert it into a text file in which certain characters appear much more frequently than others.
3. [Huffman compression]: Given a text file in which certain characters appear much more frequently than others, compress it by encoding frequently occurring characters with short codewords and infrequently occurring characters with long codewords.
