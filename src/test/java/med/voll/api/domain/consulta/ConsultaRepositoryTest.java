package med.voll.api.domain.consulta;

import med.voll.api.domain.consulta.dto.DadosRelatorioConsultaMensal;
import med.voll.api.domain.endereco.DadosEndereco;
import med.voll.api.domain.medico.DadosCadastroMedico;
import med.voll.api.domain.medico.Especialidade;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.paciente.DadosCadastroPaciente;
import med.voll.api.domain.paciente.Paciente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
public class ConsultaRepositoryTest {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void gerarRelatorioConsultaMensal_DeveRetornarDadosRelatorioConsultaMensal(){

        DadosCadastroMedico dadosMedico1 = new DadosCadastroMedico(
            "Dr. João","joao@example.com","123456789","CRM123",Especialidade.CARDIOLOGIA,
            new DadosEndereco(
                "Rua A","Bairro X","12345678",
                "Cidade X","UF",null,null));

        DadosCadastroMedico dadosMedico2 = new DadosCadastroMedico(
            "Dr. Maria","maria@example.com","987654321","CRM456",Especialidade.DERMATOLOGIA,
            new DadosEndereco(
                "Rua A","Bairro X","12345678",
                "Cidade X","UF",null,null));

        DadosCadastroPaciente dadosPaciente1 = new DadosCadastroPaciente(
            "João","joao@example.com","123456789","12345678900",
            new DadosEndereco(
                "Rua A","Bairro X","12345678",
                "Cidade X","UF",null,null));

        DadosCadastroPaciente dadosPaciente2 = new DadosCadastroPaciente(
            "Maria","maria@example.com","987654321","98765432100",
            new DadosEndereco(
                "Rua A","Bairro X","12345678",
                "Cidade X","UF",null,null));

        Medico medico1 = new Medico(dadosMedico1);
        Medico medico2 = new Medico(dadosMedico2);
        Paciente paciente1 = new Paciente(dadosPaciente1);
        Paciente paciente2 = new Paciente(dadosPaciente2);

        LocalDateTime inicioMes = LocalDateTime.of(2025, 6, 1, 0, 0);
        LocalDateTime fimMes = LocalDateTime.of(2025, 6, 30, 23, 59);

        Consulta consulta1 = new Consulta(medico1, paciente1, LocalDateTime.of(2025,6,11,10,0));
        Consulta consulta2 = new Consulta(medico1, paciente2, LocalDateTime.of(2025,6,11,10,0));
        Consulta consulta3 = new Consulta(medico2, paciente1, LocalDateTime.of(2025,6,11,10,0));

        entityManager.persist(medico1);
        entityManager.persist(medico2);
        entityManager.persist(paciente1);
        entityManager.persist(paciente2);
        entityManager.persist(consulta1);
        entityManager.persist(consulta2);
        entityManager.persist(consulta3);
        entityManager.flush();

        // Executar o método a ser testado
        List<DadosRelatorioConsultaMensal> relatorio = consultaRepository.gerarRelatorioConsultaMensal(inicioMes,fimMes);

        // Verificar o resultado
        assertEquals(2, relatorio.size());

        DadosRelatorioConsultaMensal relatorio1 = relatorio.get(0);
        assertEquals("Dr. João", dadosMedico1.nome());
        assertEquals("CRM123", dadosMedico1.crm());
        assertEquals(2L, relatorio1.quantidadeConsultasNoMes());

        DadosRelatorioConsultaMensal relatorio2 = relatorio.get(1);
        assertEquals("Dr. Maria", dadosMedico2.nome());
        assertEquals("CRM456", dadosMedico2.crm());
        assertEquals(1L, relatorio2.quantidadeConsultasNoMes());
    }
}