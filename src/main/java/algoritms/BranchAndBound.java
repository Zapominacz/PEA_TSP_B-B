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
                continue;
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
        currentNode.matrix[point.startVertex][point.endVertex] = INF;
        computeAndAddChildWithoutEdge(currentNode, point);
        computeAndAddChildWithEdge(currentNode, point);
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
        int[][] reduced = childWithEdge.matrix;
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
        int startV = point.startVertex;
        int endV = point.endVertex;
        for (int edgeCount = 0; edgeCount < childWithEdge.added; edgeCount++) {
            for (int edge2 = 0; edge2 < childWithEdge.added; edge2++) {
                Edge edge = childWithEdge.solution[edgeCount];
                if (childWithEdge.nodeUnion[edge.startVertex] == setNumber) {
                    if (edge.endVertex == startV) {
                        startV = edge.startVertex;
                    }
                    if (edge.startVertex == endV) {
                        endV = edge.endVertex;
                    }
                }
            }
        }
        childWithEdge.matrix[endV][startV] = INF;
    }

    public void computeAndAddChildWithoutEdge(final Node currentNode, Edge point) {
        Node childWithoutEdge = new Node(Utils.cloneArray(currentNode.matrix));
        childWithoutEdge.lowerBound = point.weight + currentNode.lowerBound;
        childWithoutEdge.solution = new Edge[currentNode.solution.length];
        System.arraycopy(currentNode.solution, 0, childWithoutEdge.solution, 0, currentNode.solution.length);
        childWithoutEdge.nodeUnion = new int[currentNode.solution.length];
        System.arraycopy(currentNode.nodeUnion, 0, childWithoutEdge.nodeUnion, 0, currentNode.nodeUnion.length);
        computeLowerBoundFor(childWithoutEdge);
        boolean possibleSolution = checkPossibilityForLeftChild(point, currentNode);
        if (possibleSolution && (bestSolution == null || childWithoutEdge.lowerBound < getUpperBound())) {
            nodePool.insert(childWithoutEdge);
        }
    }

    private boolean checkPossibilityForLeftChild(Edge point, Node node) {
        int otherWaysIn = 0;
        int otherWaysOut = 0;
        for(int rowCol = 0; rowCol < node.solution.length; rowCol++) {
            if (node.matrix[point.startVertex][rowCol] < INF) {
                otherWaysOut++;
            }
            if (node.matrix[rowCol][point.endVertex] < INF) {
                otherWaysIn++;
            }
        }
        return otherWaysIn > 0 && otherWaysOut > 0;
    }


    private int getEdgeCost(Edge point) {
        return point.weight;
    }

    private void prepareAlgorithm(final int[][] matrix) {
        baseMatrix = Utils.cloneArray(matrix);
        Node node = new Node(Utils.cloneArray(matrix));
        node.solution = new Edge[matrix.length];
        node.nodeUnion = new int[matrix.length];
        for (int i = 0; i < node.nodeUnion.length; i++) {
            node.nodeUnion[i] = i;
        }
        computeLowerBoundFor(node);
        nodePool = new NodeList(node);
    }

    private void computeLowerBoundFor(Node node) {
        node.lowerBound += rowReduction(node.matrix);
        node.lowerBound += columnReduction(node.matrix);
    }

    private Edge computeExcluded(int[][] matrix) {
        Edge result = new Edge();
        for(int row = 0; row < matrix.length; row++) {
            for(int col = 0; col < matrix.length; col++) {
                if(matrix[row][col] == 0) {
                    matrix[row][col] = INF;
                    int edgeCost = Utils.rowMin(matrix, row);
                    edgeCost += Utils.columnMin(matrix, col);
                    if (edgeCost > result.weight) {
                        result.startVertex = row;
                        result.endVertex = col;
                        result.weight = edgeCost;
                    }
                    matrix[row][col] = 0;
                }
            }
        }
        return result;
    }

    private int columnReduction(final int[][] matrix) {
        int result = 0;
        for(int col = 0; col < matrix.length; col++) {
            int colMin = Utils.columnMin(matrix, col);
            if (colMin != INF) {
                result += colMin;
                for (int row = 0; row < matrix.length; row++) {
                    if (matrix[row][col] < INF) {
                        matrix[row][col] -= colMin;
                    }
                }
            }
        }
        return result;
    }

    private int rowReduction(final int[][] matrix) {
        int result = 0;
        for(int row = 0; row < matrix.length; row++) {
            int rowMin = Utils.rowMin(matrix, row);
            if (rowMin != INF) {
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
