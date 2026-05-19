

import java.util.ArrayList;
import java.util.List;

public class BPlusTree {

    private final int ORDER = 4;
    private Node root;

    public BPlusTree() {
        root = new LeafNode();
    }


    public void insert(int key) {

        LeafNode leaf = findLeaf(key);

        insertIntoLeaf(leaf, key);

        if (leaf.keys.size() >= ORDER) {
            splitLeaf(leaf);
        }
    }

    private void insertIntoLeaf(LeafNode leaf, int key) {

        int i = 0;

        while (i < leaf.keys.size() && leaf.keys.get(i) < key) {
            i++;
        }

        leaf.keys.add(i, key);
        leaf.values.add(i, key);
    }

    private void splitLeaf(LeafNode leaf) {

        int mid = ORDER / 2;

        LeafNode newLeaf = new LeafNode();

        newLeaf.keys.addAll(leaf.keys.subList(mid, leaf.keys.size()));
        newLeaf.values.addAll(leaf.values.subList(mid, leaf.values.size()));

        leaf.keys.subList(mid, leaf.keys.size()).clear();
        leaf.values.subList(mid, leaf.values.size()).clear();

        newLeaf.next = leaf.next;
        leaf.next = newLeaf;

        int promotedKey = newLeaf.keys.get(0);

        insertIntoParent(leaf, promotedKey, newLeaf);
    }

    private void insertIntoParent(Node left, int key, Node right) {

        if (left == root) {

            InternalNode newRoot = new InternalNode();

            newRoot.keys.add(key);

            newRoot.children.add(left);
            newRoot.children.add(right);

            left.parent = newRoot;
            right.parent = newRoot;

            root = newRoot;

            return;
        }

        InternalNode parent = left.parent;

        int index = parent.children.indexOf(left) + 1;

        parent.keys.add(index - 1, key);
        parent.children.add(index, right);

        right.parent = parent;

        if (parent.children.size() > ORDER) {
            splitInternal(parent);
        }
    }

    private void splitInternal(InternalNode node) {

        int midIndex = node.keys.size() / 2;

        int promotedKey = node.keys.get(midIndex);

        InternalNode newInternal = new InternalNode();

        newInternal.keys.addAll(
                node.keys.subList(midIndex + 1, node.keys.size())
        );

        newInternal.children.addAll(
                node.children.subList(midIndex + 1, node.children.size())
        );

        for (Node child : newInternal.children) {
            child.parent = newInternal;
        }

        node.keys.subList(midIndex, node.keys.size()).clear();

        node.children.subList(
                midIndex + 1,
                node.children.size()
        ).clear();

        insertIntoParent(node, promotedKey, newInternal);
    }


    private LeafNode findLeaf(int key) {

        Node current = root;

        while (!current.isLeaf()) {

            InternalNode internal = (InternalNode) current;

            int i = 0;

            while (i < internal.keys.size()
                    && key >= internal.keys.get(i)) {
                i++;
            }

            current = internal.children.get(i);
        }

        return (LeafNode) current;
    }



    public List<Integer> rangeQuery(int start, int end) {

        List<Integer> result = new ArrayList<>();

        LeafNode current = findLeaf(start);

        while (current != null) {

            for (int key : current.keys) {

                if (key >= start && key <= end) {
                    result.add(key);
                }

                if (key > end) {
                    return result;
                }
            }

            current = current.next;
        }

        return result;
    }


    public void delete(int key) {

        LeafNode leaf = findLeaf(key);

        int index = leaf.keys.indexOf(key);

        if (index == -1) {
            return;
        }

        leaf.keys.remove(index);
        leaf.values.remove(index);

        updateParentKeys(leaf);

        if (leaf == root) {
            return;
        }

        int minKeys = (ORDER - 1) / 2;

        if (leaf.keys.size() >= minKeys) {
            return;
        }

        rebalanceLeaf(leaf);
    }

