package com.kyros.demokyros.restcontrollers;

import com.kyros.demokyros.dto.AsesoriaDto;
import com.kyros.demokyros.dto.MateriaDto;
import com.kyros.demokyros.form.AsesoriaForm;
import com.kyros.demokyros.services.AsesoriaService;
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
@RequestMapping("/api/asesorias")
@RequiredArgsConstructor
public class AsesoriaController {

    private final AsesoriaService service;

    @GetMapping
    public List<AsesoriaDto> getAll() {
        return service.getAllAsesorias();
    }

    @GetMapping("/{id}")
    public AsesoriaDto getById(@PathVariable Integer id) {
        return service.getAsesoriaById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AsesoriaDto create(@Valid @RequestBody AsesoriaForm form) {
        return service.createAsesoria(form);
    }

    @PutMapping("/{id}")
    public AsesoriaDto update(@PathVariable Integer id, @Valid @RequestBody AsesoriaForm form) {
        return service.updateAsesoria(id, form);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.deleteAsesoria(id);
    }

    @GetMapping("/{id}/materias")
    public List<MateriaDto> getMaterias(@PathVariable Integer id) {
        return service.getMateriasDeAsesoria(id);
    }

    @PostMapping("/{id}/materias/{idMateria}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void vincularMateria(@PathVariable Integer id, @PathVariable Integer idMateria) {
        service.vincularMateria(id, idMateria);
    }

    @DeleteMapping("/{id}/materias/{idMateria}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desvincularMateria(@PathVariable Integer id, @PathVariable Integer idMateria) {
        service.desvincularMateria(id, idMateria);
    }
}
