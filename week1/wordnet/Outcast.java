public class Outcast {
    private final WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int maxDistance = Integer.MIN_VALUE;
        String outcastNoun = "";

        for (String noun : nouns) {
            int distance = 0;
            for (String nounI : nouns) {
                if (nounI.equals(noun)) {
                    continue;
                }

                distance += wordnet.distance(noun, nounI);
            }

            if (distance > maxDistance) {
                maxDistance = distance;
                outcastNoun = noun;
            }
        }

        return outcastNoun;
    }

    // see test client below
    public static void main(String[] args) {
        // empty
    }
}
