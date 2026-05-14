
import java.util.ArrayList;
import java.util.List;

public class LeafNode extends Node {

    List<Integer> values = new ArrayList<>();
    LeafNode next;

    @Override
    boolean isLeaf() {
        return true;
    }
}
