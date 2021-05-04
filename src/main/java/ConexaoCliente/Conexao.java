package ConexaoCliente;

import java.net.InetAddress;
import java.util.Observable;

public class Conexao extends Observable {

    private int porta;
    private InetAddress ipServidor;
    private int tamanho;
    private String nomeArquivo;
    private String comando;

    public Conexao(int porta, InetAddress ipServidor, int tamanho, String nomeArquivo, String comando) {
        this.porta = porta;
        this.ipServidor = ipServidor;
        this.tamanho = tamanho;
        this.nomeArquivo = nomeArquivo;
        this.comando = comando;
        new Thread(new Cliente(porta, ipServidor, tamanho, nomeArquivo, comando)).start();
    }
}