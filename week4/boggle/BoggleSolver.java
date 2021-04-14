import java.util.HashSet;
import java.util.Set;

public class BoggleSolver {
    private final Trie trie;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        trie = new Trie();
        for (String word : dictionary) {
            trie.add(word);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        Set<String> words = new HashSet<>();

        boolean[][] visited = new boolean[board.rows()][board.cols()];
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                backtrack(board, i, j, "", words, visited);
            }
        }

        return words;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!trie.contains(word) || word.length() < 3) {
            return 0;
        } else if (word.length() <= 4) {
            return 1;
        } else if (word.length() == 5) {
            return 2;
        } else if (word.length() == 6) {
            return 3;
        } else if (word.length() == 7) {
            return 5;
        }
        return 11;
    }

    private void backtrack(BoggleBoard board, int i, int j, String word, Set<String> words, boolean[][] visited) {
        if (i < 0 || i >= board.rows() || j < 0 || j >= board.cols()) {
            return;
        }
        if (visited[i][j]) {
            return;
        }
        visited[i][j] = true;

        char letter = board.getLetter(i, j);
        word += letter;
        // Special case for "Qu"
        if (letter == 'Q') {
            word += 'U';
        }
        if (word.length() >= 3 && trie.contains(word)) {
            words.add(word);
        }

        if (!trie.keysWithPrefixExist(word)) {
            visited[i][j] = false;
            return;
        }

        // adjacent letters clockwise starting from top
        backtrack(board, i - 1, j, word, words, visited);
        backtrack(board, i - 1, j + 1, word, words, visited);
        backtrack(board, i, j + 1, word, words, visited);
        backtrack(board, i + 1, j + 1, word, words, visited);
        backtrack(board, i + 1, j, word, words, visited);
        backtrack(board, i + 1, j - 1, word, words, visited);
        backtrack(board, i, j - 1, word, words, visited);
        backtrack(board, i - 1, j - 1, word, words, visited);

        visited[i][j] = false;
    }

    public static void main(String[] args) {
        // empty
    }
}
