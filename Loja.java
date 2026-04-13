import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Loja {
    private static BlockingQueue<Veiculo> estoqueLocal = new ArrayBlockingQueue<>(20);

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Uso: java Loja <porta>");
            return;
        }
        int porta = Integer.parseInt(args[0]);

        // Thread para buscar da Fábrica
        new Thread(() -> {
            while (true) {
                try (Socket s = new Socket("localhost", 5000)) {
                    ObjectInputStream in = new ObjectInputStream(s.getInputStream());
                    Veiculo v = (Veiculo) in.readObject();
                    estoqueLocal.put(v);
                    System.out.println("Loja recebeu: " + v.id);
                } catch (Exception e) {
                    try { Thread.sleep(2000); } catch (Exception ie) {}
                }
            }
        }).start();

        // Servidor para Clientes
        try (ServerSocket server = new ServerSocket(porta)) {
            System.out.println("Loja pronta na porta " + porta);
            while (true) {
                Socket cliente = server.accept();
                Veiculo v = estoqueLocal.take();
                ObjectOutputStream out = new ObjectOutputStream(cliente.getOutputStream());
                out.writeObject(v);
                cliente.close();
            }
        } catch (Exception e) {}
    }
}