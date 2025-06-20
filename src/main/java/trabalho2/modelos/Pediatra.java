package trabalho2.modelos;

public class Pediatra extends Medico {
    private String faixaEtariaAtendida;
    private boolean possuiEspecializacaoNeonatal;

    public Pediatra() {}
    public Pediatra(String nome, String crm, String contato,
                    String faixaEtariaAtendida, boolean possuiEspecializacaoNeonatal) {
        super(nome, crm, contato);
        this.faixaEtariaAtendida = faixaEtariaAtendida;
        this.possuiEspecializacaoNeonatal = possuiEspecializacaoNeonatal;
    }

    public String getFaixaEtariaAtendida() { return faixaEtariaAtendida; }
    public void setFaixaEtariaAtendida(String faixa) { this.faixaEtariaAtendida = faixa; }
    public boolean isPossuiEspecializacaoNeonatal() { return possuiEspecializacaoNeonatal; }
    public void setPossuiEspecializacaoNeonatal(boolean neon) { this.possuiEspecializacaoNeonatal = neon; }

    @Override
    public String toString() {
        return "Pediatra{" +
                "nome='" + getNome() + '\'' +
                ", crm='" + getCrm() + '\'' +
                ", contato='" + getContato() + '\'' +
                ", faixaEtariaAtendida='" + faixaEtariaAtendida + '\'' +
                ", neonatal=" + possuiEspecializacaoNeonatal +
                '}';
    }
}