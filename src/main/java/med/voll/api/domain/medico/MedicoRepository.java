package med.voll.api.domain.medico;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface MedicoRepository extends JpaRepository<Medico, Long> {

    Page<Medico> findAllByAtivoTrue(Pageable paginacao);

    @Query(value = """
        SELECT * FROM medicos m
        WHERE m.ativo = true AND m.especialidade = :especialidade AND m.id NOT IN (
                SELECT c.medico_id FROM consultas c
                WHERE c.data = :data
            )
        ORDER BY RAND()
        LIMIT 1
    """, nativeQuery = true
    )
    Medico escolherMedicoAleatorioLivreNaData(Especialidade especialidade, LocalDateTime data);


    //@Query(value = "select m.ativo from Medicos m where m.id = :id", nativeQuery = true)
    @Query(value = """
            select m from Medico m
            where m.ativo = true and m.especialidade = :especialidade
            and m.id not in(
                select c.medico.id from Consulta c
                where
                c.data = :data and c.motivoCancelamento is null
            )
            order by rand()
            limit 1
            """, nativeQuery = true)
    Boolean findAtivoById(Long id);
}
