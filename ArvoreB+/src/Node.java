import java.util.ArrayList;
import java.util.List;

abstract class Node {
    List<Integer> keys = new ArrayList<>();
    InternalNode parent;

    abstract boolean isLeaf();
}
