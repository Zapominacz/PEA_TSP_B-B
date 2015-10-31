package models;

import java.util.List;

/**
 * Created by Quantum on 2015-10-12.
 */
public class Node {

    public int[][] original;
    public int[][] reduced;
    public Edge[] solution;
    public float lowerBound;
    public boolean leftChild;

    public Node(final int[][] matrix) {
        this.original = matrix;
        leftChild = false;
        lowerBound = 0;
    }
}
