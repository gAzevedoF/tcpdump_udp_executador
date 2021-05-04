package ConexaoCliente;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.*;

class Cliente implements Runnable {
    private final int porta;
    private final InetAddress ipServidor;
    private final int tamanhoBufferEstimado;
    private final String nomeArquivo;
    private final String comando;
    private DatagramSocket socketUDP;

    public Cliente(int porta, InetAddress ipServidor, int tamanhoBufferEstimado, String nomeArquivo, String comando) {
        this.porta = porta;
        this.ipServidor = ipServidor;
        this.tamanhoBufferEstimado = tamanhoBufferEstimado;
        this.nomeArquivo = nomeArquivo;
        this.comando = comando;
        this.socketUDP = null;
    }

    @Override
    public void run() {
        byte[] bufferParaReceber = new byte[tamanhoBufferEstimado];

        try {
            this.socketUDP = new DatagramSocket();

            byte[] bufferParaEnviar = comando.getBytes();
            DatagramPacket pacoteParaEnviar = new DatagramPacket(bufferParaEnviar,
                                                                bufferParaEnviar.length,
                                                                ipServidor,
                                                                porta);

            socketUDP.send(pacoteParaEnviar);
            System.out.println("COMANDO ENVIADO PARA O SERVIDOR NA PORTA " + porta);

            DatagramPacket pacoteRecebidoDoServidor = new DatagramPacket(bufferParaReceber, bufferParaReceber.length);
            socketUDP.receive(pacoteRecebidoDoServidor);
            System.out.println("ARQUIVO RECEBIDO DO SERVIDOR");

            this.criarArquivo(pacoteRecebidoDoServidor);
            System.out.println("ARQUIVO SALVO COMO " + this.nomeArquivo + ".pcap");
            System.out.println("DESEJA EXECUTAR OUTRO COMANDO ? [1] = SIM, [2] = NÃO");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void criarArquivo(DatagramPacket pacoteRecebido) throws IOException {
        int cont = 0;
        while (pacoteRecebido.getData()[cont] != 0) {
            cont++;
        }

        byte[] dadosRecebidosSemComplementos = new byte[cont];

        for (int i = 0; i <= cont ; i++) {
            if(pacoteRecebido.getData()[i] != 0) {
                dadosRecebidosSemComplementos[i] = pacoteRecebido.getData()[i];
            }
        }

        File arquivo = new File(this.nomeArquivo + ".pcap");
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(arquivo));
        bos.write(dadosRecebidosSemComplementos);
        bos.close();

    }
}