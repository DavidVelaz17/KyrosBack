package com.kyros.demokyros.restcontrollers;

import com.kyros.demokyros.dto.CursoVeranoDto;
import com.kyros.demokyros.form.CursoVeranoForm;
import com.kyros.demokyros.services.CursoVeranoService;
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
@RequestMapping("/api/cursos-verano")
@RequiredArgsConstructor
public class CursoVeranoController {

    private final CursoVeranoService service;

    @GetMapping
    public List<CursoVeranoDto> getAll() {
        return service.getAllCursosVerano();
    }

    @GetMapping("/{id}")
    public CursoVeranoDto getById(@PathVariable Integer id) {
        return service.getCursoVeranoById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CursoVeranoDto create(@Valid @RequestBody CursoVeranoForm form) {
        return service.createCursoVerano(form);
    }

    @PutMapping("/{id}")
    public CursoVeranoDto update(@PathVariable Integer id, @Valid @RequestBody CursoVeranoForm form) {
        return service.updateCursoVerano(id, form);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.deleteCursoVerano(id);
    }
}
