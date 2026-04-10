package com.lazarux.ventas.seguridad.controller;

import com.lazarux.ventas.seguridad.dto.AuthUserDto;
import com.lazarux.ventas.seguridad.dto.TokenDto;
import com.lazarux.ventas.seguridad.entity.AuthUser;
import com.lazarux.ventas.seguridad.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody AuthUserDto dto){
        TokenDto tokenDto = authService.login(dto);
        if(tokenDto == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/create")
    public ResponseEntity<AuthUser> create(@RequestBody AuthUserDto dto){
        try {
            AuthUser authUser = authService.save(dto);
            return ResponseEntity.ok(authUser);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().build();
        }
    }
}
