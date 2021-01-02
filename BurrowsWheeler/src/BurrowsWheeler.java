import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * This class represents a data structure that implements the Burrows–Wheeler transform.
 * It enables to transform a message into a form that is more amenable for compression
 * by rearranging the characters in the input so that
 * there are lots of clusters with repeated characters,
 * but in such a way that it is still possible to recover the original input.
 * The transform relies on the following intuition:
 * if we see the letters hen in English text, then, most of the time, the letter preceding it is either t or w.
 */
public class BurrowsWheeler {
    private static final int RADIX = 256;

    /**
     * Read input string in the data from of bytes from standard input,
     * applying Burrows-Wheeler transform, which is the last column in the sorted suffixes array t[],
     * preceded by the row number first in which the original string ends up.
     * And then writing to standard output.
     */
    public static void transform() {
        String input = BinaryStdIn.readString();
        BinaryStdIn.close();
        CircularSuffixArray sortedSuffixesArray = new CircularSuffixArray(input);
        int first = 0;
        char[] output = new char[input.length()];
        for (int i = 0; i < sortedSuffixesArray.length(); i++) {
            if (sortedSuffixesArray.index(i) == 0) {
                first = i;
                output[i] = input.charAt(sortedSuffixesArray.length() - 1);
            } else {
                output[i] = input.charAt(sortedSuffixesArray.index(i) - 1);
            }
        }
        BinaryStdOut.write(first);
        BinaryStdOut.write(new String(output));
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output

    /**
     * Reads input string in the data from of bytes from standard input,
     * applying Burrows-Wheeler inverse transform:
     *  If the jth original suffix (original string, shifted j characters to the left)
     *  is the ith row in the sorted order, we define next[i] to be the row in the sorted order,
     *  where the (j + 1)st original suffix appears.
     *  For example, if first is the row in which the original input string appears,
     *  then next[first] is the row in the sorted order where the 1st original suffix (the original string left-shifted by 1) appears;
     *  next[next[first]] is the row in the sorted order where the 2nd original suffix appears;
     *  next[next[next[first]]] is the row where the 3rd original suffix appears; and so forth.
     */
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();
        BinaryStdIn.close();
        char[] firstChars = s.toCharArray();
        indexSort(firstChars);
        Map<Character, Queue<Integer>> map = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            Queue<Integer> queue = map.getOrDefault(ch, new LinkedList<>());
            if (queue == null) {
                queue = new LinkedList<>();
            }
            queue.add(i);
            map.put(ch, queue);
        }

        int[] next = new int[s.length()];
        for (int i = 0; i < next.length; i++) {
            // noinspection ConstantConditions
            next[i] = map.get(firstChars[i]).poll();
        }

        for (int i = 0; i < s.length(); i++) {
            BinaryStdOut.write(firstChars[first]);
            first = next[first];
        }
        BinaryStdOut.close();
    }

    private static void indexSort(char[] array) {
        int[] count = new int[RADIX + 1];
        char[] aux = new char[array.length];

        for (char c : array) {
            count[c + 1]++;
        }
        for (int r = 0; r < RADIX; r++) {
            count[r + 1] += count[r];
        }

        for (char c : array) {
            aux[count[c]++] = c;
        }
        System.arraycopy(aux, 0, array, 0, array.length);
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) {
            transform();
        } else {
            inverseTransform();
        }
    }
}