package trabalho2.modelos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import trabalho2.modelos.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ClinicaServiceImpl extends UnicastRemoteObject implements IClinicaServiceRemote {
    private static final long serialVersionUID = 1L;
    private final ClinicaService delegate;
    private final ObjectMapper objectMapper;

    public ClinicaServiceImpl(String nomeClinica) throws RemoteException {
        super();
        // Delegação para lógica de negócios de agregação
        this.delegate = new ClinicaService(nomeClinica);
        // Configura Jackson para suportar java.time
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    // Métodos Java/RMI
    @Override
    public void adicionarMedico(Medico m) throws RemoteException {
        delegate.adicionarMedico(m);
    }

    @Override
    public List<Medico> listarMedicos() throws RemoteException {
        return delegate.listarMedicos();
    }

    @Override
    public List<Medico> buscarPorNome(String nome) throws RemoteException {
        return delegate.buscarPorNome(nome);
    }

    @Override
    public List<Medico> buscarPorEspecialidade(Class<? extends Medico> tipo) throws RemoteException {
        return delegate.buscarPorEspecialidade(tipo);
    }

    // JSON Médicos
    @Override
    public void adicionarMedicoJson(String medicoJson) throws RemoteException {
        try {
            Medico m = objectMapper.readValue(medicoJson, Medico.class);
            delegate.adicionarMedico(m);
        } catch (JsonProcessingException e) {
            throw new RemoteException("JSON parse error", e);
        }
    }

    @Override
    public String listarMedicosJson() throws RemoteException {
        try {
            return objectMapper.writeValueAsString(delegate.listarMedicos());
        } catch (JsonProcessingException e) {
            throw new RemoteException("JSON write error", e);
        }
    }

    // JSON Pacientes
    @Override
    public void adicionarPaciente(Paciente p) throws RemoteException {
        delegate.adicionarPaciente(p);
    }

    @Override
    public List<Paciente> listarPacientes() throws RemoteException {
        return delegate.listarPacientes();
    }

    @Override
    public void adicionarPacienteJson(String pacienteJson) throws RemoteException {
        try {
            Paciente p = objectMapper.readValue(pacienteJson, Paciente.class);
            delegate.adicionarPaciente(p);
        } catch (JsonProcessingException e) {
            throw new RemoteException("JSON parse error", e);
        }
    }

    @Override
    public String listarPacientesJson() throws RemoteException {
        try {
            return objectMapper.writeValueAsString(delegate.listarPacientes());
        } catch (JsonProcessingException e) {
            throw new RemoteException("JSON write error", e);
        }
    }

    // JSON Consultas
    @Override
    public void agendarConsulta(Consulta c) throws RemoteException {
        delegate.agendarConsulta(c);
    }

    @Override
    public List<Consulta> listarConsultas() throws RemoteException {
        return delegate.listarConsultas();
    }

    @Override
    public void agendarConsultaJson(String consultaJson) throws RemoteException {
        try {
            Consulta c = objectMapper.readValue(consultaJson, Consulta.class);
            delegate.agendarConsulta(c);
        } catch (JsonProcessingException e) {
            throw new RemoteException("JSON parse error", e);
        }
    }

    @Override
    public String listarConsultasJson() throws RemoteException {
        try {
            return objectMapper.writeValueAsString(delegate.listarConsultas());
        } catch (JsonProcessingException e) {
            throw new RemoteException("JSON write error", e);
        }
    }


}
