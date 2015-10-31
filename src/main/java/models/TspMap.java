package models;

import tools.Utils;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Quantum on 2015-10-12.
 */
public class TspMap {

    private int[][] matrix;
    private boolean symmetric;

    public TspMap(final int size, boolean symmetric) {
        this.symmetric = symmetric;
        matrix = new int[size][size];
    }

    public TspMap(final TspMap map) {
        matrix = Utils.cloneArray(map.matrix);
        this.symmetric = map.symmetric;
    }

    public int getEdgeCount() {
        return matrix.length;
    }

    public boolean isSymmetric() {
        return symmetric;
    }

    public void add(final TspEdge edge) {
        matrix[edge.vertex1][edge.vertex2] = edge.weight;
        if(symmetric) {
            matrix[edge.vertex2][edge.vertex1] = edge.weight;
        }
    }

    public void remove(final TspEdge edge) {
        matrix[edge.vertex1][edge.vertex2] = TspEdge.NO_EXIST;
        if(symmetric) {
            matrix[edge.vertex2][edge.vertex1] = TspEdge.NO_EXIST;
        }
        edge.invalidate();
    }

    public void update(final TspEdge edge) {
        add(edge);
    }

    public TspEdge get(int vertex1, int vertex2) {
        return new TspEdge(vertex1, vertex2, matrix[vertex1][vertex2]);
    }

    public List<TspEdge> getEdgesFor(int vertex) {
        List<TspEdge> edges = new LinkedList<>();
        int[] adjacentEdges = matrix[vertex];
        for(int i = 0; i < adjacentEdges.length; i++) {
            TspEdge tmpEdge = new TspEdge(vertex, i, adjacentEdges[i]);
            if(tmpEdge.isExist()) {
                edges.add(tmpEdge);
            }
        }
        return edges;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public int getSize() {
        return matrix.length;
    }
}
