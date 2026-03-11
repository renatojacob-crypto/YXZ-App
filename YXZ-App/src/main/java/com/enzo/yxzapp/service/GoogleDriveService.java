package com.enzo.yxzapp.service;

import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Collections;

@Service
public class GoogleDriveService {
    @Value("${google.drive.folder.id}")
    private String pastaMaeId;

    public String getPastaMaeId() {
        return pastaMaeId;
    }

    public Drive getDriveService() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(
                new ClassPathResource("credentials.json").getInputStream()
        ).createScoped(Collections.singletonList(DriveScopes.DRIVE_FILE));

        return new Drive.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials)
        ).setApplicationName("YXZ-App").build();
    }

    public String criarPastaDaOficina(String nomePasta) throws IOException {
        Drive driveService = getDriveService();

        File fileMetadata = new File();
        fileMetadata.setName(nomePasta);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        fileMetadata.setParents(Collections.singletonList(pastaMaeId)); // Coloca dentro da pasta principal

        File pastaCriada = driveService.files().create(fileMetadata)
                .setSupportsAllDrives(true)
                .setFields("id, webViewLink")
                .execute();

        return pastaCriada.getId();
    }

    // Mantido caso você precise fazer uploads síncronos no futuro
    public String uploadFoto(MultipartFile arquivo, String pastaOficinaId) throws IOException {
        Drive driveService = getDriveService();

        File fileMetadata = new File();
        fileMetadata.setName(arquivo.getOriginalFilename());
        fileMetadata.setParents(Collections.singletonList(pastaOficinaId)); // Coloca na pasta certa

        InputStreamContent mediaContent = new InputStreamContent(
                arquivo.getContentType(),
                arquivo.getInputStream()
        );

        File fotoEnviada = driveService.files().create(fileMetadata, mediaContent)
                .setFields("id, webViewLink")
                .execute();

        // Retorna o link para visualizar a foto no navegador
        return fotoEnviada.getWebViewLink();
    }
}