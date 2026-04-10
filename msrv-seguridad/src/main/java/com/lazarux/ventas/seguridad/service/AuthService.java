package com.lazarux.ventas.seguridad.service;

import com.lazarux.ventas.seguridad.dto.AuthUserDto;
import com.lazarux.ventas.seguridad.dto.TokenDto;
import com.lazarux.ventas.seguridad.entity.AuthUser;
import com.lazarux.ventas.seguridad.repository.AuthUserRepository;
import com.lazarux.ventas.seguridad.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AuthUserRepository authUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    public AuthUser save(AuthUserDto dto) {
        Optional<AuthUser> existing = authUserRepository.findByUserName(dto.getUserName());
        if(existing.isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }
        String password = passwordEncoder.encode(dto.getPassword());
        AuthUser authUser = AuthUser.builder()
                .userName(dto.getUserName())
                .password(password)
                .role(dto.getRole() != null ? dto.getRole() : "ROLE_USER")
                .build();
        return authUserRepository.save(authUser);
    }

    public TokenDto login(AuthUserDto dto) {
        AuthUser authUser = authUserRepository.findByUserName(dto.getUserName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (!passwordEncoder.matches(dto.getPassword(), authUser.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }
        return new TokenDto(jwtProvider.createToken(authUser));
    }
}
