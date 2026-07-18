package com.kyros.demokyros.services;

import com.kyros.demokyros.entity.Log;
import com.kyros.demokyros.entity.Usuario;
import com.kyros.demokyros.exception.InvalidCredentialsException;
import com.kyros.demokyros.jwt.JwtService;
import com.kyros.demokyros.repository.LogRepository;
import com.kyros.demokyros.repository.UsuarioRepository;
import com.kyros.demokyros.requests.LoginRequest;
import com.kyros.demokyros.responses.JwtResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final LogRepository logRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public JwtResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByUsuario(request.getUsuario())
                .orElseThrow(() -> new InvalidCredentialsException("Usuario o contraseña incorrectos"));
        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new InvalidCredentialsException("Usuario o contraseña incorrectos");
        }
        logRepository.save(Log.builder()
                .timeStamp(LocalDateTime.now())
                .idUsuario(usuario.getIdUsuario())
                .build());
        return JwtResponse.builder()
                .token(jwtService.generateToken(usuario))
                .idUsuario(usuario.getIdUsuario())
                .nombreUsuario(usuario.getNombreUsuario())
                .usuario(usuario.getUsuario())
                .rol(usuario.getRol())
                .build();
    }
}
