package models;

/**
 * Created by Quantum on 2015-10-12.
 */
public class Node {

    public int[][] original;
    public int[][] reduced;

    public Edge[] solution;
    public int[] nodeUnion;

    public float lowerBound;
    public boolean leftChild;

    public Node(final int[][] matrix) {
        this.original = matrix;
        leftChild = false;
        lowerBound = 0;
    }

    public int union(int v1, int v2) {
        int number = Math.min(v1, v2);
        for (int i = 0; i < nodeUnion.length; i++) {
            if (nodeUnion[i] == v1 || nodeUnion[i] == v2) {
                nodeUnion[i] = number;
            }
        }
        return number;
    }
}
