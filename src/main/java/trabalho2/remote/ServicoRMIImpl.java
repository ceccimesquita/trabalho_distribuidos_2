package trabalho2.remote;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import trabalho2.modelos.*;

public class ServicoRMIImpl extends UnicastRemoteObject implements ServicoRMI {

    private final ClinicaServiceImpl clinica = new ClinicaServiceImpl("Clinica");

    public ServicoRMIImpl() throws RemoteException {
        super();
        System.out.println("[DEBUG] ServicoRMIImpl iniciado.");
    }

    @Override
    public byte[] doOperation(String objectReference,
                              String methodId,
                              byte[] arguments) throws RemoteException {
        System.out.printf("[DEBUG] >> chamada recebida: %s.%s(%d bytes)%n",
                objectReference, methodId, arguments.length);

        // Só loga payload JSON nos métodos que enviam JSON
        if (arguments.length > 0 &&
                (methodId.equals("adicionarMedicoJson")
                        || methodId.equals("adicionarPacienteJson")
                        || methodId.equals("agendarConsultaJson"))) {
            try (ObjectInputStream ois0 = new ObjectInputStream(new ByteArrayInputStream(arguments))) {
                String payload = (String) ois0.readObject();
                System.out.println("[DEBUG]     payload JSON → " + payload);
            } catch (Exception e) {
                System.out.println("[DEBUG]     falha ao ler payload: " + e.getMessage());
            }
        } else {
            System.out.println("[DEBUG]     sem payload para este método.");
        }

        try (
                ByteArrayInputStream  bis = new ByteArrayInputStream(arguments);
                ObjectInputStream     ois = arguments.length > 0
                        ? new ObjectInputStream(bis)
                        : null;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream    oos = new ObjectOutputStream(bos)
        ) {
            // roteamento das chamadas
            switch (objectReference) {
                case "Clinica":
                    if (methodId.equals("adicionarMedicoJson")) {
                        String medJson = (String) ois.readObject();
                        clinica.adicionarMedicoJson(medJson);
                        oos.writeObject("OK");
                    } else if (methodId.equals("listarMedicosJson")) {
                        String lista = clinica.listarMedicosJson();
                        oos.writeObject(lista);
                    } else {
                        oos.writeObject("Método desconhecido em Clinica");
                    }
                    break;

                case "Paciente":
                    if (methodId.equals("adicionarPacienteJson")) {
                        String pacJson = (String) ois.readObject();
                        clinica.adicionarPacienteJson(pacJson);
                        oos.writeObject("OK");
                    } else if (methodId.equals("listarPacientesJson")) {
                        String lista = clinica.listarPacientesJson();
                        oos.writeObject(lista);
                    } else {
                        oos.writeObject("Método desconhecido em Paciente");
                    }
                    break;

                case "Consulta":
                    if (methodId.equals("agendarConsultaJson")) {
                        String consJson = (String) ois.readObject();
                        clinica.agendarConsultaJson(consJson);
                        oos.writeObject("OK");
                    } else if (methodId.equals("listarConsultasJson")) {
                        String lista = clinica.listarConsultasJson();
                        oos.writeObject(lista);
                    } else {
                        oos.writeObject("Método desconhecido em Consulta");
                    }
                    break;

                default:
                    oos.writeObject("Serviço inválido: " + objectReference);
            }

            oos.flush();
            byte[] resposta = bos.toByteArray();
            System.out.printf("[DEBUG] << resposta enviada: %d bytes%n", resposta.length);
            return resposta;

        } catch (Exception e) {
            System.err.println("[DEBUG] exceção no servidor: " + e);
            try (ByteArrayOutputStream eb = new ByteArrayOutputStream();
                 ObjectOutputStream  eo = new ObjectOutputStream(eb)) {
                eo.writeObject("Erro: " + e.getMessage());
                eo.flush();
                byte[] errBytes = eb.toByteArray();
                System.out.printf("[DEBUG] << resposta de erro: %d bytes%n", errBytes.length);
                return errBytes;
            } catch (IOException ex) {
                throw new RemoteException("Erro serializando exceção", ex);
            }
        }
    }
}
