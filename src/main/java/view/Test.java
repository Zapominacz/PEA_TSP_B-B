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

    private static final int REPETITIONS = 10;
    private static final int[] SIZES = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};

    private PrintWriter printWriter;
    private BranchAndBound branchAndBound;
    private FullCheck fullCheck;
    private MapGenerator mapLoader;

    public static void main(String[] args) throws FileNotFoundException {
        new Test();
    }

    public Test() throws FileNotFoundException {
        prepareTest();
        for (int size : SIZES) {
            int repetition = REPETITIONS;
            int[][] baseMap = mapLoader.generate(size);
            long bnbTime = 0;
            long fcTime = 0;
            while (repetition-- > 0) {
                Instant startbab = Instant.now();
                branchAndBound.performAlgorithm(baseMap);
                Instant endbab = Instant.now();
                bnbTime += Duration.between(startbab, endbab).toMillis();
                Instant startfc = Instant.now();
                fullCheck.performAlgorithm(baseMap);
                Instant endfc = Instant.now();
                fcTime += Duration.between(startfc, endfc).toMillis();
            }
            printWriter.write(size + " bnb " + (double) bnbTime / (double) REPETITIONS);
            printWriter.write(size + " fc " + (double) fcTime / (double) REPETITIONS);
            printWriter.flush();
        }
    }

    private void prepareTest() throws FileNotFoundException {
        File results = new File("results.txt");
        printWriter = new PrintWriter(results);
        branchAndBound = new BranchAndBound();
        fullCheck = new FullCheck();
        mapLoader = new MapGenerator();
    }
}
