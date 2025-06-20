package trabalho2.modelos;

public class Ortopedista extends Medico {
    private String areaEspecializada;
    private boolean realizaCirurgia;

    public Ortopedista() {}
    public Ortopedista(String nome, String crm, String contato,
                       String areaEspecializada, boolean realizaCirurgia) {
        super(nome, crm, contato);
        this.areaEspecializada = areaEspecializada;
        this.realizaCirurgia = realizaCirurgia;
    }

    public String getAreaEspecializada() { return areaEspecializada; }
    public void setAreaEspecializada(String area) { this.areaEspecializada = area; }
    public boolean isRealizaCirurgia() { return realizaCirurgia; }
    public void setRealizaCirurgia(boolean cir) { this.realizaCirurgia = cir; }

    @Override
    public String toString() {
        return "Ortopedista{" +
                "nome='" + getNome() + '\'' +
                ", crm='" + getCrm() + '\'' +
                ", contato='" + getContato() + '\'' +
                ", area='" + areaEspecializada + '\'' +
                ", cirurgia=" + realizaCirurgia +
                '}';
    }
}