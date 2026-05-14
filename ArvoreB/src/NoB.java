import java.util.ArrayList;

public class NoB {
    ArrayList<Integer> chaves;
    ArrayList<NoB> filhos;
    boolean folha;

    public NoB(boolean folha) {

        this.folha = folha;

        chaves = new ArrayList<>();
        filhos = new ArrayList<>();
    }
}
