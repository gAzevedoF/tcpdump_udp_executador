package ConexaoServidor;

import java.util.Observable;

public class Conexao extends Observable {

    private int porta;
    private int tamanho;

    public Conexao(int porta, int tamanho) {
        this.porta = porta;
        this.tamanho = tamanho;
        new Thread(new Servidor(porta, tamanho)).start();
    }
}