package com.example.reservas.controller;

import com.example.reservas.dto.LoginRequestDTO;
import com.example.reservas.dto.LoginResponseDTO;
import com.example.reservas.security.Usuario;
import com.example.reservas.security.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final com.example.reservas.security.JwtService jwtService;
    private final UsuarioRepository usuarioRepo;

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody @Valid LoginRequestDTO dto) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.username(), dto.password()));

        UserDetails user = (UserDetails) auth.getPrincipal();
        String token = jwtService.generarToken(user);

        Usuario u = usuarioRepo.findByUsername(user.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"));

        String rol = user.getAuthorities().stream()
                .findFirst().map(GrantedAuthority::getAuthority)
                .orElse("ROLE_USER").replace("ROLE_", "");

        return new LoginResponseDTO(token, "Bearer", expirationMs, u.getUsername(), rol);
    }
}