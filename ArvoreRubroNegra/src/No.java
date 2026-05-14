public class No {

    int valor;
    boolean cor;

    No esquerda;
    No direita;
    No pai;

    public static final boolean VERMELHO = true;
    public static final boolean PRETO = false;

    public No(int valor) {

        this.valor = valor;

        cor = VERMELHO;
    }
}
