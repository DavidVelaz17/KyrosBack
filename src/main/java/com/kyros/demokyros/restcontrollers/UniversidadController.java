package com.kyros.demokyros.restcontrollers;

import com.kyros.demokyros.dto.UniversidadDto;
import com.kyros.demokyros.form.UniversidadForm;
import com.kyros.demokyros.services.UniversidadService;
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
@RequestMapping("/api/universidades")
@RequiredArgsConstructor
public class UniversidadController {

    private final UniversidadService service;

    @GetMapping
    public List<UniversidadDto> getAll() {
        return service.getAllUniversidades();
    }

    @GetMapping("/{id}")
    public UniversidadDto getById(@PathVariable Integer id) {
        return service.getUniversidadById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UniversidadDto create(@Valid @RequestBody UniversidadForm form) {
        return service.createUniversidad(form);
    }

    @PutMapping("/{id}")
    public UniversidadDto update(@PathVariable Integer id, @Valid @RequestBody UniversidadForm form) {
        return service.updateUniversidad(id, form);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.deleteUniversidad(id);
    }
}
