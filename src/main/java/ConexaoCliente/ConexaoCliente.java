package ConexaoCliente;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Scanner;

public class ConexaoCliente extends Observable {

    private int porta;
    private InetAddress ipServidor;
    private int tamanho;
    private String nomeArquivo;
    private String comando;

    public ConexaoCliente(int porta, InetAddress ipServidor, int tamanho, String nomeArquivo, String comando) {
        this.porta = porta;
        this.ipServidor = ipServidor;
        this.tamanho = tamanho;
        this.nomeArquivo = nomeArquivo;
        this.comando = comando;
        new Thread(new Cliente(porta, ipServidor, tamanho, nomeArquivo, comando)).start();
    }

    public static void main(String[] args) throws UnknownHostException {
        Scanner resposta = new Scanner(System.in);

        int denovo = 1;
        while (denovo == 1) {
            System.out.println("===INICIANDO CLIENTE===");
            System.out.println("DIGITE A PORTA DESEJADA: ");
//        int porta = resposta.nextInt();
            int porta = 3300;

            System.out.println("DIGITE O IP DO SERVIDOR DESEJADA: ");
//        InetAddress ipServidor = InetAddress.getByName(resposta.next());
            InetAddress ipServidor = InetAddress.getByName("192.168.25.4");

            System.out.println("DIGITE O TAMANHO ESTIMADO DO BUFFER QUE SERÁ RECEBIDO: ");
//        int tamanho = resposta.nextInt();
            int tamanho = 1024;

            System.out.println("DIGITE O NOME DESEJADO PARA O ARQUIVO DE RESPOSTA: ");
//        String nomeArquivo = resposta.next();
            String nomeArquivo = "resposta";

            System.out.println("DIGITE O COMANDO QUE DESEJA EXECUTAR: ");
//        String comando = resposta.next();
            String comando = "ping 8.8.8.8";

            ConexaoCliente c = new ConexaoCliente(porta, ipServidor, tamanho, nomeArquivo, comando);

            denovo = resposta.nextInt();
            while (true) {
                if (denovo < 1 || denovo > 2) {
                    System.out.println("POR FAVOR ESCOLHA UMA DAS OPÇÕES -> [1] = SIM, [2] = NÃO");
                    denovo = resposta.nextInt();
                }else {
                    break;
                }
            }
        }
    }
}