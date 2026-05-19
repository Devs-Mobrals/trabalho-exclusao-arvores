
public class Main {
    public static void main(String[] args) {
        ArvoreRubroNegra arvore = new ArvoreRubroNegra();

        int[] valores = {
                7, 3, 18, 10,
                22, 8, 11, 26
        };

        System.out.println("===== INSERÇÃO =====");

        for (int valor : valores) {

            System.out.println("Inserindo: " + valor);

            arvore.inserir(valor);
        }

        System.out.println("\nÁRVORE INICIAL:");

        arvore.imprimir();

        int[] remover = {18, 11, 3};

        for (int valor : remover) {

            System.out.println("\n====================");

            System.out.println("Removendo: " + valor);

            arvore.remover(valor);

            arvore.imprimir();
        }
    }
}