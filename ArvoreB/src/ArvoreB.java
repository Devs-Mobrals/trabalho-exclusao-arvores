public class ArvoreB {
    private NoB raiz;

    private int ordem;
    private int maxChaves;
    private int minChaves;

    public ArvoreB(int ordem) {

        this.ordem = ordem;

        maxChaves = ordem - 1;

        minChaves = (int) Math.ceil(ordem / 2.0) - 1;

        raiz = new NoB(true);
    }


    public void inserir(int valor) {

        NoB r = raiz;

        if (r.chaves.size() == maxChaves) {

            NoB novaRaiz = new NoB(false);

            novaRaiz.filhos.add(r);

            split(novaRaiz, 0, r);

            raiz = novaRaiz;
        }

        inserirNaoCheio(raiz, valor);
    }

    private void inserirNaoCheio(NoB no, int valor) {

        int i = no.chaves.size() - 1;

        if (no.folha) {

            no.chaves.add(0);

            while (i >= 0 && valor < no.chaves.get(i)) {

                no.chaves.set(i + 1, no.chaves.get(i));

                i--;
            }

            no.chaves.set(i + 1, valor);
        }
        else {

            while (i >= 0 && valor < no.chaves.get(i)) {
                i--;
            }

            i++;

            if (no.filhos.get(i).chaves.size() == maxChaves) {

                split(no, i, no.filhos.get(i));

                if (valor > no.chaves.get(i)) {
                    i++;
                }
            }

            inserirNaoCheio(no.filhos.get(i), valor);
        }
    }

    private void split(NoB pai, int indice, NoB cheio) {

        NoB novo = new NoB(cheio.folha);

        int meio = maxChaves / 2;

        int sobe = cheio.chaves.get(meio);

        for (int i = meio + 1; i < cheio.chaves.size(); i++) {
            novo.chaves.add(cheio.chaves.get(i));
        }

        while (cheio.chaves.size() > meio) {
            cheio.chaves.remove(cheio.chaves.size() - 1);
        }

        if (!cheio.folha) {

            for (int i = meio + 1; i < cheio.filhos.size(); i++) {
                novo.filhos.add(cheio.filhos.get(i));
            }

            while (cheio.filhos.size() > meio + 1) {
                cheio.filhos.remove(cheio.filhos.size() - 1);
            }
        }

        pai.filhos.add(indice + 1, novo);

        pai.chaves.add(indice, sobe);
    }


    public void remover(int valor) {

        remover(raiz, valor);

        if (raiz.chaves.size() == 0 && !raiz.folha) {
            raiz = raiz.filhos.get(0);
        }
    }

    private void remover(NoB no, int valor) {

        int idx = encontrarIndice(no, valor);

        if (idx < no.chaves.size()
                && no.chaves.get(idx) == valor) {

            if (no.folha) {

                no.chaves.remove(idx);

                System.out.println(
                        "Remoção " + valor +
                                ": folha sem underflow"
                );
            }
            else {

                System.out.println(
                        "Remoção " + valor +
                                ": nó interno"
                );

                removerInterno(no, idx);
            }
        }


        else {

            if (no.folha) {
                return;
            }

            boolean ultimoFilho =
                    (idx == no.chaves.size());

            NoB filho = no.filhos.get(idx);

            if (filho.chaves.size() == minChaves) {

                ajustarFilho(no, idx);
            }

            if (ultimoFilho
                    && idx > no.chaves.size()) {

                remover(no.filhos.get(idx - 1), valor);
            }
            else {

                remover(no.filhos.get(idx), valor);
            }
        }
    }

    private void removerInterno(NoB no, int idx) {

        int valor = no.chaves.get(idx);

        NoB esquerdo = no.filhos.get(idx);

        NoB direito = no.filhos.get(idx + 1);

        if (esquerdo.chaves.size() > minChaves) {

            int pred = pegarPredecessor(esquerdo);

            no.chaves.set(idx, pred);

            System.out.println("Substituído por predecessor " + pred);
            remover(esquerdo, pred);
        }


        else if (direito.chaves.size() > minChaves) {

            int succ = pegarSucessor(direito);

            no.chaves.set(idx, succ);

            System.out.println("Substituído por sucessor " + succ);

            remover(direito, succ);
        }


        else {

            merge(no, idx);

            System.out.println("Merge realizado");

            remover(esquerdo, valor);
        }
    }


    private int pegarPredecessor(NoB no) {

        while (!no.folha) {

            no = no.filhos.get(
                    no.filhos.size() - 1
            );
        }

        return no.chaves.get(
                no.chaves.size() - 1
        );
    }


    private int pegarSucessor(NoB no) {

        while (!no.folha) {

            no = no.filhos.get(0);
        }

        return no.chaves.get(0);
    }


    private void ajustarFilho(NoB pai, int idx) {


        if (idx > 0 &&
                pai.filhos.get(idx - 1)
                        .chaves.size() > minChaves) {

            emprestarEsquerda(pai, idx);

            System.out.println(
                    "Redistribuição pela esquerda"
            );
        }


        else if (idx < pai.filhos.size() - 1 &&
                pai.filhos.get(idx + 1)
                        .chaves.size() > minChaves) {

            emprestarDireita(pai, idx);

            System.out.println(
                    "Redistribuição pela direita"
            );
        }


        else {

            if (idx < pai.filhos.size() - 1) {

                merge(pai, idx);
            }
            else {

                merge(pai, idx - 1);
            }

            System.out.println("Merge realizado");
        }
    }



    private void emprestarEsquerda(NoB pai, int idx) {

        NoB filho = pai.filhos.get(idx);

        NoB irmao = pai.filhos.get(idx - 1);

        filho.chaves.add(0,
                pai.chaves.get(idx - 1));

        pai.chaves.set(idx - 1,
                irmao.chaves.remove(irmao.chaves.size() - 1));

        if (!irmao.folha) {

            filho.filhos.add(0, irmao.filhos.remove(irmao.filhos.size() - 1));
        }
    }


    private void emprestarDireita(NoB pai, int idx) {

        NoB filho = pai.filhos.get(idx);

        NoB irmao = pai.filhos.get(idx + 1);

        filho.chaves.add(pai.chaves.get(idx));

        pai.chaves.set(idx, irmao.chaves.remove(0));

        if (!irmao.folha) {

            filho.filhos.add(
                    irmao.filhos.remove(0)
            );
        }
    }


    private void merge(NoB pai, int idx) {

        NoB esquerdo = pai.filhos.get(idx);

        NoB direito = pai.filhos.get(idx + 1);

        esquerdo.chaves.add(
                pai.chaves.remove(idx)
        );

        esquerdo.chaves.addAll(
                direito.chaves
        );

        if (!direito.folha) {

            esquerdo.filhos.addAll(
                    direito.filhos
            );
        }

        pai.filhos.remove(idx + 1);
    }


    private int encontrarIndice(NoB no, int valor) {

        int idx = 0;

        while (idx < no.chaves.size()
                && valor > no.chaves.get(idx)) {

            idx++;
        }

        return idx;
    }


    public void imprimir() {

        imprimir(raiz, 0);
    }

    private void imprimir(NoB no, int nivel) {

        System.out.println("Nível " + nivel + " -> " + no.chaves);

        if (!no.folha) {

            for (NoB filho : no.filhos) {

                imprimir(filho, nivel + 1);
            }
        }
    }
}
