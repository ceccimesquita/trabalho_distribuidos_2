package trabalho2.modelos;

public class Psiquiatra extends Medico {
    private boolean atendeOnline;
    private int duracaoConsultaMin;

    public Psiquiatra() {}
    public Psiquiatra(String nome, String crm, String contato,
                      boolean atendeOnline, int duracaoConsultaMin) {
        super(nome, crm, contato);
        this.atendeOnline = atendeOnline;
        this.duracaoConsultaMin = duracaoConsultaMin;
    }

    public boolean isAtendeOnline() { return atendeOnline; }
    public void setAtendeOnline(boolean online) { this.atendeOnline = online; }
    public int getDuracaoConsultaMin() { return duracaoConsultaMin; }
    public void setDuracaoConsultaMin(int dur) { this.duracaoConsultaMin = dur; }

    @Override
    public String toString() {
        return "Psiquiatra{" +
                "nome='" + getNome() + '\'' +
                ", atendeOnline=" + atendeOnline +
                ", duracaoMin=" + duracaoConsultaMin +
                '}';
    }
}