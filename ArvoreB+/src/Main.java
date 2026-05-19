
public class Main {
    public static void main(String[] args) {
        BPlusTree tree = new BPlusTree();

        for (int i = 1; i <= 30; i++) {
            tree.insert(i);
        }

        System.out.println("ANTES DAS REMOÇÕES:");
        System.out.println(tree.rangeQuery(1, 30));

        tree.printLeafLinks();


        tree.delete(5);
        tree.delete(10);
        tree.delete(15);
        tree.delete(20);
        tree.delete(25);

        System.out.println("\nAPÓS AS REMOÇÕES:");
        System.out.println(tree.rangeQuery(1, 30));

        tree.printLeafLinks();
    }
}
