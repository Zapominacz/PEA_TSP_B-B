package algoritms;

import models.Edge;
import models.Node;
import models.NodeList;
import models.TspMap;
import tools.Utils;

public class BranchAndBound implements Algorithm {

    private NodeList nodePool;
    private Node bestSolution;

    public BranchAndBound() {

    }

    @Override
    public TspMap performAlgorithm(final TspMap mapEntry) {
        prepareAlgorithm(mapEntry);
        do {
            final float upperBound = getUpperBound();
            final Node currentNode = nodePool.popBestAndRemoveWorseThan(upperBound);
            if(currentNode.leftChild) {
                currentNode.lowerBound = 0;
                computeLowerBandFor(currentNode);
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
        computeLowerBandFor(childWithEdge);
        if(addedEdges + 2 < childWithEdge.solution.length) {
            bestSolution = childWithEdge;
        } else if(bestSolution == null || childWithEdge.lowerBound < bestSolution.lowerBound) {
            nodePool.insert(childWithEdge);
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
        if(Utils.xor(startConnected, endConnected)) {
            for (int edgeCount = 0; edgeCount < addedEdges; edgeCount++) {
                Edge edge = childWithEdge.solution[edgeCount];
                if(startConnected) {
                    childWithEdge.original[edge.startVertex][point.endVertex] = INF;
                } else {
                    childWithEdge.original[point.startVertex][edge.endVertex] = INF;
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
        boolean possibleSolution = checkPossibilityForLeftChild(point, currentNode);
        if(possibleSolution && (bestSolution == null || childWithoutEdge.lowerBound < bestSolution.lowerBound)) {
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
        Node node = new Node(entryMap.getMatrix());
        node.solution = new Edge[entryMap.getSize()];
        computeLowerBandFor(node);
        nodePool = new NodeList(node);
    }

    private void computeLowerBandFor(Node node) {
        node.reduced = Utils.cloneArray(node.original);
        node.lowerBound += rowReduction(node.reduced);
        node.lowerBound += columnReduction(node.reduced);
    }

    private Edge computeExcluded(int[][] matrix) {
        Edge result = new Edge();
        for(int row = 0; row < matrix.length; row++) {
            for(int col = 0; col < matrix.length; col++) {
                if(matrix[row][col] == 0) {
                    Edge tmp = new Edge();
                    tmp.startVertex = Utils.rowMin(matrix, row);
                    tmp.endVertex = Utils.columnMin(matrix, col);
                    tmp.weight = matrix[tmp.startVertex][tmp.endVertex];
                    if(tmp.weight > result.weight) {
                        result = tmp;
                    }
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
            result += colMin;
            for(int row = 0; row < matrix.length; row++) {
                if(matrix[row][col] < INF) {
                    matrix[row][col] -= colMin;
                }
            }
        }
        return result;
    }

    private int rowReduction(final int[][] matrix) {
        int result = 0;
        for(int row = 0; row < matrix.length; row++) {
            int rowMin = Utils.rowMin(matrix, row);
            result += rowMin;
            for(int col = 0; col < matrix.length; col++) {
                if(matrix[row][col] < INF) {
                    matrix[row][col] -= rowMin;
                }
            }
        }
        return result;
    }
}
