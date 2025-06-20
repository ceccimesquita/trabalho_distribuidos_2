package trabalho2.server;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import trabalho2.modelos.ClinicaServiceImpl;

public class Server {
    public static void main(String[] args) {
        try {
            // 1) Inicia um registry RMI embutido na porta 1099
            LocateRegistry.createRegistry(1099);

            // 2) Cria a implementação remota, passando um nome para a clínica
            ClinicaServiceImpl servico = new ClinicaServiceImpl("Clínica Central");

            // 3) Registra (bind) o serviço sob o nome "ClinicaService"
            Naming.rebind("rmi://localhost/ClinicaService", servico);

            System.out.println("Servidor RMI de Clínica pronto em rmi://localhost/ClinicaService");
        } catch (Exception e) {
            System.err.println("Erro ao iniciar o servidor de Clínica:");
            e.printStackTrace();
        }
    }
}
