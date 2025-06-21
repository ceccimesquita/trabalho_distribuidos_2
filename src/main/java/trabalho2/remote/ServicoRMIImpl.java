package trabalho2.remote;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import trabalho2.modelos.*;   // seus modelos e serviços “locais”

public class ServicoRMIImpl extends UnicastRemoteObject implements ServicoRMI {
    private final ClinicaServiceImpl clinica = new ClinicaServiceImpl("nome clinica");

    public ServicoRMIImpl() throws RemoteException {
        super();
    }

    @Override
    public byte[] doOperation(String objectReference,
                              String methodId,
                              byte[] arguments) throws RemoteException {
        try (
                ByteArrayInputStream  bis = new ByteArrayInputStream(arguments);
                ObjectInputStream     ois = new ObjectInputStream(bis);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream    oos = new ObjectOutputStream(bos)
        ) {
            switch (objectReference) {
                case "Clinica":
                    switch (methodId) {
                        case "adicionarMedicoJson":
                            String medJson = (String) ois.readObject();
                            clinica.adicionarMedicoJson(medJson);
                            oos.writeObject("OK");
                            break;
                        case "listarMedicosJson":
                            String listaMed = clinica.listarMedicosJson();
                            oos.writeObject(listaMed);
                            break;
                        default:
                            oos.writeObject("Método desconhecido em Clinica");
                    }
                    break;

                case "Paciente":
                    switch (methodId) {
                        case "adicionarPacienteJson":
                            String pacJson = (String) ois.readObject();
                            clinica.adicionarPacienteJson(pacJson);
                            oos.writeObject("OK");
                            break;
                        case "listarPacientesJson":
                            String listaPac = clinica.listarPacientesJson();
                            oos.writeObject(listaPac);
                            break;
                        default:
                            oos.writeObject("Método desconhecido em Paciente");
                    }
                    break;

                case "Consulta":
                    switch (methodId) {
                        case "agendarConsultaJson":
                            String consJson = (String) ois.readObject();
                            clinica.agendarConsultaJson(consJson);
                            oos.writeObject("OK");
                            break;
                        case "listarConsultasJson":
                            String listaCons = clinica.listarConsultasJson();
                            oos.writeObject(listaCons);
                            break;
                        default:
                            oos.writeObject("Método desconhecido em Consulta");
                    }
                    break;

                default:
                    oos.writeObject("Serviço inválido: " + objectReference);
            }

            oos.flush();
            return bos.toByteArray();

        } catch (Exception e) {
            // em caso de erro, devolve a mensagem
            try (ByteArrayOutputStream errB = new ByteArrayOutputStream();
                 ObjectOutputStream    errO = new ObjectOutputStream(errB)) {
                errO.writeObject("Erro: " + e.getMessage());
                errO.flush();
                return errB.toByteArray();
            } catch (IOException ex) {
                throw new RemoteException("Erro serializando exceção", ex);
            }
        }
    }
}
