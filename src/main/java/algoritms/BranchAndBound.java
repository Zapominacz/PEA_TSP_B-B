package algoritms;

import models.Edge;
import models.Node;
import models.NodeList;
import models.TspMap;
import tools.Utils;

public class BranchAndBound implements Algorithm {

    private NodeList nodePool;
    private Node bestSolution;
    private int[][] baseMatrix;

    public BranchAndBound() {

    }

    @Override
    public TspMap performAlgorithm(final TspMap mapEntry) {
        prepareAlgorithm(mapEntry);
        do {
            final float upperBound = getUpperBound();
            final Node currentNode = nodePool.popBestAndRemoveWorseThan(upperBound);
            if (currentNode == null) {
                continue;
            }
            if(currentNode.leftChild) {
                currentNode.lowerBound = 0;
                computeLowerBoundFor(currentNode);
            }
            branch(currentNode);
        } while (!nodePool.isEmpty());
        return getResult();
    }

    private float getUpperBound() {
        float result = INF;
        if(bestSolution != null) {
            result = bestSolution.lowerBound;
        }
        return result;
    }

    private void branch(final Node currentNode) {
        final Edge point = computeExcluded(currentNode.reduced);
        int edgeCost = getEdgeCost(point);
        point.weight = getPointWeightAndMarkAsUsed(point, currentNode);
        computeAndAddChildWithoutEdge(currentNode, point, edgeCost);
        computeAndAddChildWithEdge(currentNode, point);
    }

    private void computeAndAddChildWithEdge(final Node childWithEdge, Edge point) {
        childWithEdge.original = childWithEdge.reduced;
        childWithEdge.reduced = null;
        markAsTaken(point, childWithEdge);
        int addedEdges = removeCyclesAndAddEdge(childWithEdge, point);
        computeLowerBoundFor(childWithEdge);
        if (addedEdges + 2 == childWithEdge.solution.length) {
            obtainSolution(childWithEdge);
        } else if (bestSolution == null || childWithEdge.lowerBound < getUpperBound()) {
            nodePool.insert(childWithEdge);
        }
    }

    private void obtainSolution(Node node) {
        int edgeCount = node.solution.length - 2;
        for (int row = 0; row < node.original.length; row++) {
            for (int col = 0; col < node.original.length; col++) {
                if (node.original[row][col] < INF) {
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
        int[][] reduced = childWithEdge.original;
        for(int rowCol = 0; rowCol < reduced.length; rowCol++) {
            reduced[rowCol][point.endVertex] = INF;
            reduced[point.startVertex][rowCol] = INF;
        }
        reduced[point.endVertex][point.startVertex] = INF;
    }

    private int removeCyclesAndAddEdge(Node childWithEdge, Edge point) {
        int addedEdges = 0;
        boolean startConnected = false;
        boolean endConnected = false;
        for(int edgeCount = 0; edgeCount < childWithEdge.solution.length; edgeCount++) {
            Edge edge = childWithEdge.solution[edgeCount];
            if(edge == null) {
                childWithEdge.solution[edgeCount] = point;
                childWithEdge.union(point.startVertex, point.endVertex);
                break;
            } else {
                if(edge.startVertex == point.endVertex) {
                    endConnected = true;
                } else if (point.startVertex == edge.endVertex) {
                    startConnected = true;
                }
                addedEdges++;
            }
        }
        int setNumber = childWithEdge.nodeUnion[point.startVertex];
        if(Utils.xor(startConnected, endConnected)) {
            for (int edgeCount = 0; edgeCount < addedEdges; edgeCount++) {
                Edge edge = childWithEdge.solution[edgeCount];
                if (startConnected && childWithEdge.nodeUnion[edge.endVertex] == setNumber) {
                    childWithEdge.original[point.endVertex][edge.startVertex] = INF;
                } else if (childWithEdge.nodeUnion[edge.startVertex] == setNumber) {
                    childWithEdge.original[edge.endVertex][point.startVertex] = INF;
                }
            }
        }
        return ++addedEdges;
    }

    public void computeAndAddChildWithoutEdge(final Node currentNode, Edge point, int edgeCost) {
        Node childWithoutEdge = new Node(currentNode.original);
        childWithoutEdge.leftChild = true;
        childWithoutEdge.lowerBound = edgeCost + currentNode.lowerBound;
        childWithoutEdge.solution = new Edge[currentNode.solution.length];
        System.arraycopy(currentNode.solution, 0, childWithoutEdge.solution, 0, currentNode.solution.length);
        childWithoutEdge.nodeUnion = new int[currentNode.solution.length];
        System.arraycopy(currentNode.nodeUnion, 0, childWithoutEdge.nodeUnion, 0, currentNode.nodeUnion.length);
        boolean possibleSolution = checkPossibilityForLeftChild(point, currentNode);
        if (possibleSolution && (bestSolution == null || childWithoutEdge.lowerBound < getUpperBound())) {
            nodePool.insert(childWithoutEdge);
        }
    }

    private boolean checkPossibilityForLeftChild(Edge point, Node node) {
        int otherWaysIn = 0;
        int otherWaysOut = 0;
        for(int rowCol = 0; rowCol < node.solution.length; rowCol++) {
            if(node.original[point.startVertex][rowCol] < INF) {
                otherWaysOut++;
            }
            if(node.original[rowCol][point.endVertex] < INF) {
                otherWaysIn++;
            }
        }
        return otherWaysIn > 0 && otherWaysOut > 0;
    }

    private int getPointWeightAndMarkAsUsed(Edge point, Node node) {
        int weight = node.original[point.startVertex][point.endVertex];
        node.original[point.startVertex][point.endVertex] = INF;
        return weight;
    }

    private int getEdgeCost(Edge point) {
        return point.weight;
    }

    private void prepareAlgorithm(final TspMap entryMap) {
        final int[][] matrix2 = {
                {INF, 3, 4, 2, 7},
                {3, INF, 4, 6, 3},
                {4, 4, INF, 5, 5},
                {2, 6, 5, INF, 6},
                {7, 3, 8, 6, INF}
        };
        final int[][] matrix = {
                {INF, 27, 43, 16, 30, 26},
                {7, INF, 16, 1, 30, 25},
                {20, 13, INF, 35, 5, 0},
                {21, 16, 25, INF, 18, 18},
                {12, 46, 27, 48, INF, 5},
                {23, 5, 5, 9, 5, INF}
        };
        baseMatrix = Utils.cloneArray(matrix);
        Node node = new Node(matrix);
        node.solution = new Edge[matrix.length];
        node.nodeUnion = new int[matrix.length];
        for (int i = 0; i < node.nodeUnion.length; i++) {
            node.nodeUnion[i] = i;
        }
        computeLowerBoundFor(node);
        nodePool = new NodeList(node);
    }

    private void computeLowerBoundFor(Node node) {
        node.reduced = Utils.cloneArray(node.original);
        node.lowerBound += rowReduction(node.reduced);
        node.lowerBound += columnReduction(node.reduced);
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

    @Override
    public final TspMap getResult() {
        return null;
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
