package com.enzo.yxzapp.model;

import com.enzo.yxzapp.enums.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "oficinas")
@Getter
@Setter
public class Oficina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String escola;

    @Column(nullable = false)
    private String cidade;

    @Column(nullable = false)
    private LocalDate data;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoOficina tipo;

    @Column(name = "contato_escola", nullable = false)
    private String contatoEscola;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Segmento segmento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Turno turno;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Turma turma;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusOficina status = StatusOficina.AGENDADA;

    @Column(name = "criador_nome", nullable = false)
    private String criadorNome;

    @Enumerated(EnumType.STRING)
    @Column(name = "cor_criador", nullable = false)
    private CorAdministradora corCriador;

    @Column(name = "criador_id", nullable = false)
    private Long criadorId;

    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "ultimo_atualizador_nome")
    private String ultimoAtualizadorNome;

    @Column(name = "ultimo_atualizador_id")
    private Long ultimoAtualizadorId;

    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @ElementCollection
    @CollectionTable(name = "oficina_instrutores", joinColumns = @JoinColumn(name = "oficina_id"))
    @Column(name = "instrutor")
    private List<String> instrutores = new ArrayList<>();

    @Column(name = "avaliacao_escola")
    private Integer avaliacaoEscola;

    @Column(name = "quantitativo_aluno")
    private Integer quantitativoAluno;

    @Column(name = "acompanhante_turma")
    private String acompanhanteTurma;

    @Column(name = "Motivo_Cancelamento")
    private String motivoCancelamento;

    @Column(name = "pasta_drive_id")
    private String pastaDriveId;

    @Column(name = "link_pasta_drive")
    private String linkPastaDrive;
}