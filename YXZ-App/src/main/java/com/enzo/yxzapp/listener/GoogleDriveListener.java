package com.enzo.yxzapp.listener;

import com.enzo.yxzapp.event.NovaOficinaCriadaEvent;
import com.enzo.yxzapp.model.Oficina;
import com.enzo.yxzapp.repository.OficinaRepository;
import com.enzo.yxzapp.service.GoogleDriveService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class GoogleDriveListener {
    private final GoogleDriveService googleDriveService;
    private final OficinaRepository oficinaRepository;

    public GoogleDriveListener(GoogleDriveService googleDriveService, OficinaRepository oficinaRepository) {
        this.googleDriveService = googleDriveService;
        this.oficinaRepository = oficinaRepository;
    }

    @Async // Isto faz a mágica de rodar em segundo plano!
    @EventListener
    public void handleNovaOficina(NovaOficinaCriadaEvent event) {
        try {
            // 1. Vai no Google e cria a pasta
            String pastaId = googleDriveService.criarPastaDaOficina(event.nomePasta());

            // 2. Salva o ID da pasta no banco de dados
            Oficina oficina = oficinaRepository.findById(event.oficinaId()).orElseThrow();
            oficina.setPastaDriveId(pastaId);
            oficinaRepository.save(oficina);

        } catch (Exception e) {
            System.err.println("❌ Erro ao criar pasta no Google Drive: " + e.getMessage());
        }
    }
}
