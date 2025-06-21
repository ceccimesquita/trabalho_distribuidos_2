package trabalho2.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicoRMI extends Remote {
    /**
     * @param objectReference nome do serviço ("Clinica", "Paciente", "Consulta", ...)
     * @param methodId        id ou nome do método ("adicionarMedico", "listarMedicosJson", ...)
     * @param arguments       argumentos serializados (JSON, Java-serial, protobuf…)
     * @return                 resposta serializada
     */
    byte[] doOperation(String objectReference, String methodId, byte[] arguments)
            throws RemoteException;
}
