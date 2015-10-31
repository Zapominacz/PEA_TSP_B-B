package algoritms;


import models.TspEdge;
import models.TspMap;

/**
 * Created by Quantum on 2015-10-12.
 */
public class FullCheck implements Algorithm {

    private int[] bestCityOrder;
    private int currentBestCost;
    private TspMap baseMap;
    private TspMap lastResult;

    @Override
    public TspMap performAlgorithm(final TspMap map) {
        prepareForAlgorithm(map);
        int[] cityOrder = makeFirstPermutation();
        permuteCities(cityOrder, 0);
        writeBestResultToMap();
        return lastResult;
    }

    private void writeBestResultToMap() {
        final int cityCount = bestCityOrder.length;
        lastResult = new TspMap(baseMap.getEdgeCount(), baseMap.isSymmetric());
        lastResult.add(baseMap.get(bestCityOrder[cityCount- 1], bestCityOrder[0]));
        for (int i = 1; i < cityCount; i++) {
            lastResult.add(baseMap.get(bestCityOrder[i - 1], bestCityOrder[i]));
        }
    }

    private int[] makeFirstPermutation() {
        final int cityCount = baseMap.getEdgeCount();
        int[] result = new int[cityCount];
        for (int i = 0; i < cityCount; i++) {
            result[i] = i;
            bestCityOrder[i] = i;
        }
        return result;
    }

    private void prepareForAlgorithm(TspMap map) {
        baseMap = map;
        bestCityOrder = new int[map.getEdgeCount()];
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
        TspEdge edge = baseMap.get(cityOrder[cityOrder.length - 1], cityOrder[0]);
        if (edge.isExist()) {
            totalCost += edge.getWeight();
        } else { //Nie ma po³¹czenia
            return;
        }
        for (int i = 1; i < cityOrder.length; i++) {
            edge = baseMap.get(cityOrder[i - 1], cityOrder[i]);
            if (edge.isExist()) {
                totalCost += edge.getWeight();
            } else {
                return;
            }
        }
        if (totalCost < currentBestCost) {
            currentBestCost = totalCost;
            System.arraycopy(cityOrder, 0, bestCityOrder, 0, cityOrder.length);
        }
    }

    @Override
    public TspMap getResult() {
        return lastResult;
    }


}
