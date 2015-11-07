package algoritms;

import java.util.Random;

/**
 * Created by Quantum on 2015-11-06.
 */
public class MapGenerator {

    private Random random;
    private int maxWeight;

    public MapGenerator() {
        random = new Random();
        maxWeight = 100;
    }

    public MapGenerator(int maxWeight) {
        random = new Random();
        this.maxWeight = maxWeight;
    }

    public int[][] generate(int size) {
        int[][] result = new int[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (row == col) {
                    result[row][col] = Integer.MAX_VALUE;
                    System.out.print(Integer.MAX_VALUE + " ");
                } else {
                    int value = random.nextInt(maxWeight) + 1;
                    result[row][col] = value;
                    System.out.print(value + " ");
                }
            }
            System.out.print('\n');
        }
        return result;
    }

}
