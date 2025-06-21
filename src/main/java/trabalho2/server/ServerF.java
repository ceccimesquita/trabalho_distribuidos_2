package trabalho2.server;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import trabalho2.remote.ServicoRMIImpl;

public class ServerF {
    public static void main(String[] args) {
        try {
            // 1) Cria registry na porta padrão 1099
            LocateRegistry.createRegistry(1099);

            // 2) Instancia e registra nosso ServicoRMIImpl
            ServicoRMIImpl servico = new ServicoRMIImpl();
            Naming.rebind("rmi://localhost:1099/ClinicaService", servico);

            System.out.println("Servidor RMI  pronto em rmi://localhost:1099/ClinicaService");
        } catch (Exception e) {
            System.err.println("Falha ao iniciar servidor RMI:");
            e.printStackTrace();
        }
    }
}
