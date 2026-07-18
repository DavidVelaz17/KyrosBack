package com.kyros.demokyros.restcontrollers;

import com.kyros.demokyros.dto.EstudianteDto;
import com.kyros.demokyros.dto.GrupoDto;
import com.kyros.demokyros.form.GrupoForm;
import com.kyros.demokyros.services.EstudianteService;
import com.kyros.demokyros.services.GrupoService;
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
@RequestMapping("/api/grupos")
@RequiredArgsConstructor
public class GrupoController {

    private final GrupoService service;
    private final EstudianteService estudianteService;

    @GetMapping
    public List<GrupoDto> getAll() {
        return service.getAllGrupos();
    }

    @GetMapping("/{id}")
    public GrupoDto getById(@PathVariable Integer id) {
        return service.getGrupoById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GrupoDto create(@Valid @RequestBody GrupoForm form) {
        return service.createGrupo(form);
    }

    @PutMapping("/{id}")
    public GrupoDto update(@PathVariable Integer id, @Valid @RequestBody GrupoForm form) {
        return service.updateGrupo(id, form);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.deleteGrupo(id);
    }

    @GetMapping("/{id}/estudiantes")
    public List<EstudianteDto> getEstudiantes(@PathVariable Integer id) {
        return estudianteService.getEstudiantesByGrupo(id);
    }
}
