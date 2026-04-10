package com.enzo.yxzapp.controller;

import com.enzo.yxzapp.dto.common.PageResponse;
import com.enzo.yxzapp.dto.oficina.CreateOficinaRequest;
import com.enzo.yxzapp.dto.oficina.OficinaResponse;
import com.enzo.yxzapp.dto.oficina.UpdateViaModalRequest;
import com.enzo.yxzapp.enums.CorAdministradora;
import com.enzo.yxzapp.enums.StatusOficina;
import com.enzo.yxzapp.enums.TipoOficina;
import com.enzo.yxzapp.exception.BadRequestException;
import com.enzo.yxzapp.service.OficinaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/oficinas")
public class OficinaController {

    private final OficinaService oficinaService;

    public OficinaController(OficinaService oficinaService) {
        this.oficinaService = oficinaService;
    }

    // ==================== LISTAGEM ====================

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public PageResponse<OficinaResponse> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "data") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        return oficinaService.list(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public OficinaResponse getById(@PathVariable Long id) {
        return oficinaService.getById(id);
    }

    // ==================== CRUD ====================

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OficinaResponse> create(@RequestBody @Valid CreateOficinaRequest request) {
        OficinaResponse response = oficinaService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public OficinaResponse update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateViaModalRequest request) {
        return oficinaService.update(id, request);
    }

    // ==================== FILTROS ====================

    @GetMapping("/filtrar")
    @PreAuthorize("isAuthenticated()")
    public PageResponse<OficinaResponse> filtrar(
            @RequestParam(required = false) TipoOficina tipo,
            @RequestParam(required = false) String cidade,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "data") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        return oficinaService.filtrarCombinado(tipo, cidade, dataInicio, dataFim, pageable);
    }

    @GetMapping("/filtrar/tipo")
    @PreAuthorize("isAuthenticated()")
    public PageResponse<OficinaResponse> filtrarPorTipo(
            @RequestParam TipoOficina tipo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "data"));
        return oficinaService.filtrarPorTipo(tipo, pageable);
    }

    @GetMapping("/filtrar/cidade")
    @PreAuthorize("isAuthenticated()")
    public PageResponse<OficinaResponse> filtrarPorCidade(
            @RequestParam String cidade,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "data"));
        return oficinaService.filtrarPorCidade(cidade, pageable);
    }

    @GetMapping("/filtrar/periodo")
    @PreAuthorize("isAuthenticated()")
    public PageResponse<OficinaResponse> filtrarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "data"));
        return oficinaService.filtrarPorPeriodo(dataInicio, dataFim, pageable);
    }

    @GetMapping("/filtrar/data")
    @PreAuthorize("isAuthenticated()")
    public PageResponse<OficinaResponse> filtrarPorData(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "data"));
        return oficinaService.filtrarPorData(data, pageable);
    }

    @GetMapping("/filtrar/mercadologico")
    @PreAuthorize("isAuthenticated()")
    public PageResponse<OficinaResponse> filtrarPorPeriodoMercadologico(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "data"));
        return oficinaService.filtrarPorPeriodoMercadologico(pageable);
    }

    @GetMapping("/filtrar/mensal")
    @PreAuthorize("isAuthenticated()")
    public PageResponse<OficinaResponse> filtrarPorPeriodoMensal(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "data"));
        return oficinaService.filtrarPorPeriodoMensal(pageable);
    }

    @GetMapping("/filtrar/status")
    @PreAuthorize("isAuthenticated()")
    public PageResponse<OficinaResponse> filtrarPorStatus(
            @RequestParam StatusOficina status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "data"));
        return oficinaService.filtrarPorStatus(status, pageable);
    }

    @GetMapping("/filtrar/cor")
    @PreAuthorize("hasRole('ADMIN')")  // ← Só ADMIN vê filtro por cor
    public PageResponse<OficinaResponse> filtrarPorCor(
            @RequestParam CorAdministradora cor,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "data"));
        return oficinaService.filtrarPorCor(cor, pageable);
    }

    // ==================== CALENDÁRIO ====================

    @GetMapping("/calendario")
    @PreAuthorize("isAuthenticated()")
    public List<OficinaResponse> buscarPorMes(
            @RequestParam int ano,
            @RequestParam int mes) {

        if (mes < 1 || mes > 12) {
            throw new BadRequestException("Mês deve ser entre 1 e 12");
        }

        return oficinaService.buscarPorMes(ano, mes);
    }

    // ==================== FOTOS ====================
    @PostMapping(value = "/{id}/fotos", consumes = "multipart/form-data")
    public ResponseEntity<Void> uploadFotos(@PathVariable Long id, @RequestParam("fotos") List<MultipartFile> fotos) {
        oficinaService.receberFotos(id, fotos);
        return ResponseEntity.accepted().build(); // Retorna 202 (Aceito para processamento) instantaneamente!
    }
}
