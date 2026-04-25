import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Fabrica {
    private static int pecas = 500;
    private static int contadorId = 0;
    private static int posEsteira = 0;
    private static Veiculo[] esteira = new Veiculo[40];
    
    private static Semaphore semPecas = new Semaphore(1);
    private static Semaphore semId = new Semaphore(1);
    private static Semaphore semEsteiraAcesso = new Semaphore(1);
    private static Semaphore semSolicitacoes = new Semaphore(5);
    private static Semaphore itensEsteira = new Semaphore(0);
    private static Semaphore espacoEsteira = new Semaphore(40);

    public static void main(String[] args) throws Exception {
        for (int i = 1; i <= 4; i++) {
            iniciarEstacao(i);
        }

        try (ServerSocket server = new ServerSocket(5000)) {
            while (true) {
                Socket loja = server.accept();
                new Thread(() -> {
                    try {
                        semSolicitacoes.acquire();
                        itensEsteira.acquire();
                        semEsteiraAcesso.acquire();
                        
                        Veiculo v = null;
                        for(int i=0; i<40; i++) {
                            if(esteira[i] != null) {
                                v = esteira[i];
                                esteira[i] = null;
                                break;
                            }
                        }
                        
                        semEsteiraAcesso.release();
                        espacoEsteira.release();
                        semSolicitacoes.release();

                        ObjectOutputStream out = new ObjectOutputStream(loja.getOutputStream());
                        out.writeObject(v);
                        out.flush();
                        loja.close();
                    } catch (Exception e) {}
                }).start();
            }
        } catch (Exception e) {}
    }

    private static void iniciarEstacao(int estId) {
        Semaphore[] ferramentas = new Semaphore[5];
        for (int i = 0; i < 5; i++) ferramentas[i] = new Semaphore(1);

        for (int i = 0; i < 5; i++) {
            int fId = i;
            new Thread(() -> {
                String[] cores = {"R", "G", "B"};
                String[] tipos = {"SUV", "SEDAN"};
                while (true) {
                    Semaphore fEsq = ferramentas[fId];
                    Semaphore fDir = ferramentas[(fId + 1) % 5];

                    try {
                        if (fEsq.tryAcquire()) {
                            if (fDir.tryAcquire()) {
                                semPecas.acquire();
                                if (pecas > 0) {
                                    pecas--;
                                    semPecas.release();

                                    semId.acquire();
                                    int id = ++contadorId;
                                    semId.release();

                                    espacoEsteira.acquire();
                                    semEsteiraAcesso.acquire();
                                    int pos = (posEsteira++) % 40;
                                    Veiculo v = new Veiculo(id, estId, fId, cores[id % 3], tipos[id % 2], pos);
                                    esteira[pos] = v;
                                    System.out.println("Produzido: " + v);
                                    semEsteiraAcesso.release();
                                    itensEsteira.release();
                                    
                                    Thread.sleep(1000);
                                } else {
                                    semPecas.release();
                                }
                                fDir.release();
                            }
                            fEsq.release();
                        }
                        Thread.sleep(10);
                    } catch (Exception e) {}
                }
            }).start();
        }
    }
}