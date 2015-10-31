package tools;

/**
 * Created by Quantum on 2015-10-12.
 */
public class Utils {

    public static int[][] cloneArray(final int[][] src) {
        int length = src.length;
        int[][] target = new int[length][src[0].length];
        for (int i = 0; i < length; i++) {
            System.arraycopy(src[i], 0, target[i], 0, src[i].length);
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

    public static int rowMin(final int[][] matrix, final int row) {
        int min = matrix[row][0];
        for(int i = 1; i < matrix.length; i++) {
            if(min > matrix[row][i]) {
                min = matrix[row][i];
            }
        }
        return min;
    }

    public static boolean xor(boolean x, boolean y) {
        return (x && !y) || (!x && y);
    }
}