    private void rebalanceLeaf(LeafNode leaf) {

        InternalNode parent = leaf.parent;

        int index = parent.children.indexOf(leaf);

        LeafNode leftSibling = null;
        LeafNode rightSibling = null;

        if (index > 0) {
            leftSibling = (LeafNode) parent.children.get(index - 1);
        }

        if (index < parent.children.size() - 1) {
            rightSibling = (LeafNode) parent.children.get(index + 1);
        }

        if (leftSibling != null && leftSibling.keys.size() > 1) {

            int borrowedKey =
                    leftSibling.keys.remove(leftSibling.keys.size() - 1);

            int borrowedValue =
                    leftSibling.values.remove(leftSibling.values.size() - 1);

            leaf.keys.add(0, borrowedKey);
            leaf.values.add(0, borrowedValue);

            parent.keys.set(index - 1, leaf.keys.get(0));

            return;
        }


        if (rightSibling != null && rightSibling.keys.size() > 1) {

            int borrowedKey = rightSibling.keys.remove(0);

            int borrowedValue = rightSibling.values.remove(0);

            leaf.keys.add(borrowedKey);
            leaf.values.add(borrowedValue);

            parent.keys.set(index, rightSibling.keys.get(0));

            return;
        }

        if (leftSibling != null) {

            leftSibling.keys.addAll(leaf.keys);
            leftSibling.values.addAll(leaf.values);


            leftSibling.next = leaf.next;

            parent.children.remove(index);
            parent.keys.remove(index - 1);

        } else if (rightSibling != null) {

            leaf.keys.addAll(rightSibling.keys);
            leaf.values.addAll(rightSibling.values);


            leaf.next = rightSibling.next;

            parent.children.remove(index + 1);
            parent.keys.remove(index);
        }

        rebalanceInternal(parent);
    }

    private void rebalanceInternal(InternalNode node) {

        if (node == root) {

            if (node.children.size() == 1) {

                root = node.children.get(0);
                root.parent = null;
            }

            return;
        }

        int minChildren = (ORDER + 1) / 2;

        if (node.children.size() >= minChildren) {
            return;
        }

        InternalNode parent = node.parent;

        int index = parent.children.indexOf(node);

        InternalNode leftSibling = null;
        InternalNode rightSibling = null;

        if (index > 0) {
            leftSibling = (InternalNode) parent.children.get(index - 1);
        }

        if (index < parent.children.size() - 1) {
            rightSibling = (InternalNode) parent.children.get(index + 1);
        }

        if (leftSibling != null) {

            leftSibling.keys.add(parent.keys.get(index - 1));
            leftSibling.keys.addAll(node.keys);

            leftSibling.children.addAll(node.children);

            for (Node child : node.children) {
                child.parent = leftSibling;
            }

            parent.children.remove(index);
            parent.keys.remove(index - 1);

        } else if (rightSibling != null) {

            node.keys.add(parent.keys.get(index));
            node.keys.addAll(rightSibling.keys);

            node.children.addAll(rightSibling.children);

            for (Node child : rightSibling.children) {
                child.parent = node;
            }

            parent.children.remove(index + 1);
            parent.keys.remove(index);
        }

        rebalanceInternal(parent);
    }


    private void updateParentKeys(LeafNode leaf) {

        InternalNode parent = leaf.parent;

        while (parent != null) {

            for (int i = 0; i < parent.children.size() - 1; i++) {

                LeafNode rightLeaf =
                        getFirstLeaf(parent.children.get(i + 1));

                if (!rightLeaf.keys.isEmpty()) {
                    parent.keys.set(i, rightLeaf.keys.get(0));
                }
            }

            parent = parent.parent;
        }
    }

    private LeafNode getFirstLeaf(Node node) {

        Node current = node;

        while (!current.isLeaf()) {
            current = ((InternalNode) current).children.get(0);
        }

        return (LeafNode) current;
    }



    public void printLeafLinks() {

        Node current = root;

        while (!current.isLeaf()) {
            current = ((InternalNode) current).children.get(0);
        }

        LeafNode leaf = (LeafNode) current;

        System.out.println("\n=== ENCADEAMENTO DAS FOLHAS ===");

        while (leaf != null) {

            System.out.print("Folha " + leaf.keys + " -> ");

            if (leaf.next != null) {
                System.out.println(leaf.next.keys);
            } else {
                System.out.println("null");
            }

            leaf = leaf.next;
        }
    }
}
