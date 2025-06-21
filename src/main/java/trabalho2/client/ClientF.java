package trabalho2.client;

import trabalho2.remote.ServicoRMI;

import java.io.*;
import java.rmi.Naming;
import java.time.LocalDateTime;

public class ClientF {
    public static void main(String[] args) throws Exception {
        // 1) Lookup do stub no RMI Registry
        ServicoRMI stub = (ServicoRMI) Naming.lookup("//localhost/ServicoRMI");

        // --- MÉTODOS DE MÉDICO ---
        System.out.println("=== MÉDICOS ===");
        // 1.1 Adicionar 2 médicos
        for (String medJson : new String[]{
                "{\"type\":\"Medico\",\"nome\":\"Dr. Alfa\",\"crm\":\"CRM-100\",\"contato\":\"alfa@c.com\"}",
                "{\"type\":\"Pediatra\",\"nome\":\"Dra. Beta\",\"crm\":\"CRM-200\",\"contato\":\"beta@c.com\",\"faixaEtariaAtendida\":\"0-12\",\"possuiEspecializacaoNeonatal\":true}"
        }) {
            byte[] payload = serializeString(medJson);
            byte[] resp = stub.doOperation("Clinica", "adicionarMedicoJson", payload);
            System.out.println("addMed → " + deserializeString(resp));
        }

        // 1.2 Listar médicos (payload vazio)
        byte[] listMed = stub.doOperation("Clinica", "listarMedicosJson", emptyPayload());
        String medicosJson = deserializeString(listMed);
        System.out.println("listarMedicosJson() → " + medicosJson);

        // --- MÉTODOS DE PACIENTE ---
        System.out.println("\n=== PACIENTES ===");
        for (String pacJson : new String[]{
                "{\"nome\":\"Alice\",\"idPaciente\":\"P-101\",\"contato\":\"alice@ex.com\"}",
                "{\"nome\":\"Bruno\",\"idPaciente\":\"P-102\",\"contato\":\"bruno@ex.com\"}"
        }) {
            byte[] payload = serializeString(pacJson);
            byte[] resp = stub.doOperation("Paciente", "adicionarPacienteJson", payload);
            System.out.println("addPac → " + deserializeString(resp));
        }

        byte[] listPac = stub.doOperation("Paciente", "listarPacientesJson", emptyPayload());
        System.out.println("listarPacientesJson() → " + deserializeString(listPac));

        // --- MÉTODOS DE CONSULTA ---
        System.out.println("\n=== CONSULTAS ===");
        LocalDateTime now = LocalDateTime.now();
        String[] consJsons = new String[]{
                String.format("{\"medico\":{\"crm\":\"CRM-100\"},\"paciente\":{\"idPaciente\":\"P-101\"},\"dataHora\":\"%s\"}", now.plusDays(1)),
                String.format("{\"medico\":{\"crm\":\"CRM-200\"},\"paciente\":{\"idPaciente\":\"P-102\"},\"dataHora\":\"%s\"}", now.plusDays(2))
        };
        for (String consJson : consJsons) {
            byte[] payload = serializeString(consJson);
            byte[] resp = stub.doOperation("Consulta", "agendarConsultaJson", payload);
            System.out.println("agendar → " + deserializeString(resp));
        }

        byte[] listCons = stub.doOperation("Consulta", "listarConsultasJson", emptyPayload());
        System.out.println("listarConsultasJson() → " + deserializeString(listCons));
    }

    // Serializa uma String qualquer em byte[]
    private static byte[] serializeString(String s) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(s);
            oos.flush();
        }
        return bos.toByteArray();
    }

    // Gera um “payload vazio” válido (só cabeçalho de ObjectOutputStream)
    private static byte[] emptyPayload() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.flush();
        }
        return bos.toByteArray();
    }

    // Desserializa a resposta do servidor (espera sempre uma String)
    private static String deserializeString(byte[] data) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return (String) in.readObject();
        }
    }
}
