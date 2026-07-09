package com.kyros.demokyros.restcontrollers;

import com.kyros.demokyros.dto.UsuarioDto;
import com.kyros.demokyros.form.ResetPasswordForm;
import com.kyros.demokyros.form.UsuarioForm;
import com.kyros.demokyros.services.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;

    @GetMapping
    public List<UsuarioDto> getAll() {
        return service.getAllUsuarios();
    }

    @GetMapping("/{id}")
    public UsuarioDto getById(@PathVariable Integer id) {
        return service.getUsuarioById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioDto create(@Valid @RequestBody UsuarioForm form) {
        return service.createUsuario(form);
    }

    @PutMapping("/{id}")
    public UsuarioDto update(@PathVariable Integer id, @Valid @RequestBody UsuarioForm form) {
        return service.updateUsuario(id, form);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.deleteUsuario(id);
    }

    // único camino para cambiar la contraseña; nunca se expone un GET que la muestre
    @PutMapping("/{id}/password")
    public UsuarioDto resetPassword(@PathVariable Integer id, @Valid @RequestBody ResetPasswordForm form) {
        return service.resetPassword(id, form);
    }
}
