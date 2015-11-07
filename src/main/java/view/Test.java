package view;

import algoritms.BranchAndBound;
import algoritms.FullCheck;
import algoritms.MapGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;

/**
 * Created by Quantum on 2015-11-06.
 */
public class Test {
    private final static int INF = Integer.MAX_VALUE;
    private static final int REPETITIONS = 10;
    private static final int[] SIZES = {5};

    private static final int[][] matrix1 = {
            {INF, 75, 52, 26, 68},
            {69, INF, 92, 87, 82},
            {25, 70, INF, 79, 96},
            {77, 68, 31, INF, 31},
            {46, 76, 47, 63, INF}
    };

    final int[][] matrix = {
            {INF, 3, 4, 2, 7},
            {3, INF, 4, 6, 3},
            {4, 4, INF, 5, 8},
            {2, 6, 5, INF, 6},
            {7, 3, 8, 6, INF}
    };
    final int[][] matrix2 = {
            {INF, 27, 43, 16, 30, 26},
            {7, INF, 16, 1, 30, 25},
            {20, 13, INF, 35, 5, 0},
            {21, 16, 25, INF, 18, 18},
            {12, 46, 27, 48, INF, 5},
            {23, 5, 5, 9, 5, INF}
    };

    private static final int[][] matrix3 = {
            {INF, 75, 52, 26, 68},
            {69, INF, 92, 87, 82},
            {25, 70, INF, 79, 96},
            {77, 68, 31, INF, 31},
            {46, 76, 47, 63, INF}
    };

    private PrintWriter printWriter;
    private BranchAndBound branchAndBound;
    private FullCheck fullCheck;
    private MapGenerator mapLoader;

    public static void main(String[] args) throws FileNotFoundException {
        new Test();
    }

    public Test() throws FileNotFoundException {
        prepareTest();
        //for (int size : SIZES) {
        int size = matrix3.length;
        int[] sol1 = null;
        int[] sol2 = null;
            int repetition = REPETITIONS;
        int[][] baseMap = matrix3;//mapLoader.generate(size);
            long bnbTime = 0;
            long fcTime = 0;
        Instant startbab = Instant.now();
        sol1 = branchAndBound.performAlgorithm(baseMap);
        Instant endbab = Instant.now();
        bnbTime += Duration.between(startbab, endbab).toNanos();
        Instant startfc = Instant.now();
        sol2 = fullCheck.performAlgorithm(baseMap);
        Instant endfc = Instant.now();
        fcTime += Duration.between(startfc, endfc).toNanos();
        printWriter.write(size + " bnb " + ((double) bnbTime / (double) REPETITIONS) + " " + sol1 + "\n");
        printWriter.write(size + " fc " + ((double) fcTime / (double) REPETITIONS) + " " + sol2 + "\n");
            printWriter.flush();
        //}
    }

    private void prepareTest() throws FileNotFoundException {
        File results = new File("results.txt");
        printWriter = new PrintWriter(results);
        branchAndBound = new BranchAndBound();
        fullCheck = new FullCheck();
        mapLoader = new MapGenerator();
    }
}
