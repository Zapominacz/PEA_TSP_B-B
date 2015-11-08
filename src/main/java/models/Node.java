package models;

/**
 * Created by Quantum on 2015-10-12.
 */
public class Node {

    public int[][] matrix;

    public Edge[] solution;
    public int[] nodeUnion;
    public int[] startEdges;
    public int[] endEdges;

    public int added = 0;
    public float lowerBound;

    public Node(final int[][] matrix) {
        this.matrix = matrix;
        lowerBound = 0;
    }

    public int union(int v1, int v2) {
        int cls1 = nodeUnion[v1];
        int cls2 = nodeUnion[v2];
        int number = Math.min(cls1, cls2);
        for (int i = 0; i < nodeUnion.length; i++) {
            if (nodeUnion[i] == cls1 || nodeUnion[i] == cls2) {
                nodeUnion[i] = number;
            }
        }
        return number;
    }
}
