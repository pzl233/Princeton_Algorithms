import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

/**
 * This class represents a data structure that maintains an ordered sequence of f the 256 extended ASCII characters
 * by repeatedly reading a character from the input message;
 * printing the position in the sequence in which that character appears;
 * and moving that character to the front of the sequence.
 * If equal characters occur near one another other many times in the input,
 * then many of the output values will be small integers (such as 0, 1, and 2).
 * The resulting high frequency of certain characters (0s, 1s, and 2s) provides the kind of input
 * for which Huffman coding achieves favorable compression ratios.
 */
public class MoveToFront {
    private static final int RADIX = 256;

    /**
     * Initialize the sequence by making the ith character in the sequence equal to the ith extended ASCII character.
     * Read each 8-bit character c from standard input, one at a time;
     * output the 8-bit index in the sequence where c appears;
     * and move c to the front.
     */
    public static void encode() {
        int[] values = new int[RADIX];
        for (int i = 0; i < RADIX; i++) {
            values[i] = i;
        }
        while (!BinaryStdIn.isEmpty()) {
            int in = BinaryStdIn.readByte() & 0xFF;
            int prev = values[0];
            int i = 0;
            for (; i < RADIX; i++) {
                if (i > 0) {
                    int temp = values[i];
                    values[i] = prev;
                    prev = temp;
                }
                if (in == prev) {
                    break;
                }
            }
            values[0] = in;
            BinaryStdOut.write((byte)i);
        }
        BinaryStdOut.close();
    }

    /**
     * Initialize an ordered sequence of 256 characters, where extended ASCII character i appears ith in the sequence.
     * Read each 8-bit character i (but treat it as an integer between 0 and 255) from standard input one at a time;
     * write the ith character in the sequence; and move that character to the front.
     * Check that the decoder recovers any encoded message.
     */
    public static void decode() {
        int[] values = new int[RADIX];
        for (int i = 0; i < RADIX; i++) {
            values[i] = i;
        }
        while (!BinaryStdIn.isEmpty()) {
            int in = BinaryStdIn.readByte() & 0xFF;
            int last = values[in];
            for (int i = in - 1; i >= 0; i--)
                values[i + 1] = values[i];
            values[0] = last;
            BinaryStdOut.write((char)last);
        }
        BinaryStdOut.close();
    }

    /**
     * If args[0] is '-', apply move-to-front encoding
     * If args[0] is '+', apply move-to-front decoding
     * @param args arguments from standard input
     */
    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException("Expected either '-' or '+'");
    }
}