package algoritms;


/**
 * Created by Quantum on 2015-10-12.
 */
public class FullCheck {

    private int[] bestCityOrder;
    private int currentBestCost;
    private int[][] baseMap;

    public int[] performAlgorithm(final int[][] map) {
        prepareForAlgorithm(map);
        int[] cityOrder = makeFirstPermutation();
        permuteCities(cityOrder, 0);
        return bestCityOrder;
    }

    private int[] makeFirstPermutation() {
        final int cityCount = baseMap.length;
        int[] result = new int[cityCount];
        for (int i = 0; i < cityCount; i++) {
            result[i] = i;
            bestCityOrder[i] = i;
        }
        return result;
    }

    private void prepareForAlgorithm(int[][] map) {
        baseMap = map;
        bestCityOrder = new int[map.length];
        currentBestCost = Integer.MAX_VALUE;
    }

    private void swapCities(int[] cityOrder, int first, int second) {
        int tmp = cityOrder[first];
        cityOrder[first] = second;
        cityOrder[second] = tmp;
    }

    private void permuteCities(int[] cityOrder, int iteration) {
        if (iteration == cityOrder.length) {
            checkIsBetterPermutation(cityOrder);
        } else {
            for (int i = iteration; i < cityOrder.length; i++) {
                swapCities(cityOrder, iteration, i);
                permuteCities(cityOrder, i + 1);
                swapCities(cityOrder, iteration, i);
            }
        }
    }

    private void checkIsBetterPermutation(int[] cityOrder) {
        int totalCost = 0;
        int edgeCost = baseMap[cityOrder[cityOrder.length - 1]][cityOrder[0]];
        if (edgeCost > 0) {
            totalCost += edgeCost;
        } else {
            return;
        }
        for (int i = 1; i < cityOrder.length; i++) {
            edgeCost = baseMap[cityOrder[i - 1]][cityOrder[i]];
            if (edgeCost > 0) {
                totalCost += edgeCost;
            } else {
                return;
            }
        }
        if (totalCost < currentBestCost) {
            currentBestCost = totalCost;
            System.arraycopy(cityOrder, 0, bestCityOrder, 0, cityOrder.length);
        }
    }

}
