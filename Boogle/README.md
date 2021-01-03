


<div id="nav2">
<a href="" id="logo-noborder"> <img src="specification_files/logo.png"> Boggle</a>
<ul id="links">
<li><a href="" id="here">Spec</a></li>
<li><a href="faq.php">FAQ</a></li>
<li><a href="specification_files/boggle.zip">Project</a></li>
<li><a href="https://www.coursera.org/learn/algorithms-part2/programming/9GqJs/boggle/submission" target="_blank">Submit</a></li>
</ul>
</div>

&#10;Write a program to play the word game Boggle<sup>®</sup>.
&#10;

&#10;<b>The Boggle game.</b> Boggle is a word game designed by Allan Turoff and distributed by Hasbro.
It involves a board made up of 16 cubic dice, where each die has a letter printed on each
of its 6 sides.
At the beginning of the game, the 16 dice are shaken and randomly
distributed into a 4-by-4 tray, with only the top sides of the dice visible.
The players compete to accumulate points by building *valid* words from the dice,
according to these rules:


- A valid word must be composed by following a sequence of *adjacent dice*—two
    dice are adjacent if they are horizontal, vertical, or diagonal neighbors.
- A valid word can use each die at most once.
- A valid word must contain at least 3 letters.
- A valid word must be in the dictionary (which typically does not contain proper nouns).

Here are some examples of valid and invalid words:


&#10;<table class="image" align="center">
<tbody><tr><td>![pins](specification_files/pins.png "pins")</td>
<td>&nbsp;&nbsp;&nbsp;</td>
    <td>![pines](specification_files/pines.png "pines")</td>
<td>&nbsp;&nbsp;&nbsp;</td>
    <td>![dates](specification_files/dates.png "dates")</td></tr>
<tr><td class="caption" align="center">`PINS`  
(*valid*)</td>
<td>&nbsp;&nbsp;&nbsp;</td>
    <td class="caption" align="center">`PINES`  
(*valid*)</td>
<td>&nbsp;&nbsp;&nbsp;</td>
    <td class="caption" align="center">`DATES`  
(*invalid—dice not adjacent*)</td></tr>
<tr><td>&nbsp;</td></tr>
<tr><td>![pint](specification_files/pint.png "pint")</td>
<td>&nbsp;&nbsp;&nbsp;</td>
    <td>![tepee](specification_files/tepee.png "tepee")</td>
<td>&nbsp;&nbsp;&nbsp;</td>
    <td>![sid](specification_files/sid.png "sid")</td></tr>
<tr><td class="caption" align="center">`PINT`  
(*invalid—path not sequential*)</td>
<td>&nbsp;&nbsp;&nbsp;</td>
    <td class="caption" align="center">`TEPEE`  
(*invalid—die used more than once*)</td>
<td>&nbsp;&nbsp;&nbsp;</td>
    <td class="caption" align="center">`SID`  
(*invalid—word not in dictionary*)</td></tr>
</tbody></table>
&#10;

<br><b>Scoring.</b>
Valid words are scored according to their length, using this table:
&#10;

<center>
> 
>   <table cellspacing="0" cellpadding="3">
>     <tbody><tr class="border_bottom" align="center"><th>word length</th> <th>&nbsp;&nbsp;</th> <th>points</th></tr>
>     <tr align="center"><td>3–4</td> <td></td> <td>1</td></tr>
>     <tr align="center"><td>5</td>   <td></td> <td>2</td></tr>
>     <tr align="center"><td>6</td>   <td></td> <td>3</td></tr>
>     <tr align="center"><td>7</td>   <td></td> <td>5</td></tr>
>     <tr align="center"><td>8+</td>  <td></td> <td>11</td></tr>
>   </tbody></table>

</center>


&#10;<b>The <i>Qu</i> special case.</b>
In the English language, the letter `Q` is almost always followed by the letter
`U`.
Consequently, the side of one die is printed with the two-letter sequence `Qu`
instead of `Q` (and this two-letter sequence must be used together when forming words).
When scoring, `Qu` counts as two letters; for example, the word `QuEUE`
scores as a 5-letter word even though it is formed by following a sequence of only 4 dice.
&#10;

&#10;

<center>
![Qu](specification_files/Qu.png "Qu")
</center>


<br>
<b>Your task.</b> 
Your challenge is to write a Boggle solver that finds *all* valid words in a given Boggle board, using
a given dictionary.
Implement an immutable data type `BoggleSolver` with the following API:
&#10;

