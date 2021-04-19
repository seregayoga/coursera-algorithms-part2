import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] alphabet = new char[R];
        for (char i = 0; i < R; i++) {
            alphabet[i] = i;
        }

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar(8);
            char first = alphabet[0];

            if (first == c) {
                BinaryStdOut.write(0, 8);
                continue;
            }

            char prev = first;
            alphabet[0] = c;
            for (int i = 1; i < R; i++) {
                char tmp = alphabet[i];
                alphabet[i] = prev;
                prev = tmp;
                if (prev == c) {
                    BinaryStdOut.write(i, 8);
                    break;
                }
            }
        }

        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] alphabet = new char[R];
        for (char i = 0; i < R; i++) {
            alphabet[i] = i;
        }

        // 0 1 2 3 4 5
        while (!BinaryStdIn.isEmpty()) {
            int x = BinaryStdIn.readInt(8);
            BinaryStdOut.write(alphabet[x]);

            char prev = alphabet[0];
            alphabet[0] = alphabet[x];
            for (int i = 1; i <= x; i++) {
                char tmp = alphabet[i];
                alphabet[i] = prev;
                prev = tmp;
            }
        }

        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        } else if (args[0].equals("+")) {
            decode();
        } else {
            throw new IllegalArgumentException("Illegal command line argument");
        }
    }
}
