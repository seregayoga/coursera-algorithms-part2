import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    private static final int R = 256;

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray suffixArray = new CircularSuffixArray(s);

        int first = 0;
        for (int i = 0; i < suffixArray.length(); i++) {
            if (suffixArray.index(i) == 0) {
                first = i;
                break;
            }
        }

        BinaryStdOut.write(first);
        for (int i = 0; i < suffixArray.length(); i++) {
            char c = s.charAt((s.length() - 1 + suffixArray.index(i)) % s.length());
            BinaryStdOut.write(c);
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String sTransformed = BinaryStdIn.readString();
        int len = sTransformed.length();

        char[] t = new char[len];
        for (int i = 0; i < len; i++) {
            t[i] = sTransformed.charAt(i);
        }

        int[] next = new int[len];

        // radix sort
        int[] count = new int[R + 1];
        for (int i = 0; i < len; i++) {
            count[t[i] + 1]++;
        }
        for (int r = 0; r < R; r++) {
            count[r + 1] += count[r];
        }
        for (int i = 0; i < len; i++) {
            next[count[t[i]]++] = i;
        }

        // array of first letters
        char[] f = new char[len];
        int index = first;
        for (int i = 0; i < len; i++) {
            f[(i + len - 1) % len] = t[index];
            index = next[index];
        }

        for (int i = 0; i < len; i++) {
            BinaryStdOut.write(f[i]);
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            transform();
        } else if (args[0].equals("+")) {
            inverseTransform();
        } else {
            throw new IllegalArgumentException("Illegal command line argument");
        }
    }
}
