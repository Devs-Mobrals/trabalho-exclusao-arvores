public class ArvoreRubroNegra {
    private No raiz;

    private final No NIL;

    public ArvoreRubroNegra() {
        NIL = new No(0);
        NIL.cor = No.PRETO;
        NIL.esquerda = NIL;
        NIL.direita = NIL;
        NIL.pai = NIL;
        raiz = NIL;
    }

    public void inserir(int valor) {

        No novo = new No(valor);
        novo.esquerda = NIL;
        novo.direita = NIL;
        No pai = NIL;
        No atual = raiz;

        while (atual != NIL) {
            pai = atual;

            if (novo.valor < atual.valor) {
                atual = atual.esquerda;
            }
            else {
                atual = atual.direita;
            }
        }

        novo.pai = pai;

        if (pai == NIL) {
            raiz = novo;
        }

        else if (novo.valor < pai.valor) {
            pai.esquerda = novo;
        }

        else {
            pai.direita = novo;
        }

        corrigirInsercao(novo);
    }


    private void corrigirInsercao(No no) {

        while (no.pai.cor == No.VERMELHO) {

            if (no.pai == no.pai.pai.esquerda) {

                No tio = no.pai.pai.direita;

                if (tio.cor == No.VERMELHO) {

                    no.pai.cor = No.PRETO;

                    tio.cor = No.PRETO;

                    no.pai.pai.cor = No.VERMELHO;

                    no = no.pai.pai;
                }

                else {

                    if (no == no.pai.direita) {

                        no = no.pai;

                        rotacaoEsquerda(no);
                    }


                    no.pai.cor = No.PRETO;

                    no.pai.pai.cor = No.VERMELHO;

                    rotacaoDireita(no.pai.pai);
                }
            }


            else {

                No tio = no.pai.pai.esquerda;

                if (tio.cor == No.VERMELHO) {

                    no.pai.cor = No.PRETO;

                    tio.cor = No.PRETO;

                    no.pai.pai.cor = No.VERMELHO;

                    no = no.pai.pai;
                }

                else {

                    if (no == no.pai.esquerda) {

                        no = no.pai;

                        rotacaoDireita(no);
                    }

                    no.pai.cor = No.PRETO;

                    no.pai.pai.cor = No.VERMELHO;

                    rotacaoEsquerda(no.pai.pai);
                }
            }
        }

        raiz.cor = No.PRETO;
    }

    private void rotacaoEsquerda(No x) {

        No y = x.direita;

        x.direita = y.esquerda;

        if (y.esquerda != NIL) {
            y.esquerda.pai = x;
        }

        y.pai = x.pai;

        if (x.pai == NIL) {
            raiz = y;
        }

        else if (x == x.pai.esquerda) {
            x.pai.esquerda = y;
        }

        else {
            x.pai.direita = y;
        }
        y.esquerda = x;
        x.pai = y;
    }

    private void rotacaoDireita(No y) {

        No x = y.esquerda;

        y.esquerda = x.direita;

        if (x.direita != NIL) {
            x.direita.pai = y;
        }

        x.pai = y.pai;

        if (y.pai == NIL) {
            raiz = x;
        }

        else if (y == y.pai.direita) {
            y.pai.direita = x;
        }

        else {
            y.pai.esquerda = x;
        }
        x.direita = y;
        y.pai = x;
    }

    private No buscar(No no, int valor) {

        while (no != NIL && valor != no.valor) {
            if (valor < no.valor) {
                no = no.esquerda;
            }
            else {
                no = no.direita;
            }
        }
        return no;
    }

    private No minimo(No no) {

        while (no.esquerda != NIL) {

            no = no.esquerda;
        }
        return no;
    }

    private void transplant(No u, No v) {

        if (u.pai == NIL) {
            raiz = v;
        }

        else if (u == u.pai.esquerda) {
            u.pai.esquerda = v;
        }

        else {
            u.pai.direita = v;
        }

        v.pai = u.pai;
    }


    public void remover(int valor) {

        No z = buscar(raiz, valor);

        if (z == NIL) {

            System.out.println("Valor não encontrado");
            return;
        }

        No y = z;

        boolean corOriginal = y.cor;

        No x;

        if (z.esquerda == NIL) {

            x = z.direita;

            transplant(z, z.direita);
        }


        else if (z.direita == NIL) {

            x = z.esquerda;

            transplant(z, z.esquerda);
        }


        else {

            y = minimo(z.direita);

            corOriginal = y.cor;

            x = y.direita;

            if (y.pai == z) {

                x.pai = y;
            }

            else {

                transplant(y, y.direita);

                y.direita = z.direita;

                y.direita.pai = y;
            }

            transplant(z, y);

            y.esquerda = z.esquerda;

            y.esquerda.pai = y;

            y.cor = z.cor;
        }

        if (corOriginal == No.PRETO) {

            corrigirRemocao(x);
        }
    }


    private void corrigirRemocao(No x) {

        while (x != raiz && x.cor == No.PRETO) {

            if (x == x.pai.esquerda) {

                No irmao = x.pai.direita;


                if (irmao.cor == No.VERMELHO) {

                    System.out.println("Caso 1");
                    irmao.cor = No.PRETO;
                    x.pai.cor = No.VERMELHO;
                    rotacaoEsquerda(x.pai);
                    irmao = x.pai.direita;
                }


                if (irmao.esquerda.cor == No.PRETO &&
                        irmao.direita.cor == No.PRETO) {

                    System.out.println("Caso 2");
                    irmao.cor = No.VERMELHO;
                    x = x.pai;
                }

                else {


                    if (irmao.direita.cor == No.PRETO) {

                        System.out.println("Caso 3");
                        irmao.esquerda.cor = No.PRETO;
                        irmao.cor = No.VERMELHO;
                        rotacaoDireita(irmao);
                        irmao = x.pai.direita;
                    }


                    System.out.println("Caso 4");
                    irmao.cor = x.pai.cor;
                    x.pai.cor = No.PRETO;
                    irmao.direita.cor = No.PRETO;
                    rotacaoEsquerda(x.pai);
                    x = raiz;
                }
            }

            else {

                No irmao = x.pai.esquerda;

                if (irmao.cor == No.VERMELHO) {

                    System.out.println("Caso 1");
                    irmao.cor = No.PRETO;
                    x.pai.cor = No.VERMELHO;
                    rotacaoDireita(x.pai);
                    irmao = x.pai.esquerda;
                }

                if (irmao.direita.cor == No.PRETO &&
                        irmao.esquerda.cor == No.PRETO) {

                    System.out.println("Caso 2");
                    irmao.cor = No.VERMELHO;
                    x = x.pai;
                }

                else {

                    if (irmao.esquerda.cor == No.PRETO) {

                        System.out.println("Caso 3");
                        irmao.direita.cor = No.PRETO;
                        irmao.cor = No.VERMELHO;
                        rotacaoEsquerda(irmao);
                        irmao = x.pai.esquerda;
                    }

                    System.out.println("Caso 4");
                    irmao.cor = x.pai.cor;
                    x.pai.cor = No.PRETO;
                    irmao.esquerda.cor = No.PRETO;
                    rotacaoDireita(x.pai);
                    x = raiz;
                }
            }
        }

        x.cor = No.PRETO;
    }

    public void imprimir() {

        imprimirArvore(raiz, "", true);
    }

    private void imprimirArvore(No no, String espaco, boolean ultimo) {

        if (no != NIL) {

            System.out.print(espaco);

            if (ultimo) {

                System.out.print("R----");

                espaco += "     ";
            }

            else {

                System.out.print("L----");

                espaco += "|    ";
            }

            String cor;

            if (no.cor == No.VERMELHO) {

                cor = "VERMELHO";
            }

            else {
                cor = "PRETO";
            }

            System.out.println(no.valor + "(" + cor + ")");

            imprimirArvore(no.esquerda, espaco, false);

            imprimirArvore(no.direita, espaco, true);
        }
    }
}
