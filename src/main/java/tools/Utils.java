package tools;

import models.Edge;

/**
 * Created by Quantum on 2015-10-12.
 */
public class Utils {

    public static int[][] cloneArray(final int[][] src) {
        int length = src.length;
        int[][] target = new int[length][length];
        for (int i = 0; i < length; i++) {
            System.arraycopy(src[i], 0, target[i], 0, length);
        }
        return target;
    }

    public static int columnMin(final int[][] matrix, final int col) {
        int min = matrix[0][col];
        for(int i = 1; i < matrix.length; i++) {
            if(min > matrix[i][col]) {
                min = matrix[i][col];
            }
        }
        return min;
    }

    public static int[] cloneArray(final int[] array) {
        int[] result = new int[array.length];
        System.arraycopy(array, 0, result, 0, array.length);
        return result;
    }

    public static int rowMin(final int[][] matrix, final int row) {
        int min = matrix[row][0];
        for(int i = 1; i < matrix.length; i++) {
            if(min > matrix[row][i]) {
                min = matrix[row][i];
            }
        }
        return min;
    }

    public static Edge[] cloneEdges(Edge[] solution) {
        Edge[] result = new Edge[solution.length];
        System.arraycopy(solution, 0, result, 0, solution.length);
        return result;
    }
}
