package com.lazarux.ventas.seguridad.service;

import com.lazarux.ventas.seguridad.dto.AuthUserDto;
import com.lazarux.ventas.seguridad.dto.TokenDto;
import com.lazarux.ventas.seguridad.entity.AuthUser;
import com.lazarux.ventas.seguridad.repository.AuthUserRepository;
import com.lazarux.ventas.seguridad.security.JwtProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthUserRepository authUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private AuthService authService;

    // ===================== save() =====================

    @Test
    void save_cuandoUsuarioNuevo_debeGuardarYRetornarUsuario() {
        AuthUserDto dto = AuthUserDto.builder()
                .userName("testuser").password("pass123").role("ROLE_USER").build();
        AuthUser usuarioGuardado = AuthUser.builder()
                .id(1L).userName("testuser").password("hashed_pass").role("ROLE_USER").build();

        when(authUserRepository.findByUserName("testuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pass123")).thenReturn("hashed_pass");
        when(authUserRepository.save(any(AuthUser.class))).thenReturn(usuarioGuardado);

        AuthUser resultado = authService.save(dto);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getUserName()).isEqualTo("testuser");
        assertThat(resultado.getPassword()).isEqualTo("hashed_pass");
        assertThat(resultado.getRole()).isEqualTo("ROLE_USER");
        verify(authUserRepository).save(any(AuthUser.class));
    }

    @Test
    void save_cuandoRoleEsNull_debeAsignarRoleUserPorDefecto() {
        AuthUserDto dto = AuthUserDto.builder()
                .userName("newuser").password("pass123").role(null).build();
        AuthUser usuarioGuardado = AuthUser.builder()
                .id(2L).userName("newuser").password("hashed").role("ROLE_USER").build();

        when(authUserRepository.findByUserName("newuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("hashed");
        when(authUserRepository.save(any(AuthUser.class))).thenReturn(usuarioGuardado);

        AuthUser resultado = authService.save(dto);

        assertThat(resultado.getRole()).isEqualTo("ROLE_USER");
    }

    @Test
    void save_cuandoUsuarioYaExiste_debeLanzarExcepcion() {
        AuthUserDto dto = AuthUserDto.builder()
                .userName("existente").password("pass").role("ROLE_USER").build();
        AuthUser existente = AuthUser.builder().id(1L).userName("existente").build();

        when(authUserRepository.findByUserName("existente")).thenReturn(Optional.of(existente));

        assertThatThrownBy(() -> authService.save(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("ya existe");

        verify(authUserRepository, never()).save(any());
    }

    // ===================== login() =====================

    @Test
    void login_conCredencialesCorrectas_debeRetornarToken() {
        AuthUserDto dto = AuthUserDto.builder()
                .userName("testuser").password("pass123").build();
        AuthUser authUser = AuthUser.builder()
                .id(1L).userName("testuser").password("hashed_pass").role("ROLE_USER").build();

        when(authUserRepository.findByUserName("testuser")).thenReturn(Optional.of(authUser));
        when(passwordEncoder.matches("pass123", "hashed_pass")).thenReturn(true);
        when(jwtProvider.createToken(authUser)).thenReturn("jwt.token.aqui");

        TokenDto resultado = authService.login(dto);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getToken()).isEqualTo("jwt.token.aqui");
    }

    @Test
    void login_cuandoUsuarioNoExiste_debeLanzarExcepcion() {
        AuthUserDto dto = AuthUserDto.builder()
                .userName("noexiste").password("pass").build();

        when(authUserRepository.findByUserName("noexiste")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("no encontrado");
    }

    @Test
    void login_conPasswordIncorrecta_debeLanzarExcepcion() {
        AuthUserDto dto = AuthUserDto.builder()
                .userName("testuser").password("wrongpass").build();
        AuthUser authUser = AuthUser.builder()
                .id(1L).userName("testuser").password("hashed_pass").role("ROLE_USER").build();

        when(authUserRepository.findByUserName("testuser")).thenReturn(Optional.of(authUser));
        when(passwordEncoder.matches("wrongpass", "hashed_pass")).thenReturn(false);

        assertThatThrownBy(() -> authService.login(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Contraseña incorrecta");
    }
}
