package tools;

import java.io.File;
import java.util.Scanner;

/**
 * Created by Quantum on 2015-11-06.
 */
public class MapLoader {

    public MapLoader() {

    }

    public int[][] loadMap(File mapFile) {
        int[][] result;
        try {
            Scanner scanner = new Scanner(mapFile);
            int size = scanner.nextInt();
            result = new int[size][size];
            for (int row = 0; row < size; row++) {
                for (int col = 0; col < size; col++) {
                    int cost = scanner.nextInt();
                    if (cost < 0) {
                        cost = Integer.MAX_VALUE;
                    }
                    result[row][col] = cost;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = new int[0][0];
        }
        return result;
    }

}
