import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class Fabrica {
    private static int pecas = 500;
    private static int contadorVeiculos = 0;
    private static BlockingQueue<Veiculo> esteiraCircular = new ArrayBlockingQueue<>(40);

    public static void main(String[] args) throws Exception {
        System.out.println("Fábrica iniciada. Estoque: 500 peças.");

        // Inicia 4 estações
        for (int i = 1; i <= 4; i++) iniciarEstacao(i);

        // Servidor para Lojas
        try (ServerSocket server = new ServerSocket(5000)) {
            while (true) {
                Socket lojaSocket = server.accept();
                new Thread(() -> {
                    try {
                        Veiculo v = esteiraCircular.take(); // Espera ter veículo
                        ObjectOutputStream out = new ObjectOutputStream(lojaSocket.getOutputStream());
                        out.writeObject(v);
                        System.out.println("Fábrica enviou " + v.id + " para uma loja.");
                        lojaSocket.close();
                    } catch (Exception e) {}
                }).start();
            }
        }
    }

    private static void iniciarEstacao(int estId) {
        Lock[] ferramentas = new ReentrantLock[5];
        for (int i = 0; i < 5; i++) ferramentas[i] = new ReentrantLock();

        for (int i = 0; i < 5; i++) {
            int funcId = i;
            new Thread(() -> {
                String[] cores = {"Vermelho", "Verde", "Azul"};
                String[] tipos = {"SUV", "SEDAN"};
                while (true) {
                    Lock f1 = ferramentas[funcId];
                    Lock f2 = ferramentas[(funcId + 1) % 5];

                    if (f1.tryLock()) {
                        if (f2.tryLock()) {
                            try {
                                if (gastarPeca()) {
                                    int id = proximoId();
                                    Veiculo v = new Veiculo(id, estId, funcId, cores[id % 3], tipos[id % 2]);
                                    esteiraCircular.put(v);
                                    System.out.println("Produzido: " + v);
                                    Thread.sleep(1000); // Tempo de produção
                                }
                            } catch (InterruptedException e) {
                            } finally { f2.unlock(); }
                        }
                        f1.unlock();
                    }
                }
            }).start();
        }
    }

    private synchronized static boolean gastarPeca() {
        if (pecas > 0) { pecas--; return true; }
        return false;
    }

    private synchronized static int proximoId() { return ++contadorVeiculos; }
}