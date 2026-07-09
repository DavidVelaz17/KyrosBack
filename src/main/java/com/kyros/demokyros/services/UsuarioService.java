package com.kyros.demokyros.services;

import com.kyros.demokyros.dto.UsuarioDto;
import com.kyros.demokyros.entity.Usuario;
import com.kyros.demokyros.exception.ResourceNotFoundException;
import com.kyros.demokyros.form.ResetPasswordForm;
import com.kyros.demokyros.form.UsuarioForm;
import com.kyros.demokyros.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public List<UsuarioDto> getAllUsuarios() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public UsuarioDto getUsuarioById(Integer id) {
        return toDto(findEntity(id));
    }

    public UsuarioDto createUsuario(UsuarioForm form) {
        ensureUsuarioDisponible(form.getUsuario(), null);
        Usuario usuario = Usuario.builder()
                .nombreUsuario(form.getNombreUsuario())
                .usuario(form.getUsuario())
                .password(passwordEncoder.encode(form.getPassword()))
                .direccionUsuario(form.getDireccionUsuario())
                .rol(form.getRol())
                .build();
        return toDto(repository.save(usuario));
    }

    public UsuarioDto updateUsuario(Integer id, UsuarioForm form) {
        Usuario usuario = findEntity(id);
        ensureUsuarioDisponible(form.getUsuario(), id);
        usuario.setNombreUsuario(form.getNombreUsuario());
        usuario.setUsuario(form.getUsuario());
        usuario.setDireccionUsuario(form.getDireccionUsuario());
        usuario.setRol(form.getRol());
        return toDto(repository.save(usuario));
    }

    public void deleteUsuario(Integer id) {
        repository.delete(findEntity(id));
    }

    public UsuarioDto resetPassword(Integer id, ResetPasswordForm form) {
        Usuario usuario = findEntity(id);
        usuario.setPassword(passwordEncoder.encode(form.getNuevaPassword()));
        return toDto(repository.save(usuario));
    }

    private void ensureUsuarioDisponible(String usuarioLogin, Integer idUsuarioActual) {
        repository.findByUsuario(usuarioLogin).ifPresent(existing -> {
            if (!existing.getIdUsuario().equals(idUsuarioActual)) {
                throw new IllegalArgumentException("El usuario ya está en uso: " + usuarioLogin);
            }
        });
    }

    Usuario findEntity(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + id));
    }

    UsuarioDto toDto(Usuario usuario) {
        return UsuarioDto.builder()
                .idUsuario(usuario.getIdUsuario())
                .nombreUsuario(usuario.getNombreUsuario())
                .usuario(usuario.getUsuario())
                .direccionUsuario(usuario.getDireccionUsuario())
                .rol(usuario.getRol())
                .build();
    }
}
