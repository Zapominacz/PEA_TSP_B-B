package models;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

/**
 * Created by Quantum on 2015-10-27.
 */
public class NodeList {

    private ListNode first;

    public NodeList(@NotNull Node first) {
        this.first = new ListNode(first);
    }

    public Node popBestAndRemoveWorseThan(float upperBound) {
        ListNode best = first;
        ListNode current = first;
        while(current != null) {
            ListNode next = current.next;
            if(next.node.lowerBound < best.node.lowerBound) {
                best = next;
            } else if(upperBound <= next.node.lowerBound) {
                remove(next);
                next = current.next;
            }
            current = next;
        }
        remove(best);
        return best.node;
    }

    private void remove(ListNode node) {
        node.previous.next = node.next;
        node.next.previous = node.previous;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public void insert(Node node) {
        ListNode newListNode = new ListNode(node);
        newListNode.next = first;
        first = newListNode;
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
