package algoritms;

import models.Edge;
import models.Node;
import models.NodeList;
import tools.Utils;

public class BranchAndBound {

    public static final int INF = Integer.MAX_VALUE;

    private NodeList nodePool;
    private Node bestSolution;
    private int[][] baseMatrix;

    public BranchAndBound() {

    }

    public int[] performAlgorithm(final int[][] mapEntry) {
        prepareAlgorithm(mapEntry);
        do {
            final float upperBound = getUpperBound();
            final Node currentNode = nodePool.popBestAndRemoveWorseThan(upperBound);
            if (currentNode == null) {
                break;
            }
            branch(currentNode);
        } while (!nodePool.isEmpty());
        return prepareSolution();
    }

    private int[] prepareSolution() {
        int size = bestSolution.solution.length;
        int[] result = new int[size];
        Edge edge = bestSolution.solution[0];
        result[0] = edge.startVertex;
        result[1] = edge.endVertex;
        for (int i = 2; i < size; i++) {
            int last = result[i - 1];
            for (int j = 1; j < size; j++) {
                Edge tmp = bestSolution.solution[j];
                if (tmp.startVertex == last) {
                    result[i] = tmp.endVertex;
                }
            }
        }
        return result;
    }

    private float getUpperBound() {
        float result = INF;
        if(bestSolution != null) {
            result = bestSolution.lowerBound;
        }
        return result;
    }

    private void branch(final Node currentNode) {
        final Edge point = computeExcluded(currentNode.matrix);
        if (point.weight < INF) { //można dalej branchować
            currentNode.matrix[point.startVertex][point.endVertex] = INF;
            computeAndAddChildWithoutEdge(currentNode, point);
            computeAndAddChildWithEdge(currentNode, point);
        }
    }

    private void computeAndAddChildWithEdge(final Node childWithEdge, Edge point) {
        markAsTaken(point, childWithEdge);
        removeCyclesAndAddEdge(childWithEdge, point);
        computeLowerBoundFor(childWithEdge);
        if (childWithEdge.added + 2 == childWithEdge.solution.length) {
            obtainSolution(childWithEdge);
        } else if (bestSolution == null || childWithEdge.lowerBound < getUpperBound()) {
            nodePool.insert(childWithEdge);
        }
    }

    private void obtainSolution(Node node) {
        int edgeCount = node.solution.length - 2;
        for (int row = 0; row < node.matrix.length; row++) {
            for (int col = 0; col < node.matrix.length; col++) {
                if (node.matrix[row][col] < INF) {
                    Edge tmp = new Edge();
                    tmp.startVertex = row;
                    tmp.endVertex = col;
                    node.solution[edgeCount] = tmp;
                    edgeCount++;
                }
            }
        }
        int upperBound = 0;
        for (Edge edge : node.solution) {
            upperBound += baseMatrix[edge.startVertex][edge.endVertex];
        }
        if (bestSolution == null || bestSolution.lowerBound > upperBound) {
            bestSolution = node;
        }
    }

    private void markAsTaken(Edge point, Node childWithEdge) {
        final int[][] reduced = childWithEdge.matrix;
        for(int rowCol = 0; rowCol < reduced.length; rowCol++) {
            reduced[rowCol][point.endVertex] = INF;
            reduced[point.startVertex][rowCol] = INF;
        }
        reduced[point.endVertex][point.startVertex] = INF;
    }

    private void removeCyclesAndAddEdge(Node childWithEdge, Edge point) {
        childWithEdge.solution[childWithEdge.added] = point;
        childWithEdge.added++;
        int setNumber = childWithEdge.union(point.startVertex, point.endVertex);
        //Zawsze zmieniamy numer unii na unię z kórego wychodzi krawedź
        childWithEdge.matrix[childWithEdge.endEdges[setNumber]][setNumber] = INF;
    }

    /**
     * Dodaje do puli rozwiązanie bez danej krawędzi
     */
    public void computeAndAddChildWithoutEdge(final Node currentNode, Edge point) {
        Node childWithoutEdge = new Node(Utils.cloneArray(currentNode.matrix));
        childWithoutEdge.lowerBound = point.weight + currentNode.lowerBound;
        childWithoutEdge.solution = Utils.cloneEdges(currentNode.solution);
        childWithoutEdge.nodeUnion = Utils.cloneArray(currentNode.nodeUnion);
        childWithoutEdge.endEdges = Utils.cloneArray(currentNode.endEdges);
        if (bestSolution == null || childWithoutEdge.lowerBound < getUpperBound()) {
            nodePool.insert(childWithoutEdge);
        }
    }

    /**
     * Przygotowanie algorytmu: inicjalizacje itd.
     *
     * @param matrix
     */
    private void prepareAlgorithm(final int[][] matrix) {
        baseMatrix = Utils.cloneArray(matrix);
        Node node = new Node(Utils.cloneArray(matrix));
        node.solution = new Edge[matrix.length];
        node.nodeUnion = new int[matrix.length];
        node.endEdges = new int[matrix.length];
        for (int i = 0; i < node.nodeUnion.length; i++) {
            node.nodeUnion[i] = i;
            node.endEdges[i] = i;
        }
        computeLowerBoundFor(node);
        nodePool = new NodeList(node);
    }

    private void computeLowerBoundFor(Node node) { //redukcja, której wynik dodaje się do aktualnej wartości ograniczenia
        node.lowerBound += rowReduction(node.matrix); //dolnego
        node.lowerBound += columnReduction(node.matrix);
    }

    private Edge computeExcluded(int[][] matrix) {
        Edge result = new Edge();
        result.weight = -1;
        for(int row = 0; row < matrix.length; row++) {
            for(int col = 0; col < matrix.length; col++) {
                if(matrix[row][col] == 0) {
                    matrix[row][col] = INF; //zabezpieczenie przed braniem samego siebie
                    int rowCost = Utils.rowMin(matrix, row);
                    int colCost = Utils.columnMin(matrix, col); //pobieranie minimum z kolumny i wiersza
                    int edgeCost = rowCost + colCost;
                    if (rowCost < INF && colCost < INF && edgeCost > result.weight) { //szukanie największego kosztu
                        result.startVertex = row;
                        result.endVertex = col;
                        result.weight = edgeCost;
                    }
                    matrix[row][col] = 0; //odbezpieczanie
                }
            }
        }
        if (result.weight < 0) {
            result.weight = INF;
        }
        return result;
    }

    private int columnReduction(final int[][] matrix) { //algorytm dla kolumn
        int result = 0;
        for(int col = 0; col < matrix.length; col++) {
            int colMin = Utils.columnMin(matrix, col); //szukanie najmniejszej
            if (colMin < INF) { //gdy inf, nie ma co redukować (INF = brak)
                result += colMin;
                for (int row = 0; row < matrix.length; row++) {
                    if (matrix[row][col] < INF) { //redukcja w kolumnie
                        matrix[row][col] -= colMin;
                    }
                }
            }
        }
        return result;
    }

    private int rowReduction(final int[][] matrix) { //patrz: columnReduction
        int result = 0;
        for(int row = 0; row < matrix.length; row++) {
            int rowMin = Utils.rowMin(matrix, row);
            if (rowMin < INF) {
                result += rowMin;
                for (int col = 0; col < matrix.length; col++) {
                    if (matrix[row][col] < INF) {
                        matrix[row][col] -= rowMin;
                    }
                }
            }
        }
        return result;
    }
}
