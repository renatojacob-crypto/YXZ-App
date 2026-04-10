package com.enzo.yxzapp.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;

@Service
public class GoogleDriveService {
    @Value("${google.drive.folder.id}")
    private String pastaMaeId;

    public String getPastaMaeId() {
        return pastaMaeId;
    }
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    public Drive getDriveService() throws IOException {
        // 1. Carrega o novo credentials.json (OAuth)
        InputStream in = new ClassPathResource("credentials.json").getInputStream();
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                GsonFactory.getDefaultInstance(), new InputStreamReader(in)
        );

        // 2. Configura o fluxo solicitando acesso total ao Drive
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                new NetHttpTransport(), GsonFactory.getDefaultInstance(), clientSecrets, Collections.singletonList(DriveScopes.DRIVE))
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline") // Garante que o token se renove sozinho
                .build();

        // 3. Abre uma porta local temporária para receber a resposta do Google
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

        // 4. Executa a autorização
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

        // 5. Constrói o serviço com as SUAS credenciais
        return new Drive.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                credential
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
                .setSupportsAllDrives(true)
                .setIgnoreDefaultVisibility(true) // Adicione esta linha
                .setFields("id, webViewLink")
                .execute();


        // Retorna o link para visualizar a foto no navegador
        return fotoEnviada.getWebViewLink();
    }
}