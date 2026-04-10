package com.enzo.yxzapp.dto.oficina;

import com.enzo.yxzapp.enums.Segmento;
import com.enzo.yxzapp.enums.TipoOficina;
import com.enzo.yxzapp.enums.Turma;
import com.enzo.yxzapp.enums.Turno;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateOficinaRequest(
        @NotBlank String escola,
        @NotBlank String cidade,
        @NotNull LocalDate data,
        @NotNull TipoOficina tipo,
        @NotBlank String contatoEscola,
        @NotNull Segmento segmento,
        @NotNull Turno turno,
        @NotNull Turma turma
) {}