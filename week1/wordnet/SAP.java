import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import java.util.Collections;

public class SAP {
    private final Digraph digraph;

    private static class CommonAncestorResult {
        public final int length;
        public final int ancestor;

        public CommonAncestorResult(int length, int ancestor) {
            this.length = length;
            this.ancestor = ancestor;
        }
    }

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException();
        }

        digraph = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        return length(Collections.singletonList(v), Collections.singletonList(w));
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        return ancestor(Collections.singletonList(v), Collections.singletonList(w));
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        CommonAncestorResult result = commonAncestor(v, w);

        return result.length;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        CommonAncestorResult result = commonAncestor(v, w);

        return result.ancestor;
    }

    private CommonAncestorResult commonAncestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }

        if (!v.iterator().hasNext() || !w.iterator().hasNext()) {
            return new CommonAncestorResult(-1, -1);
        }

        BreadthFirstDirectedPaths vBFS = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths wBFS = new BreadthFirstDirectedPaths(digraph, w);

        int minLen = Integer.MAX_VALUE;
        int minX = 0;
        for (int x = 0; x < digraph.V(); x++) {
            if (vBFS.hasPathTo(x) && wBFS.hasPathTo(x)) {
                int len = vBFS.distTo(x) + wBFS.distTo(x);
                if (len < minLen) {
                    minLen = len;
                    minX = x;
                }
            }
        }

        if (minLen == Integer.MAX_VALUE) {
            return new CommonAncestorResult(-1, -1);
        }

        return new CommonAncestorResult(minLen, minX);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        // empty
    }
}
