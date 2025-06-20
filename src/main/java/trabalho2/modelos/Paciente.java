package trabalho2.modelos;

import java.io.Serializable;

public class Paciente implements Serializable {
    private String nome;
    private String idPaciente;
    private String contato;

    public Paciente() {}
    public Paciente(String nome, String idPaciente, String contato) {
        this.nome = nome;
        this.idPaciente = idPaciente;
        this.contato = contato;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getIdPaciente() { return idPaciente; }
    public void setIdPaciente(String id) { this.idPaciente = id; }
    public String getContato() { return contato; }
    public void setContato(String contato) { this.contato = contato; }

    @Override
    public String toString() {
        return "Paciente{" +
                "nome='" + nome + '\'' +
                ", id='" + idPaciente + '\'' +
                ", contato='" + contato + '\'' +
                '}';
    }
}