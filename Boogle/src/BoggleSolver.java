import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.TreeSet;

public class BoggleSolver {
    // The possible points for each word.
    private static final int[] POINTS = {0, 1, 2, 3, 5};
    // The corresponding maximum length for each point.
    // Tor example, if a word's length is <=2, then its point is 0, If its length is in [3, 4], then its point is 4.
    private static final int[] wordLengthBounds = {2, 4, 5, 6, 7};
    // The maximum  available points, which is for word with length >= 8
    private static final int MAX_POINTS = 11;
    // All possible row movements.
    private static final int[] MOVEROW = {-1, -1, -1, 0, 0, 1, 1, 1};
    // All possible col movements. For all possible integer i, MOVEROW[i]MOVECOL[i] represents all possible movement.
    private static final int[] MOVECOL = {-1, 0, 1, -1, 1, -1, 0, 1};
    private final Trie root;

    /**
     * Initializes the data structure using the given array of strings as the dictionary.
     * We assume each word in the dictionary contains only the uppercase letters A through Z.
     * @param dictionary the dictionary we used for the boggle game
     */
    public BoggleSolver(String[] dictionary) {
        root = new Trie();
        for (String word : dictionary)
            root.addWord(word);
    }

    /**
     * A recursive method that search for and store the words contained in the board
     * @param words the set of found possible words.
     * @param currWord the current word we are trying to search for.
     * @param currState the current trie state.
     * @param board the board we used to search.
     * @param visible the visible letters.
     * @param row the row of the current letter.
     * @param col the column of the current letter.
     */
    private void recur(TreeSet<String> words, StringBuilder currWord, Trie currState, BoggleBoard board, boolean[][] visible, int row, int col) {
        char currLetter = board.getLetter(row, col);

        currState = currState.getNext(currLetter);
        if (currState == null) {
            return;
        }
        if (currLetter == 'Q') {
            if ((currState = currState.getNext('U')) == null) {
                return;
            }
        }
        visible[row][col] = true;
        currWord.append(currLetter);
        if (currLetter == 'Q') {
            currWord.append('U');
        }
        if (scoreOf(currWord.toString()) != 0) {
            words.add(currWord.toString());
        }
        for (int i = 0; i < MOVEROW.length; i++) {
            int nextRow = row + MOVEROW[i];
            int nextCol = col + MOVECOL[i];
            if (nextRow < 0 || nextRow >= board.rows() || nextCol < 0 || nextCol >= board.cols() || visible[nextRow][nextCol]) {
                continue;
            }
            recur(words, currWord, currState, board, visible, nextRow, nextCol);
        }
        if (currLetter == 'Q') {
            currWord.setLength(currWord.length() - 2);
        } else {
            currWord.setLength(currWord.length() - 1);
        }
        visible[row][col] = false;
    }

    /**
     * Returns the set of all valid words in the given Boggle board, as an Iterable.
     * @param board
     * @return an Iterable set of all valid words in the given Boggle board.
     */
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        TreeSet<String> words = new TreeSet<>();
        StringBuilder currWord = new StringBuilder();
        boolean[][] visible = new boolean[board.rows()][board.cols()];
        for (int row = 0; row < board.rows(); row++) {
            for (int col = 0; col < board.cols(); col++) {
                recur(words, currWord, root, board, visible, row, col);
            }
        }
        return words;
    }

    /**
     * Returns the score of the given word if it is in the dictionary, zero otherwise.
     * We assume the word contains only the uppercase letters A through Z.
     * @param word
     * @return the score of the given word if it is in the dictionary, zero otherwise.
     */
    public int scoreOf(String word) {
        if (!root.isWord(word)) {
            return 0;
        }
        for (int i = 0; i < POINTS.length; i++) {
            if (word.length() <= wordLengthBounds[i]) {
                return POINTS[i];
            }
        }
        return MAX_POINTS;
    }

    public static void main(String[] args) {
        In in = new In("testing/dictionary-algs4.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard("testing/board-q.txt");
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}