package com.kyros.demokyros.restcontrollers;

import com.kyros.demokyros.dto.MateriaDto;
import com.kyros.demokyros.form.MateriaForm;
import com.kyros.demokyros.services.MateriaService;
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
@RequestMapping("/api/materias")
@RequiredArgsConstructor
public class MateriaController {

    private final MateriaService service;

    @GetMapping
    public List<MateriaDto> getAll() {
        return service.getAllMaterias();
    }

    @GetMapping("/{id}")
    public MateriaDto getById(@PathVariable Integer id) {
        return service.getMateriaById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MateriaDto create(@Valid @RequestBody MateriaForm form) {
        return service.createMateria(form);
    }

    @PutMapping("/{id}")
    public MateriaDto update(@PathVariable Integer id, @Valid @RequestBody MateriaForm form) {
        return service.updateMateria(id, form);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.deleteMateria(id);
    }
}
