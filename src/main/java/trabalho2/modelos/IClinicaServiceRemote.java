package trabalho2.modelos;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import trabalho2.modelos.Medico;
import trabalho2.modelos.Paciente;
import trabalho2.modelos.Consulta;

public interface IClinicaServiceRemote extends Remote {
    // Médicos
    void adicionarMedico(Medico medico) throws RemoteException;
    List<Medico> listarMedicos() throws RemoteException;
    List<Medico> buscarPorNome(String nome) throws RemoteException;
    List<Medico> buscarPorEspecialidade(Class<? extends Medico> tipo) throws RemoteException;
    // JSON
    void adicionarMedicoJson(String medicoJson) throws RemoteException;
    String listarMedicosJson() throws RemoteException;

    // Pacientes
    void adicionarPaciente(Paciente paciente) throws RemoteException;
    List<Paciente> listarPacientes() throws RemoteException;
    // JSON
    void adicionarPacienteJson(String pacienteJson) throws RemoteException;
    String listarPacientesJson() throws RemoteException;

}