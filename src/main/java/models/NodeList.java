package models;

import com.sun.istack.internal.NotNull;

/**
 * Created by Quantum on 2015-10-27.
 */
public class NodeList {

    private ListNode first;

    public NodeList(@NotNull Node first) {
        this.first = new ListNode(first);
    }

    public Node popBestAndRemoveWorseThan(float upperBound) {
        ListNode best = null;
        ListNode current = first;
        while(current != null) {
            if (upperBound <= current.node.lowerBound) {
                remove(current);
            } else if (best == null || current.node.lowerBound < best.node.lowerBound) {
                best = current;
            }
            current = current.next;
        }
        if (best != null) {
            remove(best);
            return best.node;
        } else {
            return null;
        }
    }

    private void remove(ListNode node) {
        if (node.previous != null) {
            node.previous.next = node.next;
        } else {
            first = node.next;
        }
        if (node.next != null) {
            node.next.previous = node.previous;
        }
    }

    public boolean isEmpty() {
        return first == null;
    }

    public void insert(Node node) {
        ListNode newListNode = new ListNode(node);
        if (first == null) {
            first = newListNode;
        } else {
            first.previous = newListNode;
            newListNode.next = first;
            first = newListNode;
        }
    }

    public static class ListNode {

        public ListNode previous;
        public final Node node;
        public ListNode next;

        public ListNode(Node node) {
            this.node = node;
        }

    }
}
