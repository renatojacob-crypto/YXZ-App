package com.enzo.yxzapp.dto.oficina;

import com.enzo.yxzapp.enums.*;
import com.enzo.yxzapp.model.Oficina;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record OficinaResponse(
        Long id,
        String escola,
        String cidade,
        LocalDate data,
        TipoOficina tipo,
        String contatoEscola,
        Segmento segmento,
        Turno turno,
        Turma turma,
        StatusOficina status,
        List<String> instrutores,
        Integer avaliacaoEscola,
        Integer quantitativoAluno,
        String acompanhanteTurma,
        String criadorNome,              // ← Campo direto
        CorAdministradora corCriador,    // ← Campo direto
        LocalDateTime dataCriacao,
        String ultimoAtualizadorNome,    // ← Campo direto
        LocalDateTime dataAtualizacao,
        String motivoCancelamento,
        String linkPastaDrive
) {
    public static OficinaResponse fromEntity(Oficina o) {
        return new OficinaResponse(
                o.getId(),
                o.getEscola(),
                o.getCidade(),
                o.getData(),
                o.getTipo(),
                o.getContatoEscola(),
                o.getSegmento(),
                o.getTurno(),
                o.getTurma(),
                o.getStatus(),
                o.getInstrutores(),
                o.getAvaliacaoEscola(),
                o.getQuantitativoAluno(),
                o.getAcompanhanteTurma(),
                o.getCriadorNome(),           // ← Simples!
                o.getCorCriador(),            // ← Simples!
                o.getDataCriacao(),
                o.getUltimoAtualizadorNome(), // ← Simples!
                o.getDataAtualizacao(),
                o.getMotivoCancelamento(),
                o.getLinkPastaDrive()
        );
    }
}