// src/main/java/trabalho2/client/InteractiveClient.java
package trabalho2.client;

import trabalho2.remote.ServicoRMI;
import trabalho2.modelos.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.*;
import java.rmi.Naming;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class ClientF {
    public static void main(String[] args) throws Exception {
        // 1) Lookup do serviço
        ServicoRMI stub = (ServicoRMI) Naming.lookup("rmi://localhost:1099/ClinicaService");

        // 2) Jackson configurado para java.time
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== MENU ===");
            System.out.println("1) Adicionar Médico");
            System.out.println("2) Listar Médicos");
            System.out.println("3) Adicionar Paciente");
            System.out.println("4) Listar Pacientes");
            System.out.println("5) Agendar Consulta");
            System.out.println("6) Listar Consultas");
            System.out.println("0) Sair");
            System.out.print("Escolha: ");
            int opc = Integer.parseInt(sc.nextLine());

            switch (opc) {
                case 1 -> {
                    // --- adicionar médico ---
                    System.out.print("Tipo [1=Pediatra 2=Ortopedista 3=Psiquiatra]: ");
                    int tipo = Integer.parseInt(sc.nextLine());
                    System.out.print("Nome: ");    String nome = sc.nextLine();
                    System.out.print("CRM: ");     String crm  = sc.nextLine();
                    System.out.print("Contato: "); String cont = sc.nextLine();

                    Medico m;
                    switch (tipo) {
                        case 1 -> {
                            System.out.print("Faixa Etária: ");
                            String faixa = sc.nextLine();
                            System.out.print("Especialização Neonatal (true/false): ");
                            boolean neonatal = Boolean.parseBoolean(sc.nextLine());
                            m = new Pediatra(nome, crm, cont, faixa, neonatal);
                        }
                        case 2 -> {
                            System.out.print("Área Especializada: ");
                            String area = sc.nextLine();
                            System.out.print("Realiza Cirurgia (true/false): ");
                            boolean cir = Boolean.parseBoolean(sc.nextLine());
                            m = new Ortopedista(nome, crm, cont, area, cir);
                        }
                        case 4 -> {
                            System.out.print("Atende Online (true/false): ");
                            boolean onl = Boolean.parseBoolean(sc.nextLine());
                            System.out.print("Duração Consulta (min): ");
                            int dur = Integer.parseInt(sc.nextLine());
                            m = new Psiquiatra(nome, crm, cont, onl, dur);
                        }
                        default -> m = new Medico(nome, crm, cont);
                    }
                    String json = mapper.writeValueAsString(m);
                    byte[] resp = stub.doOperation("Clinica", "adicionarMedicoJson", serializeString(json));
                    System.out.println("--> Servidor: " + deserializeString(resp));
                }

                case 2 -> {
                    // --- listar médicos ---
                    byte[] resp = stub.doOperation("Clinica", "listarMedicosJson", new byte[0]);
                    String allJson = deserializeString(resp);
                    System.out.println("JSON retornado: " + allJson);
                    List<Medico> lista = mapper.readValue(allJson, new TypeReference<List<Medico>>(){});
                    System.out.println("=== Médicos Cadastrados ===");
                    lista.forEach(System.out::println);
                }

                case 3 -> {
                    // --- adicionar paciente ---
                    System.out.print("Nome: ");       String nomeP = sc.nextLine();
                    System.out.print("ID Paciente: ");String idP   = sc.nextLine();
                    System.out.print("Contato: ");    String contP = sc.nextLine();

                    Paciente p = new Paciente(nomeP, idP, contP);
                    String json = mapper.writeValueAsString(p);
                    byte[] resp = stub.doOperation("Paciente", "adicionarPacienteJson", serializeString(json));
                    System.out.println("--> Servidor: " + deserializeString(resp));
                }

                case 4 -> {
                    // --- listar pacientes ---
                    byte[] resp = stub.doOperation("Paciente", "listarPacientesJson", new byte[0]);
                    String allJson = deserializeString(resp);
                    System.out.println("JSON retornado: " + allJson);
                    List<Paciente> listaP = mapper.readValue(allJson, new TypeReference<List<Paciente>>(){});
                    System.out.println("=== Pacientes Cadastrados ===");
                    listaP.forEach(System.out::println);
                }

                case 5 -> {
                    // --- agendar consulta ---
                    System.out.print("CRM do Médico: ");       String crmMed = sc.nextLine();
                    System.out.print("ID do Paciente: ");      String idPac  = sc.nextLine();
                    System.out.print("Data e Hora (yyyy-MM-ddTHH:mm): ");
                    LocalDateTime dt = LocalDateTime.parse(sc.nextLine());

                    Medico refM = new Medico(); refM.setCrm(crmMed);
                    Paciente refP = new Paciente(); refP.setIdPaciente(idPac);
                    Consulta c = new Consulta(refM, refP, dt);

                    String json = mapper.writeValueAsString(c);
                    byte[] resp = stub.doOperation("Consulta", "agendarConsultaJson", serializeString(json));
                    System.out.println("--> Servidor: " + deserializeString(resp));
                }

                case 6 -> {
                    // --- listar consultas ---
                    byte[] resp = stub.doOperation("Consulta", "listarConsultasJson", new byte[0]);
                    String allJson = deserializeString(resp);
                    System.out.println("JSON retornado: " + allJson);
                    List<Consulta> listaC = mapper.readValue(allJson, new TypeReference<List<Consulta>>(){});
                    System.out.println("=== Consultas Agendadas ===");
                    listaC.forEach(System.out::println);
                }

                case 0 -> {
                    System.out.println("Saindo...");
                    sc.close();
                    return;
                }

                default -> System.out.println("Opção inválida!");
            }
        }
    }

    // Serializa String → byte[]
    private static byte[] serializeString(String s) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream    oos = new ObjectOutputStream(bos)) {
            oos.writeObject(s);
            oos.flush();
            return bos.toByteArray();
        }
    }

    // Desserializa byte[] → String
    private static String deserializeString(byte[] data) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return (String) ois.readObject();
        }
    }
}
