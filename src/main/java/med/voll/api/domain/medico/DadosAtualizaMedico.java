package med.voll.api.domain.medico;

import med.voll.api.domain.endereco.DadosEndereco;

public record DadosAtualizaMedico(
        Long id,
        String nome,
        String telefone,
        DadosEndereco endereco) {


}
