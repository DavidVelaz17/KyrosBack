package com.kyros.demokyros.restcontrollers;

import com.kyros.demokyros.dto.SecundariaDto;
import com.kyros.demokyros.form.SecundariaForm;
import com.kyros.demokyros.services.SecundariaService;
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
@RequestMapping("/api/secundarias")
@RequiredArgsConstructor
public class SecundariaController {

    private final SecundariaService service;

    @GetMapping
    public List<SecundariaDto> getAll() {
        return service.getAllSecundarias();
    }

    @GetMapping("/{id}")
    public SecundariaDto getById(@PathVariable Integer id) {
        return service.getSecundariaById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SecundariaDto create(@Valid @RequestBody SecundariaForm form) {
        return service.createSecundaria(form);
    }

    @PutMapping("/{id}")
    public SecundariaDto update(@PathVariable Integer id, @Valid @RequestBody SecundariaForm form) {
        return service.updateSecundaria(id, form);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.deleteSecundaria(id);
    }
}
