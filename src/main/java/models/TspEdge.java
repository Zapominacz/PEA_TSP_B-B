package models;

import java.util.Comparator;

/**
 * Created by Quantum on 2015-10-12.
 */
public class TspEdge {

    public final static int NO_EXIST = 0;

    public int vertex1;
    public int vertex2;
    public int weight;

    public TspEdge(int vertex1, int vertex2, int weight) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.weight = weight;
    }

    public TspEdge(int vertex1, int vertex2) {
        this(vertex1, vertex2, NO_EXIST);
    }


    public boolean isExist() {
        return weight > NO_EXIST;
    }

    public void invalidate() {
        weight = NO_EXIST;
    }

    public int getWeight() {
        return weight;
    }

    public static class AscendingWeightComparator implements Comparator<TspEdge> {
        @Override
        public int compare(TspEdge first, TspEdge second) {
            return Integer.compare(second.weight, first.weight);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TspEdge) {
            TspEdge edge = (TspEdge) obj;
            if((edge.vertex1 == vertex1 && edge.vertex2 == vertex2) || (edge.vertex1 == vertex2 && edge.vertex2 == vertex1)) {
                return weight == edge.weight;
            }
        }
        return false;
    }
}
