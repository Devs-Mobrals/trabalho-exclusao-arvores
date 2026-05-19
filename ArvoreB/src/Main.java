
public class Main {
    public static void main(String[] args) {

        ArvoreB arvore = new ArvoreB(4);

        int[] valores = {
                1, 3, 7, 10, 11, 13, 14, 15,
                18, 16, 19, 24, 25, 26, 21,
                4, 5, 20, 22, 2, 17, 12, 6
        };

        System.out.println("INSERINDO:");

        for (int v : valores) {

            System.out.println("Inserindo " + v);

            arvore.inserir(v);
        }

        System.out.println("\nÁRVORE INICIAL:");

        arvore.imprimir();

        int[] remover = {6, 13, 7, 4, 2, 16};

        for (int r : remover) {

            System.out.println("\n======================");

            System.out.println("REMOVENDO " + r);

            arvore.remover(r);

            arvore.imprimir();
        }
    }
}