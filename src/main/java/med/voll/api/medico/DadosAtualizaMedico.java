package med.voll.api.medico;

import med.voll.api.endereco.DadosEndereco;

public record DadosAtualizaMedico(
        Long id,
        String nome,
        String telefone,
        DadosEndereco endereco) {


}
