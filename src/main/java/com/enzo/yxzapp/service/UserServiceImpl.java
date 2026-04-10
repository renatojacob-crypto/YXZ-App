package com.enzo.yxzapp.service;

import com.enzo.yxzapp.dto.common.PageResponse;
import com.enzo.yxzapp.dto.user.CreateUserRequest;
import com.enzo.yxzapp.dto.user.UpdateUserRequest;
import com.enzo.yxzapp.dto.user.UserResponse;
import com.enzo.yxzapp.enums.CorAdministradora;
import com.enzo.yxzapp.enums.Role;
import com.enzo.yxzapp.event.UsuarioMudouNomeEvent;
import com.enzo.yxzapp.exception.BadRequestException;
import com.enzo.yxzapp.exception.NotFoundException;
import com.enzo.yxzapp.model.User;
import com.enzo.yxzapp.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final ApplicationEventPublisher eventPublisher;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder encoder, ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public UserResponse create(CreateUserRequest req) {
        if (userRepository.existsByEmail(normalizeEmail(req.email()))) {
            throw new BadRequestException("Usuário já cadastrado");
        }

        if (req.role() == Role.ROOT) {
            throw new BadRequestException("Não é permitido criar outro usuário ROOT");
        }

        if (req.role() == Role.ADMIN && req.corAdministradora() == null) {
            throw new BadRequestException("ADMIN precisa ter uma corAdministradora");
        }

        if (req.role() != Role.ADMIN && req.corAdministradora() != null) {
            throw new BadRequestException("Somente ADMIN pode ter corAdministradora");
        }

        User user = new User();
        user.setNome(normalizeName(req.nome()));
        user.setEmail(normalizeEmail(req.email()));
        user.setSenha(encoder.encode(req.senha()));
        user.setRole(req.role());
        user.setAtivo(true);
        user.setCorAdministradora(req.role() == Role.ADMIN ? req.corAdministradora() : null);

        User saved = userRepository.save(user);
        return UserResponse.fromEntity(saved);
    }

    @Override
    @Transactional
    public UserResponse update(Long id, UpdateUserRequest req) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        boolean isRootUser = user.getRole() == Role.ROOT;
        boolean nomeMudou = false;

        // 1. NOME (PATCH parcial com verificação de mudança)
        if (req.nome() != null && !req.nome().isBlank()) {
            String novoNome = normalizeName(req.nome()); // Usa o seu metodo de normalizar

            // Verifica se o nome realmente mudou
            if (!novoNome.equals(user.getNome())) {
                user.setNome(novoNome);
                nomeMudou = true; // Marca que mudou!
            }
        }

        // 2. ATIVO (PATCH parcial)
        if (req.ativo() != null) {
            if (isRootUser && !req.ativo()) {
                throw new BadRequestException("Não é permitido desativar o usuário ROOT");
            }
            user.setAtivo(req.ativo());
        }

        // 3. ROLE/COR (PATCH parcial) — valida conjunto
        Role newRole = (req.role() != null) ? req.role() : user.getRole();

        // Bloqueios envolvendo ROOT
        if (newRole == Role.ROOT && !isRootUser) {
            throw new BadRequestException("Não é permitido promover usuário para ROOT");
        }
        if (isRootUser && newRole != Role.ROOT) {
            throw new BadRequestException("Não é permitido alterar o role do usuário ROOT");
        }

        CorAdministradora newColor;
        if (req.role() != null || req.corAdministradora() != null) {
            newColor = req.corAdministradora();
        } else {
            newColor = user.getCorAdministradora();
        }

        if (newRole == Role.ADMIN && newColor == null) {
            throw new BadRequestException("ADMIN precisa ter uma corAdministradora");
        }
        if (newRole != Role.ADMIN) {
            newColor = null; // garante consistência
        }

        user.setRole(newRole);
        user.setCorAdministradora(newColor);

        // 4. SALVAR O USUÁRIO
        User updated = userRepository.save(user);

        // 5. ATUALIZAR AS OFICINAS SE O NOME MUDOU
        if (nomeMudou) {
            eventPublisher.publishEvent(new UsuarioMudouNomeEvent(user.getId(), updated.getNome()));
        }

        return UserResponse.fromEntity(updated);
    }

    @Override
    @Transactional
    public PageResponse<UserResponse> list(Pageable pageable) {
        Page<User> page = userRepository.findAll(pageable);
        return PageResponse.fromPage(page, UserResponse::fromEntity);
    }

    @Override
    public UserResponse getById(Long id) {
        return userRepository.findById(id)
                .map(UserResponse::fromEntity)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
    }

    private String normalizeEmail(String email) {
        if (email == null) return null;
        return email.trim().toLowerCase();
    }

    private String normalizeName(String nome) {
        if (nome == null) return null;
        return nome.trim().replaceAll("\\s+", " ");
    }
}

