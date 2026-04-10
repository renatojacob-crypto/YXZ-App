package com.enzo.yxzapp.config;

import com.enzo.yxzapp.enums.Role;
import com.enzo.yxzapp.model.User;
import com.enzo.yxzapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class RootSeedRunner {

    @Bean
    public ApplicationRunner seedRoot(
            UserRepository userRepository,
            BCryptPasswordEncoder encoder,
            @Value("${root.email}") String email,
            @Value("${root.password}") String password,
            @Value("${root.name}") String name
    ) {
        return args -> {
            if (userRepository.existsByEmail(email)) return;

            User root = new User();
            root.setNome(name);
            root.setEmail(email);
            root.setSenha(encoder.encode(password));
            root.setRole(Role.ROOT);
            root.setAtivo(true);
            root.setCorAdministradora(null);

            userRepository.save(root);
        };
    }
}
