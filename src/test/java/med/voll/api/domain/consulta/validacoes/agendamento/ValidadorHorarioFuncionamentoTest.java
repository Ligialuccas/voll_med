package med.voll.api.domain.consulta.validacoes.agendamento;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.dto.DadosAgendamentoConsulta;
import med.voll.api.domain.medico.Especialidade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidadorHorarioFuncionamentoTest {

    @InjectMocks
    private ValidadorHorarioFuncionamento validador;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void validar_ConsultaDentroDoHorario_NaoDeveLancarExcecao() {
        // Cenário
        DadosAgendamentoConsulta dados = criarDadosAgendamentoConsulta(
                LocalDateTime.of(2025, 8, 11, 9, 0)
        );

        validador.validar(dados);

    }

    @Test
    void validar_ConsultaForaDoHorario_LancaValidacaoException() {
        DadosAgendamentoConsulta dados = criarDadosAgendamentoConsulta(
                LocalDateTime.of(2025, 8, 11, 19, 0)
        );

        assertThrows(ValidacaoException.class, () -> validador.validar(dados));
    }

    @Test
    void validar_ConsultaNoDomingo_LancaValidacaoException() {
        DadosAgendamentoConsulta dados = criarDadosAgendamentoConsulta(
                LocalDateTime.of(2023, 6, 4, 10, 0)
        );

        assertThrows(ValidacaoException.class, () -> validador.validar(dados));
    }

    @Test
    void validar_ConsultaNoHorarioDeAbertura_NaoDeveLancarExcecao() {
        DadosAgendamentoConsulta dados = criarDadosAgendamentoConsulta(
                LocalDateTime.of(2023, 6, 1, 7, 0)
        );

        assertDoesNotThrow(() -> validador.validar(dados));
    }

    @Test
    void validar_ConsultaNoHorarioDeEncerramento_NaoDeveLancarExcecao() {
        DadosAgendamentoConsulta dados = criarDadosAgendamentoConsulta(
                LocalDateTime.of(2023, 6, 1, 18, 0)
        );

        assertDoesNotThrow(() -> validador.validar(dados));
    }

    @Test
    void validar_ConsultaEmDiaDeSemana_NaoDeveLancarExcecao() {
        DadosAgendamentoConsulta dados = criarDadosAgendamentoConsulta(
                LocalDateTime.of(2023, 6, 5, 10, 0)
        );

        assertDoesNotThrow(() -> validador.validar(dados));
    }

    @Test
    void validar_ConsultaAntesDoHorarioDeAberturaEmDiaDeSemana_LancaValidacaoException() {
        DadosAgendamentoConsulta dados = criarDadosAgendamentoConsulta(
                LocalDateTime.of(2023, 6, 1, 6, 59)
        );

        assertThrows(ValidacaoException.class, () -> validador.validar(dados));
    }

    @Test
    void validar_ConsultaDepoisDoHorarioDeEncerramentoEmDiaDeSemana_LancaValidacaoException() {
        DadosAgendamentoConsulta dados = criarDadosAgendamentoConsulta(
                LocalDateTime.of(2023, 6, 1, 18, 1)
        );

        assertThrows(ValidacaoException.class, () -> validador.validar(dados));
    }

    private DadosAgendamentoConsulta criarDadosAgendamentoConsulta(LocalDateTime data) {
        Long idMedico = 1L;
        Long idPaciente = 2L;
        Especialidade especialidade = Especialidade.CARDIOLOGIA;

        return new DadosAgendamentoConsulta(idMedico, idPaciente, data, especialidade);
    }

}