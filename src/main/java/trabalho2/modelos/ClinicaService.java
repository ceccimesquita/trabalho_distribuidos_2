package trabalho2.modelos;
import java.util.ArrayList; import java.util.List;
public class ClinicaService {
    private final String nomeClinica;
    private final List<Medico> medicos;
    private final List<Paciente> pacientes;
    private final List<Consulta> consultas;

    public ClinicaService(String nomeClinica) {
        this.nomeClinica = nomeClinica;
        medicos = new ArrayList<>();
        pacientes = new ArrayList<>();
        consultas = new ArrayList<>();
    }

    public String getNomeClinica() { return nomeClinica; }
    public void adicionarMedico(Medico m) { medicos.add(m); }
    public List<Medico> listarMedicos() { return new ArrayList<>(medicos); }
    public void adicionarPaciente(Paciente p) { pacientes.add(p); }
    public List<Paciente> listarPacientes() { return new ArrayList<>(pacientes); }
    public void agendarConsulta(Consulta c) { consultas.add(c); }
    public List<Consulta> listarConsultas() { return new ArrayList<>(consultas); }
    public List<Medico> buscarPorNome(String nome) {
        List<Medico> res=new ArrayList<>();
        for(Medico m:medicos) if(m.getNome().equalsIgnoreCase(nome)) res.add(m);
        return res;
    }
    public List<Medico> buscarPorEspecialidade(Class<? extends Medico> tipo) {
        List<Medico> res=new ArrayList<>();
        for(Medico m:medicos) if(tipo.isInstance(m)) res.add(m);
        return res;
    }
}