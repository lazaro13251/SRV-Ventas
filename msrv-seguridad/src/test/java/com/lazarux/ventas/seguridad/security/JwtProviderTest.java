package com.lazarux.ventas.seguridad.security;

import com.lazarux.ventas.seguridad.entity.AuthUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class JwtProviderTest {

    private JwtProvider jwtProvider;

    // Secret en Base64 válido de al menos 256 bits para HS256
    private static final String SECRET = "aW52ZW50YWRvLXBvci1sYS1pbnRlbGlnZW5jaWEtYXJ0aWZpY2lhbC1xdWUtZGVtby1zZWNyZXRv";

    @BeforeEach
    void setUp() {
        jwtProvider = new JwtProvider();
        ReflectionTestUtils.setField(jwtProvider, "secret", SECRET);
    }

    @Test
    void createToken_debeGenerarUnTokenNoNulo() {
        AuthUser user = AuthUser.builder()
                .id(1L).userName("admin").password("hashed").role("ROLE_ADMIN").build();

        String token = jwtProvider.createToken(user);

        assertThat(token).isNotNull();
        assertThat(token).isNotBlank();
    }

    @Test
    void createToken_debeGenerarTokenConFormatoJwt() {
        AuthUser user = AuthUser.builder()
                .id(1L).userName("user1").password("hashed").role("ROLE_USER").build();

        String token = jwtProvider.createToken(user);

        // Un JWT tiene exactamente 3 partes separadas por puntos
        String[] partes = token.split("\\.");
        assertThat(partes).hasSize(3);
    }

    @Test
    void createToken_paraDistintosUsuarios_debeGenerarTokensDiferentes() {
        AuthUser user1 = AuthUser.builder().id(1L).userName("user1").password("h").role("ROLE_USER").build();
        AuthUser user2 = AuthUser.builder().id(2L).userName("user2").password("h").role("ROLE_ADMIN").build();

        String token1 = jwtProvider.createToken(user1);
        String token2 = jwtProvider.createToken(user2);

        assertThat(token1).isNotEqualTo(token2);
    }
}
