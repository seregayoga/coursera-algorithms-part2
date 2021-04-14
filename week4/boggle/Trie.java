// 26-way trie for upper case letters A-Z
// Adapted from TrieSET.java
public class Trie {
    private static final int R = 26;

    private Node root;

    private static class Node {
        private Node[] next = new Node[R];
        private boolean isString;
    }

    public boolean contains(String key) {
        Node x = get(root, key, 0);
        if (x == null) {
            return false;
        }
        return x.isString;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) {
            return null;
        }
        if (d == key.length()) {
            return x;
        }
        char c = key.charAt(d);
        int i = charToIndex(c);
        return get(x.next[i], key, d+1);
    }

    public void add(String key) {
        root = add(root, key, 0);
    }

    private Node add(Node x, String key, int d) {
        if (x == null) {
            x = new Node();
        }
        if (d == key.length()) {
            x.isString = true;
        } else {
            char c = key.charAt(d);
            int i = charToIndex(c);
            x.next[i] = add(x.next[i], key, d+1);
        }
        return x;
    }

    public boolean keysWithPrefixExist(String prefix) {
        Node x = get(root, prefix, 0);
        if (x == null) {
            return false;
        }
        for (int i = 0; i < R; i++) {
            if (x.next[i] != null) {
                return true;
            }
        }
        return false;
    }

    private int charToIndex(char c) {
        return c - 'A';
    }
}