<pre>
> 
> <b>public class BoggleSolver
> {</b>
>     <font color="gray">// Initializes the data structure using the given array of strings as the dictionary.
>     // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)</font>
>     <b>public BoggleSolver(String[] dictionary)</b>
> 
>     <font color="gray">// Returns the set of all valid words in the given Boggle board, as an Iterable.</font>
>     <b>public Iterable&lt;String&gt; getAllValidWords(BoggleBoard board)</b>
> 
>     <font color="gray">// Returns the score of the given word if it is in the dictionary, zero otherwise.
>     // (You can assume the word contains only the uppercase letters A through Z.)</font>
>     <b>public int scoreOf(String word)</b>
> <b>}</b>

</pre>

It is up to you how you search for and store the words contained in the board,
as well as the dictionary used to check them.


<br>
<b>The board data type.</b>
We provide an immutable data type
[BoggleBoard.java](specification_files/BoggleBoard.java) for
representing Boggle boards.
It includes constructors for creating
Boggle boards from either the 16 Hasbro dice, the distribution of letters in
the English language, a file, or a character array; methods for accessing
the individual letters; and a method to print out the board for debugging.
Here is the full API:
&#10;

<pre>
> 
> <b>public class BoggleBoard
> {</b>
>     <font color="gray">// Initializes a random 4-by-4 Boggle board.
>     // (by rolling the Hasbro dice)</font>
>     <b>public BoggleBoard()</b>
> 
>     <font color="gray">// Initializes a random m-by-n Boggle board.
>     // (using the frequency of letters in the English language)</font>
>     <b>public BoggleBoard(int m, int n)</b>
> 
>     <font color="gray">// Initializes a Boggle board from the specified filename.</font>
>     <b>public BoggleBoard(String filename)</b>
> 
>     <font color="gray">// Initializes a Boggle board from the 2d char array.
>     // (with 'Q' representing the two-letter sequence "Qu")</font>
>     <b>public BoggleBoard(char[][] a)</b>
> 
>     <font color="gray">// Returns the number of rows.</font>
>     <b>public int rows()</b>
> 
>     <font color="gray">// Returns the number of columns.</font>
>     <b>public int cols()</b>
> 
>     <font color="gray">// Returns the letter in row i and column j.
>     // (with 'Q' representing the two-letter sequence "Qu")</font>
>     <b>public char getLetter(int i, int j)</b>
> 
>     <font color="gray">// Returns a string representation of the board.</font>
>     <b>public String toString()
> }</b>

</pre>


<b>Testing.</b> 
The zip file [boggle.zip](specification_files/boggle.zip) contains a number of sample boards
and test files.
&#10;&#10;

- *Dictionaries.*
    A dictionary consists of a sequence of words, separated by whitespace, in alphabetical order.
    You can assume that each word contains only the uppercase letters `A` through `Z`.
    For example, here are the two files
    [dictionary-algs4.txt](specification_files/dictionary-algs4.txt) and
    [dictionary-yawl.txt](specification_files/dictionary-yawl.txt):
    
    &#10;
    
    <blockquote>
    <pre class="terminal"><span class="prompt">~/Desktop/boggle&gt;</span> <span class="command">cat dictionary-algs4.txt</span>
    ABACUS
    ABANDON
    ABANDONED
    ABBREVIATE
    ...
    QUEUE
    ...
    ZOOLOGY
    
    <span class="prompt">~/Desktop/boggle&gt;</span> <span class="command">cat dictionary-yawl.txt</span>
    AA
    AAH
    AAHED
    AAHING
    ...
    PNEUMONOULTRAMICROSCOPICSILICOVOLCANOCONIOSIS
    ...
    ZYZZYVAS
    </pre>
    </blockquote>
    
    &#10;The former is a list of 6,013 words that appear in *Algorithms 4/e*; the latter is a comprehensive
    list of 264,061 English words (known as *Yet Another Word List*) that is widely used in word-game competitions.
    &#10;

- *Boggle boards.*
    A boggle board consists of two integers *m* and *n*, followed by the
 *m* × *n* characters in the board, with the integers and
    characters separated by whitespace.
    You can assume the integers are nonnegative and that the 
    characters are uppercase letters `A` through `Z`
    (with the two-letter sequence `Qu` represented as either `Q` or `Qu`).
    For example, here are the files [board4x4.txt](specification_files/board4x4.txt)
    and [board-q.txt](specification_files/board-q.txt):
    
    &#10;
    
    <blockquote>
    <pre class="terminal"><span class="prompt">~/Desktop/boggle&gt;</span> <span class="command">cat board4x4.txt</span>
    4 4
    A  T  E  E
    A  P  Y  O
    T  I  N  U
    E  D  S  E
    
    <span class="prompt">~/Desktop/boggle&gt;</span> <span class="command">cat board-q.txt</span>
    4 4
    S  N  R  T
    O  I  E  L
    E  Qu T  T
    R  S  A  T
    </pre>
    </blockquote>
    
    




The following test client takes the filename of a dictionary and the filename 
of a Boggle board as command-line arguments and prints out all valid words for
the given board using the given dictionary.

