import java.io.*;
import java.net.*;
import java.util.*;

public class Cliente {
    public static void main(String[] args) {
        for (int i = 1; i <= 20; i++) {
            int idC = i;
            new Thread(() -> {
                Random r = new Random();
                List<Veiculo> garagem = new ArrayList<>();
                while (true) {
                    int porta = 6001 + r.nextInt(3);
                    try (Socket s = new Socket("localhost", porta)) {
                        ObjectInputStream in = new ObjectInputStream(s.getInputStream());
                        Veiculo v = (Veiculo) in.readObject();
                        garagem.add(v);
                        System.out.println("Cliente " + idC + " comprou: " + v.id + " (Total: " + garagem.size() + ")");
                        Thread.sleep(2000 + r.nextInt(3000));
                    } catch (Exception e) {
                        try { Thread.sleep(1000); } catch (Exception ie) {}
                    }
                }
            }).start();
        }
    }
}