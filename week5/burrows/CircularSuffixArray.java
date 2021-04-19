import java.util.Arrays;

// Partially adapted from SuffixArray.java
public class CircularSuffixArray {
    private final CircularSuffix[] suffixes;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }

        suffixes = new CircularSuffix[s.length()];
        for (int i = 0; i < s.length(); i++) {
            suffixes[i] = new CircularSuffix(s, i);
        }

        Arrays.sort(suffixes);
    }

    private static class CircularSuffix implements Comparable<CircularSuffix> {
        private final String s;
        private final int index;

        private CircularSuffix(String s, int index) {
            this.s = s;
            this.index = index;
        }
        private int length() {
            return s.length();
        }
        private char charAt(int i) {
            return s.charAt((index + i) % s.length());
        }

        public int compareTo(CircularSuffix that) {
            if (this == that) {
                return 0;
            }

            for (int i = 0; i < this.length(); i++) {
                if (this.charAt(i) < that.charAt(i)) {
                    return -1;
                }
                if (this.charAt(i) > that.charAt(i)) {
                    return +1;
                }
            }

            return 0;
        }
    }

    // length of s
    public int length() {
        return suffixes.length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= suffixes.length) {
            throw new IllegalArgumentException();
        }
        return suffixes[i].index;
    }

    // unit testing (required)
    public static void main(String[] args) {
        // empty
    }
}
