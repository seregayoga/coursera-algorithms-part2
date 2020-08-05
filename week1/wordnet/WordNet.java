import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Topological;
import java.util.ArrayList;
import java.util.HashMap;

public class WordNet {
    private final HashMap<String, ArrayList<Integer>> nouns;
    private final ArrayList<String> synsetsArray;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }

        nouns = new HashMap<>();
        synsetsArray = new ArrayList<>();

        In synsetsStream = new In(synsets);
        while (synsetsStream.hasNextLine()) {
            String line = synsetsStream.readLine();

            // fields are splitted by ","
            String[] splittedLines = line.split(",");

            int id = Integer.parseInt(splittedLines[0]);

            // nouns are splitted by " "
            for (String noun : splittedLines[1].split(" ")) {
                if (nouns.containsKey(noun)) {
                    nouns.get(noun).add(id);
                } else {
                    ArrayList<Integer> ids = new ArrayList<>();
                    ids.add(id);
                    nouns.put(noun, ids);
                }
            }

            synsetsArray.add(splittedLines[1]);
        }

        Digraph digraph = new Digraph(synsetsArray.size());

        In hypernymsStream = new In(hypernyms);
        while (hypernymsStream.hasNextLine()) {
            String line = hypernymsStream.readLine();

            // fields are splitted by ","
            String[] splittedLines = line.split(",");

            int id = Integer.parseInt(splittedLines[0]);
            // zero element is synset id so we start from 1
            for (int i = 1; i < splittedLines.length; i++) {
                int hypernymId = Integer.parseInt(splittedLines[i]);
                digraph.addEdge(id, hypernymId);
            }
        }

        // check digraph is acyclic
        Topological topological = new Topological(digraph);
        if (!topological.hasOrder()) {
            throw new IllegalArgumentException();
        }

        // check DAG has only one root (rooted DAG)
        int roots = 0;
        for (int x = 0; x < digraph.V(); x++) {
            if (digraph.outdegree(x) == 0) {
                roots++;
            }
        }
        if (roots > 1) {
            throw new IllegalArgumentException();
        }

        sap = new SAP(digraph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nouns.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }

        return nouns.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }

        return sap.length(nouns.get(nounA), nouns.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }

        int ancestor = sap.ancestor(nouns.get(nounA), nouns.get(nounB));

        return synsetsArray.get(ancestor);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        // empty
    }
}
