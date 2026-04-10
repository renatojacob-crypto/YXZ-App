package com.enzo.yxzapp.repository;

import com.enzo.yxzapp.enums.CorAdministradora;
import com.enzo.yxzapp.enums.StatusOficina;
import com.enzo.yxzapp.enums.TipoOficina;
import com.enzo.yxzapp.model.Oficina;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OficinaRepository extends JpaRepository<Oficina, Long> {

    // ==================== FIND ALL ====================

    @Override
    @EntityGraph(attributePaths = {"instrutores"})
    Page<Oficina> findAll(Pageable pageable);

    // ==================== FILTROS ====================

    @EntityGraph(attributePaths = {"instrutores"})
    Page<Oficina> findByTipo(TipoOficina tipo, Pageable pageable);

    @EntityGraph(attributePaths = {"instrutores"})
    @Query("SELECT o FROM Oficina o WHERE LOWER(o.cidade) LIKE LOWER(CONCAT('%', :cidade, '%'))")
    Page<Oficina> findByCidadeContainingIgnoreCase(
            @Param("cidade") String cidade,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"instrutores"})
    Page<Oficina> findByDataBetween(
            LocalDate dataInicio,
            LocalDate dataFim,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"instrutores"})
    Page<Oficina> findByData(LocalDate data, Pageable pageable);

    @EntityGraph(attributePaths = {"instrutores"})
    @Query("SELECT o FROM Oficina o WHERE " +
            "(:tipo IS NULL OR o.tipo = :tipo) AND " +
            "(CAST(:cidade AS String) IS NULL OR LOWER(o.cidade) LIKE LOWER(CONCAT('%', CAST(:cidade AS String), '%'))) AND " +
            "(CAST(:dataInicio AS date) IS NULL OR o.data >= :dataInicio) AND " +
            "(CAST(:dataFim AS date) IS NULL OR o.data <= :dataFim)")
    Page<Oficina> findByFiltros(
            @Param("tipo") TipoOficina tipo,
            @Param("cidade") String cidade,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"instrutores"})
    Page<Oficina> findByStatus(StatusOficina status, Pageable pageable);

    @EntityGraph(attributePaths = {"instrutores"})
    Page<Oficina> findByCorCriador(CorAdministradora cor, Pageable pageable);

    // ==================== CALENDÁRIO ====================

    @EntityGraph(attributePaths = {"instrutores"})
    @Query("SELECT o FROM Oficina o WHERE YEAR(o.data) = :ano AND MONTH(o.data) = :mes")
    List<Oficina> findByMes(
            @Param("ano") int ano,
            @Param("mes") int mes
    );

    @Modifying
    @Query("UPDATE Oficina o SET o.criadorNome = :novoNome WHERE o.criadorId = :usuarioId")
    void atualizarNomeCriador(@Param("usuarioId") Long usuarioId, @Param("novoNome") String novoNome);

    @Modifying
    @Query("UPDATE Oficina o SET o.ultimoAtualizadorNome = :novoNome WHERE o.ultimoAtualizadorId = :usuarioId")
    void atualizarNomeAtualizador(@Param("usuarioId") Long usuarioId, @Param("novoNome") String novoNome);
}