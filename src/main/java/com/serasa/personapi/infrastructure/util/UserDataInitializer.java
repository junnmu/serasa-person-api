package com.serasa.personapi.infrastructure.util;

import com.serasa.personapi.domain.auth.Role;
import com.serasa.personapi.domain.auth.User;
import com.serasa.personapi.infrastructure.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class UserDataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void createUsers() {
        List<User> users = List.of(
            new User(1L, "admin", passwordEncoder.encode("admin"), Set.of(Role.ROLE_ADMIN)),
            new User(2L, "marcelo", passwordEncoder.encode("marcelo123"), Set.of(Role.ROLE_USER))
        );
        userRepository.saveAll(users);
    }
}
