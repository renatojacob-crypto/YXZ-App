package com.enzo.yxzapp.dto.oficina;

import com.enzo.yxzapp.enums.StatusOficina;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.List;

public record UpdateViaModalRequest(
        StatusOficina status,

        List<String> instrutores,

        @Min(value = 1, message = "Avaliação mínima é 1")
        @Max(value = 10, message = "Avaliação máxima é 10")
        Integer avaliacaoEscola,

        @Min(value = 0, message = "Quantitativo não pode ser negativo")
        Integer quantitativoAluno,

        String acompanhanteTurma,

        String motivoCancelamento
) {}