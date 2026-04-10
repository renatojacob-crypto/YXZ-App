package com.enzo.yxzapp.listener;

import com.enzo.yxzapp.event.ArquivoDriveDTO;
import com.enzo.yxzapp.event.FotosOficinaEnviadasEvent;
import com.enzo.yxzapp.model.Oficina;
import com.enzo.yxzapp.repository.OficinaRepository;
import com.enzo.yxzapp.service.GoogleDriveService;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;


@Component
public class GoogleDriveListener {

    private final GoogleDriveService googleDriveService;
    private final OficinaRepository oficinaRepository;

    public GoogleDriveListener(GoogleDriveService googleDriveService, OficinaRepository oficinaRepository) {
        this.googleDriveService = googleDriveService;
        this.oficinaRepository = oficinaRepository;
    }

    @Async
    @EventListener
    @Transactional // Para garantir que salva o link no banco de dados
    public void handleUploadFotos(FotosOficinaEnviadasEvent event) {
        try {
            Oficina oficina = oficinaRepository.findById(event.oficinaId()).orElseThrow();
            Drive drive = googleDriveService.getDriveService(); // Certifique-se de mudar getDriveService para 'public' no seu GoogleDriveService

            String pastaId = oficina.getPastaDriveId();

            // 1. SE A PASTA AINDA NÃO EXISTE, CRIA A PASTA E SALVA O LINK
            if (pastaId == null || pastaId.isBlank()) {
                File fileMetadata = new File();
                fileMetadata.setName(event.nomePasta());
                fileMetadata.setMimeType("application/vnd.google-apps.folder");
                fileMetadata.setParents(Collections.singletonList(googleDriveService.getPastaMaeId())); // Torne pastaMaeId acessível

                File pastaCriada = drive.files().create(fileMetadata)
                        .setFields("id, webViewLink")
                        .execute();

                pastaId = pastaCriada.getId();
                oficina.setPastaDriveId(pastaId);
                oficina.setLinkPastaDrive(pastaCriada.getWebViewLink()); // <--- SALVANDO O LINK AQUI!
                oficinaRepository.save(oficina);

                System.out.println("✅ Pasta criada: " + pastaCriada.getWebViewLink());
            }

            // 2. FAZ O UPLOAD DE CADA FOTO PARA DENTRO DA PASTA
            for (ArquivoDriveDTO foto : event.fotos()) {
                File fileMetadata = new File();
                fileMetadata.setName(foto.nomeOriginal());
                fileMetadata.setParents(Collections.singletonList(pastaId));

                ByteArrayContent mediaContent = new ByteArrayContent(foto.contentType(), foto.conteudo());

                drive.files().create(fileMetadata, mediaContent)
                        .setFields("id") // Não precisamos do link de cada foto, só da pasta
                        .execute();

                System.out.println("✅ Foto enviada: " + foto.nomeOriginal());
            }

        } catch (Exception e) {
            System.err.println("❌ Erro ao processar fotos no Drive: " + e.getMessage());
        }
    }
}