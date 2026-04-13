import java.io.*;
import java.net.*;
import java.util.Random;

public class Cliente {
    public static void main(String[] args) {
        for (int i = 1; i <= 20; i++) {
            int idCliente = i;
            new Thread(() -> {
                Random rand = new Random();
                while (true) {
                    int portaLoja = 6001 + rand.nextInt(3); // Sorteia entre 6001, 6002, 6003
                    try (Socket s = new Socket("localhost", portaLoja)) {
                        ObjectInputStream in = new ObjectInputStream(s.getInputStream());
                        Veiculo v = (Veiculo) in.readObject();
                        System.out.println("Cliente " + idCliente + " COMPROU: " + v);
                        Thread.sleep(3000 + rand.nextInt(5000)); // Espera um tempo para comprar outro
                    } catch (Exception e) {
                        // Loja vazia ou offline, tenta de novo em breve
                        try { Thread.sleep(1000); } catch (Exception ie) {}
                    }
                }
            }).start();
        }
    }
}