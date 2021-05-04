package ConexaoServidor;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

public class Servidor implements Runnable {

    private int porta;
    private int tamanho;
    private DatagramSocket socket;

    private static final String REGRA_DE_SEPARACAO_PARA_COMANDO = " ";

    public Servidor(int porta, int tamanho) {
        this.porta = porta;
        this.tamanho = tamanho;
        this.socket = null;
    }

    @Override
    public void run() {
        while (true) {
            byte[] bufferReceber = new byte[this.tamanho];

            try {
                socket = new DatagramSocket(this.porta);
                System.out.println("SERVIDOR COM IP " + InetAddress.getLocalHost().getHostAddress() + " SUBIU NA PORTA " + this.porta);
            } catch (SocketException | UnknownHostException e) {
                e.printStackTrace();
            }
            while (true) {
                DatagramPacket pacoteRecebido = new DatagramPacket(bufferReceber, bufferReceber.length);

                try {
                    System.out.println("AGUARDANDO CLIENTE");
                    socket.receive(pacoteRecebido);
                    System.out.println("COMANDO RECEBIDO DO CLIENTE " + pacoteRecebido.getAddress().getHostAddress());

                    this.executarComando(pacoteRecebido);

                    InetAddress enderecoIP = pacoteRecebido.getAddress();
                    int porta = pacoteRecebido.getPort();

                    byte[] arquivoGerado = this.converterArquivoEmBytes();

                    DatagramPacket pacoteEnviado = new DatagramPacket(arquivoGerado, arquivoGerado.length, pacoteRecebido.getAddress(), porta);
                    socket.send(pacoteEnviado);

                } catch (Exception e) {

                    continue;
                }
            }
        }
    }

    public byte[] converterArquivoEmBytes() throws IOException {
        File file = new File("log.txt");
        byte[] arquivo = Files.readAllBytes(file.toPath());

        PrintWriter writer = new PrintWriter("log.txt");
        writer.write("");
        writer.close();

        return arquivo;
    }

    private String[] gerarComando(DatagramPacket pacoteParaReceber) {
        int cont = 0;
        while (pacoteParaReceber.getData()[cont] != 0) {
            cont++;
        }

        byte[] listaAjustada = new byte[cont];
        int i = 0;
        for (Byte dado : pacoteParaReceber.getData()) {
            if (dado != 0) {
                listaAjustada[i] = dado;
                i++;
            }
        }

        String comando = new String(listaAjustada);
        System.out.println("INICIANDO EXECUÇÃO DO COMANDO: " + comando);
        return comando.split(REGRA_DE_SEPARACAO_PARA_COMANDO);
    }

    public void executarComando(DatagramPacket pacote) throws InterruptedException, IOException {
        String[] comando = this.gerarComando(pacote);
        ProcessBuilder pb = new ProcessBuilder(comando);
        pb.redirectErrorStream(true);
        File log = new File("log.txt");
        pb.redirectOutput(ProcessBuilder.Redirect.appendTo(log));
        Process p = pb.start();
        TimeUnit.SECONDS.sleep(10);
        p.waitFor();
        System.out.println("TERMINOU EXECUÇÃO DO COMANDO");
    }
}