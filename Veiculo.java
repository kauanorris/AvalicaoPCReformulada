import java.io.Serializable;

public class Veiculo implements Serializable {
    private static final long serialVersionUID = 1L;
    public int id, idEstacao, idFuncionario, posFabrica;
    public String cor, tipo;

    public Veiculo(int id, int idEstacao, int idFuncionario, String cor, String tipo, int posFabrica) {
        this.id = id;
        this.idEstacao = idEstacao;
        this.idFuncionario = idFuncionario;
        this.cor = cor;
        this.tipo = tipo;
        this.posFabrica = posFabrica;
    }

    @Override
    public String toString() {
        return "ID:" + id + " | " + cor + " | " + tipo + " | Est:" + idEstacao + " | Func:" + idFuncionario + " | Pos:" + posFabrica;
    }
}