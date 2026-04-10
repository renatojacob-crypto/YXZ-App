package com.enzo.yxzapp.auth;

import com.enzo.yxzapp.dto.auth.LoginRequest;
import com.enzo.yxzapp.dto.auth.LoginResponse;
import com.enzo.yxzapp.model.User;
import com.enzo.yxzapp.repository.UserRepository;
import com.enzo.yxzapp.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public LoginResponse login(LoginRequest req) {
       Authentication authentication = authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(req.email(), req.password())
       );
        User user = userRepository.findByEmail(req.email()).orElseThrow(() -> new BadCredentialsException("Credencial Invalida"));

        String token = jwtService.generateToken(
                user.getEmail(), Map.of("role", user.getRole().name(), "userId", user.getId())
        );
        return new LoginResponse(
                token,
                user.getId(),
                user.getNome(),
                user.getRole(),
                user.getCorAdministradora() // <-- Adicionado
        );
    }
}
