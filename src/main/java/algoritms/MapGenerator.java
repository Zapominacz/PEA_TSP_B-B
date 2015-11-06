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
                result[row][col] = random.nextInt(maxWeight) + 1;
            }
        }

        return result;
    }

}
