import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Loja {
    private static Veiculo[] estoqueLocal = new Veiculo[20];
    private static Semaphore semEstoque = new Semaphore(1);
    private static Semaphore itensEstoque = new Semaphore(0);
    private static Semaphore espacoEstoque = new Semaphore(20);

    public static void main(String[] args) throws Exception {
        if (args.length < 1) return;
        int porta = Integer.parseInt(args[0]);

        new Thread(() -> {
            while (true) {
                try {
                    espacoEstoque.acquire();
                    Socket s = new Socket("localhost", 5000);
                    ObjectInputStream in = new ObjectInputStream(s.getInputStream());
                    Veiculo v = (Veiculo) in.readObject();
                    
                    semEstoque.acquire();
                    for(int i=0; i<20; i++) {
                        if(estoqueLocal[i] == null) {
                            estoqueLocal[i] = v;
                            break;
                        }
                    }
                    semEstoque.release();
                    itensEstoque.release();
                    System.out.println("Loja recebeu: " + v.id);
                    s.close();
                } catch (Exception e) {
                    espacoEstoque.release();
                    try { Thread.sleep(2000); } catch (Exception ie) {}
                }
            }
        }).start();

        try (ServerSocket server = new ServerSocket(porta)) {
            while (true) {
                Socket cliente = server.accept();
                new Thread(() -> {
                    try {
                        itensEstoque.acquire();
                        semEstoque.acquire();
                        Veiculo v = null;
                        for(int i=0; i<20; i++) {
                            if(estoqueLocal[i] != null) {
                                v = estoqueLocal[i];
                                estoqueLocal[i] = null;
                                break;
                            }
                        }
                        semEstoque.release();
                        espacoEstoque.release();

                        ObjectOutputStream out = new ObjectOutputStream(cliente.getOutputStream());
                        out.writeObject(v);
                        out.flush();
                        cliente.close();
                    } catch (Exception e) {}
                }).start();
            }
        } catch (Exception e) {}
    }
}