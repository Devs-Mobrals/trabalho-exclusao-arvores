

import java.util.ArrayList;
import java.util.List;

public class InternalNode extends Node {
    List<Node> children = new ArrayList<>();

    @Override
    boolean isLeaf() {
        return false;
    }
}
