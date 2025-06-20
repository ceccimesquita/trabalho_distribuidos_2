// src/main/java/trabalho2/client/FullTestClient.java
package trabalho2.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import trabalho2.modelos.*;
import trabalho2.modelos.IClinicaServiceRemote;

import java.rmi.Naming;
import java.time.LocalDateTime;
import java.util.List;

public class Client {
    public static void main(String[] args) {
        try {
            // 1) Lookup do serviço RMI
            IClinicaServiceRemote stub = (IClinicaServiceRemote)
                    Naming.lookup("rmi://localhost:1099/ClinicaService");

            // 2) Configura ObjectMapper com suporte a java.time
            ObjectMapper mapper = new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            // --- Teste de Médicos ---
            System.out.println("=== ADICIONAR MÉDICOS ===");
            Medico base     = new Medico("Dr. Base", "CRM-1000", "base@clinica.com");
            Pediatra pedi   = new Pediatra("Dra. Pedia", "CRM-2000", "pedia@clinica.com","0-12 anos", true);
            Ortopedista ort = new Ortopedista("Dr. Orto", "CRM-3000", "orto@clinica.com","Coluna", false);
            Psiquiatra psi  = new Psiquiatra("Dra. Psi", "CRM-4000", "psi@clinica.com", true, 50);

            for (Medico m : List.of(base, pedi, ort, psi)) {
                String json = mapper.writeValueAsString(m);
                stub.adicionarMedicoJson(json);
                System.out.println("-> Adicionado: " + json);
            }

            System.out.println("\n=== LISTAR MÉDICOS ===");
            String allMedJson = stub.listarMedicosJson();
            System.out.println(allMedJson);
            List<Medico> medicos = mapper.readValue(allMedJson, new TypeReference<>() {});
            medicos.forEach(System.out::println);

            // --- Teste de Pacientes ---
            System.out.println("\n=== ADICIONAR PACIENTES ===");
            Paciente p1 = new Paciente("Alice", "P-101", "alice@ex.com");
            Paciente p2 = new Paciente("Bruno", "P-102", "bruno@ex.com");
            for (Paciente p : List.of(p1, p2)) {
                String json = mapper.writeValueAsString(p);
                stub.adicionarPacienteJson(json);
                System.out.println("-> Adicionado: " + json);
            }

            System.out.println("\n=== LISTAR PACIENTES ===");
            String allPacJson = stub.listarPacientesJson();
            System.out.println(allPacJson);
            List<Paciente> pacientes = mapper.readValue(allPacJson, new TypeReference<>() {});
            pacientes.forEach(System.out::println);

            // --- Teste de Consultas ---
            System.out.println("\n=== AGENDAR CONSULTAS ===");
            // usa o primeiro médico e paciente
            LocalDateTime agora = LocalDateTime.now();
            Consulta c1 = new Consulta(medicos.get(1), pacientes.get(0), agora.plusDays(1));
            Consulta c2 = new Consulta(medicos.get(2), pacientes.get(1), agora.plusDays(2).plusHours(3));
            for (Consulta c : List.of(c1, c2)) {
                String json = mapper.writeValueAsString(c);
                stub.agendarConsultaJson(json);
                System.out.println("-> Agendada: " + json);
            }

            System.out.println("\n=== LISTAR CONSULTAS ===");
            String allConJson = stub.listarConsultasJson();
            System.out.println(allConJson);
            List<Consulta> consultas = mapper.readValue(allConJson, new TypeReference<>() {});
            consultas.forEach(System.out::println);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
