package models;

/**
 * Created by Quantum on 2015-10-27.
 */
public class Edge {

    public int startVertex;
    public int endVertex;
    public int weight;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Edge) {
            Edge tmp = (Edge) obj;
            return tmp.endVertex == endVertex && tmp.startVertex == startVertex;
        }
        return false;
    }
}
