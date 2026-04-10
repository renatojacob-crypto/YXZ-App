package com.enzo.yxzapp.service;

import com.enzo.yxzapp.dto.common.PageResponse;
import com.enzo.yxzapp.dto.oficina.CreateOficinaRequest;
import com.enzo.yxzapp.dto.oficina.OficinaResponse;
import com.enzo.yxzapp.dto.oficina.UpdateViaModalRequest;
import com.enzo.yxzapp.enums.CorAdministradora;
import com.enzo.yxzapp.enums.StatusOficina;
import com.enzo.yxzapp.enums.TipoOficina;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;


public interface OficinaService {
    OficinaResponse create(CreateOficinaRequest req);
    OficinaResponse update(Long id, UpdateViaModalRequest req);
    PageResponse<OficinaResponse> list(Pageable pageable);
    OficinaResponse getById(Long id);

    // Listagem e Filtros
    PageResponse<OficinaResponse> filtrarPorTipo(TipoOficina tipo, Pageable pageable);
    PageResponse<OficinaResponse> filtrarPorCidade(String cidade, Pageable pageable);
    PageResponse<OficinaResponse> filtrarPorPeriodo(LocalDate dataInicio, LocalDate dataFim, Pageable pageable);
    PageResponse<OficinaResponse> filtrarPorData(LocalDate data, Pageable pageable);
    PageResponse<OficinaResponse> filtrarCombinado(TipoOficina tipo, String cidade,
                                                   LocalDate dataInicio, LocalDate dataFim,
                                                   Pageable pageable);
    PageResponse<OficinaResponse> filtrarPorPeriodoMercadologico(Pageable pageable);
    PageResponse<OficinaResponse> filtrarPorPeriodoMensal(Pageable pageable);
    PageResponse<OficinaResponse> filtrarPorStatus(StatusOficina status, Pageable pageable);
    PageResponse<OficinaResponse> filtrarPorCor(CorAdministradora cor, Pageable pageable);
    // Calendário
    List<OficinaResponse> buscarPorMes(int ano, int mes);
    void receberFotos(Long id, List<MultipartFile> fotos);
}