<pre><blockquote>
public static void main(String[] args) {
    In in = new In(args[0]);
    String[] dictionary = in.readAllStrings();
    BoggleSolver solver = new BoggleSolver(dictionary);
    BoggleBoard board = new BoggleBoard(args[1]);
    int score = 0;
    for (String word : solver.getAllValidWords(board)) {
        StdOut.println(word);
        score += solver.scoreOf(word);
    }
    StdOut.println("Score = " + score);
}
</blockquote>
</pre>

&#10;Here are two sample executions:
&#10;

&#10;
> 
> <pre class="terminal"><span class="prompt">~/Desktop/boggle&gt;</span> <span class="command">java-algs4 BoggleSolver dictionary-algs4.txt board4x4.txt</span>
> AID
> DIE
> END
> ENDS
> ...
> YOU
> Score = 33
> 
> <span class="prompt">~/Desktop/boggle&gt;</span> <span class="command">java-algs4 BoggleSolver dictionary-algs4.txt board-q.txt</span>
> EQUATION
> EQUATIONS
> ...
> QUERIES
> QUESTION
> QUESTIONS
> ...
> TRIES
> Score = 84
> </pre>


<b>Performance.</b> If you choose your data structures and algorithms judiciously, your program
can preprocess the dictionary and find all valid words in a random Hasbro board
(or even a random 10-by-10 board) in a fraction of a second.
To stress test the performance of your implementation, create one `BoggleSolver`
object (from a given dictionary); then, repeatedly generate and
solve random Hasbro boards. How many random Hasbro boards can you solve per second?
*For full credit, your program must be able to solve thousands of
random Hasbro boards per second.*
The goal on this assignment is raw speed—for example, it's fine to use 10× more memory if
the program is 10× faster.
&#10;

<b>Interactive game (optional, but fun and no extra work).</b>
Once you have a working version of `BoggleSolver.java`,
download, compile, and run [BoggleGame.java](specification_files/BoggleGame.java)
to play Boggle against a computer opponent.
To enter a word, either type it in the text box
or click the corresponding sequence of dice on the board.
The computer opponent has various levels of difficulty, ranging from finding 
only words from popular nursery rhymes (easy) to words that appear in *Algorithms 4/e* (medium)
to finding every valid word (humbling).
&#10;

&#10;

<center>
![Boggle GUI](specification_files/boggle-gui.png "Boggle GUI")
</center>


<b>Challenge for the bored.</b> Here are some challenges:


-  Find a maximum scoring 4-by-4 Hasbro board.
    Here is the [best known board](specification_files/board-points4540.txt) (4540 points),
    which was discovered by Robert McAnany in connection with this Coursera course.
    
-  Find a maximum scoring 4-by-4 Hasbro board using the
    [Zinga](specification_files/dictionary-zingarelli2005.txt) list
    of 584,983 Italian words.
    
-  Find a minimum scoring 4-by-4 Hasbro board.
    
    
-  Find a maximum scoring 5-by-5 Deluxe Boggle board.
    
-  Find a maximum scoring *n*-by-*n* board (not necessarily
    using the Hasbro dice) for different values of *n*.
    
-  Find a board with the most words (or the most words that are 8 letters or longer).
    
-  Find a 4-by-4 Hasbro board that scores *exactly* 2,500, 3,000, 3,500, or 4,000 points.
    
-  Design an algorithm to determine whether a given 4-by-4 board can be generated
    by rolling the 16 Hasbro dice.
    
- 
    How many words in the dictionary appear in no 4-by-4 Hasbro boards?
    
- 
    Add new features to [BoggleGame.java](specification_files/BoggleGame.java).
    
- 
    Extend your program to handle arbitrary Unicode letters and dictionaries. You may need to consider
    alternate algorithms and data structures.
    
- 
    Extend your program to handle arbitrary strings on the faces of the dice, generalizing your hack for
    dealing with the two-letter sequence `Qu`.
    


Unless otherwise stated, use the [dictionary-yawl.txt](specification_files/dictionary-yawl.txt) dictionary.
If you discover interesting boards,
you are encouraged to share and describe them in the Discussion Forums.




<b>Web submission.</b>
Submit a .zip file containing 
Submit `BoggleSolver.java` and any
other supporting files (excluding `BoggleBoard.java` and `algs4.jar`).
You may not call any library functions except those in
`java.lang`, `java.util`, and `algs4.jar`.
&#10;&#10;&#10;

<br>
&#10;&#10;

<address><small>
This assignment was developed by Matthew Drabick and Kevin Wayne, inspired by 
Todd Feldman and Julie Zelenski's classic
[Nifty Boggle](http://www-cs-faculty.stanford.edu/~zelenski/boggle/)
assignment from Stanford.

  
Copyright © 2013.
</small>
</address>


