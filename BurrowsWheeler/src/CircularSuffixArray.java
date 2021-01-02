import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import edu.princeton.cs.algs4.Quick3string;

/**
 * This class represents a data structure that describes the abstraction of
 * a sorted array of n circular suffixes of a string of length n.
 * We define index(i) to be the index of the original suffix that appears ith in the sorted array.
 */
public class CircularSuffixArray {
    private final String s;
    private final int[] suffixArray;

    /**
     * Create a circular suffix array of string s
     * @param s
     */
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException("The input string is null.");
        }
            this.s = s;
        suffixArray = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            suffixArray[i] = i;
        }
        sort(s, 0, s.length() - 1, 0);
    }


    /**
     * A helper method that find the char at the pivot position of a string.
     * @param s a string
     * @param i the index of lo
     * @param d dth character's index that the sort starting at
     * @return the index of the pivot position
     */
    private char charAt(String s, int i, int d) {
        return s.charAt((i + d) % s.length());
    }

    /**
     * A helper method that swap 2 index's value in a given int array.
     * @param arr the given array
     * @param i the 1st index
     * @param j the 2nd index
     */
    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // [lo, lt) < pivot
    // [lt, gt] = pivot
    // (gt, hi] > pivot

    /**
     * A customized quicksort with 3-way partitioning to sort a given string s starting at dth character.
     * @param s the given string.
     * @param lo the lower bound index.
     * @param hi the upper bound index.
     * @param d starting index.
     */

    private void sort(String s, int lo, int hi, int d) {
        if (hi <= lo || d >= s.length()) {
            return;
        }
        int lt = lo;
        int gt = hi;
        int pivot = charAt(s, suffixArray[lo], d);
        int eq = lo + 1;
        while (eq <= gt) {
            int curr = charAt(s, suffixArray[eq], d);
            if (curr < pivot) {
                swap(suffixArray, lt++, eq++);
            }
            else if (curr > pivot) {
                swap(suffixArray, eq, gt--);
            }
            else {
                eq++;
            }
        }
        sort(s, lo, lt - 1, d);
        sort(s, lt, gt, d + 1);
        sort(s, gt + 1, hi, d);
    }


    /**
     * Return the length of s
     * @return the length of s
     */
    public int length() {
        return s.length();
    }

    // returns index of ith sorted suffix

    /**
     * Returns the index of ith sorted suffix
     * @param i
     * @return the index of ith sorted suffix
     */
    public int index(int i) {
        if (i < 0 || i >= length()) {
            throw new IllegalArgumentException("The input index is out of bounds.");
        }
        return suffixArray[i];
    }

    /**
     * Unit testing
     * @param args arguments from standard input
     */
    public static void main(String[] args) {
        String s = "ABRACADABRA!";
        CircularSuffixArray csa = new CircularSuffixArray(s);
        s += s;
        for (int i = 0; i < csa.length(); i++) {
            System.out.println(s.substring(csa.index(i), csa.index(i) + csa.length()));
        }
    }
}