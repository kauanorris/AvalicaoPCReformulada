import java.io.Serializable;

public class Veiculo implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public int id, idEstacao, idFuncionario;
    public String cor, tipo;

    public Veiculo(int id, int idEstacao, int idFuncionario, String cor, String tipo) {
        this.id = id;
        this.idEstacao = idEstacao;
        this.idFuncionario = idFuncionario;
        this.cor = cor;
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return String.format("Veículo [ID:%d | %s | %s | Estação:%d | Func:%d]", id, cor, tipo, idEstacao, idFuncionario);
    }
}