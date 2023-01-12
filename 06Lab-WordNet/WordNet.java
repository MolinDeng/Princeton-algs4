/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Topological;

import java.util.TreeMap;

public class WordNet {
    private final TreeMap<String, Bag<Integer>> noun2IDs;
    private final TreeMap<Integer, String> id2Noun;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();
        noun2IDs = new TreeMap<>();
        id2Noun = new TreeMap<>();
        In inSyn = new In(synsets);
        while (!inSyn.isEmpty()) {
            String line = inSyn.readLine();
            String[] fields = line.split(",");
            String[] nouns = fields[1].split(" ");
            int id = Integer.parseInt(fields[0]);
            for (String noun : nouns) {
                if (noun2IDs.containsKey(noun))
                    noun2IDs.get(noun).add(id);
                else {
                    Bag<Integer> b = new Bag<Integer>();
                    b.add(id);
                    noun2IDs.put(noun, b);
                }
            }
            id2Noun.put(id, fields[1]);
        }
        Digraph digraph = new Digraph(id2Noun.size());
        In inHyp = new In(hypernyms);
        while (!inHyp.isEmpty()) {
            String line = inHyp.readLine();
            String[] fields = line.split(",");
            int id = Integer.parseInt(fields[0]);
            for (int i = 1; i < fields.length; i++)
                digraph.addEdge(id, Integer.parseInt(fields[i]));
        }
        // check multi-roots
        int cnt = 0;
        for (int i = 0; i < digraph.V(); i++) {
            if (digraph.outdegree(i) == 0)
                if (++cnt > 1) throw new IllegalArgumentException();
        }
        // check cycle
        // DirectedCycle cycle = new DirectedCycle(digraph);
        // if (cycle.hasCycle()) throw new IllegalArgumentException();
        // check DAG
        Topological t = new Topological(digraph);
        if (!t.hasOrder()) throw new IllegalArgumentException();

        sap = new SAP(digraph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return noun2IDs.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return noun2IDs.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException();
        return sap.length(noun2IDs.get(nounA), noun2IDs.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException();
        int id = sap.ancestor(noun2IDs.get(nounA), noun2IDs.get(nounB));
        return id2Noun.get(id);
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
    }
}
