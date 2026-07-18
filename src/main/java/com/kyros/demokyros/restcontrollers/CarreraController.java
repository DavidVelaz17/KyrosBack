package com.kyros.demokyros.restcontrollers;

import com.kyros.demokyros.dto.CarreraDto;
import com.kyros.demokyros.dto.UniversidadDto;
import com.kyros.demokyros.form.CarreraForm;
import com.kyros.demokyros.services.CarreraService;
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
@RequestMapping("/api/carreras")
@RequiredArgsConstructor
public class CarreraController {

    private final CarreraService service;

    @GetMapping
    public List<CarreraDto> getAll() {
        return service.getAllCarreras();
    }

    @GetMapping("/{id}")
    public CarreraDto getById(@PathVariable Integer id) {
        return service.getCarreraById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarreraDto create(@Valid @RequestBody CarreraForm form) {
        return service.createCarrera(form);
    }

    @PutMapping("/{id}")
    public CarreraDto update(@PathVariable Integer id, @Valid @RequestBody CarreraForm form) {
        return service.updateCarrera(id, form);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.deleteCarrera(id);
    }

    @GetMapping("/{id}/universidades")
    public List<UniversidadDto> getUniversidades(@PathVariable Integer id) {
        return service.getUniversidadesDeCarrera(id);
    }

    @PostMapping("/{id}/universidades/{idUniversidad}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void vincularUniversidad(@PathVariable Integer id, @PathVariable Integer idUniversidad) {
        service.vincularUniversidad(id, idUniversidad);
    }

    @DeleteMapping("/{id}/universidades/{idUniversidad}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desvincularUniversidad(@PathVariable Integer id, @PathVariable Integer idUniversidad) {
        service.desvincularUniversidad(id, idUniversidad);
    }
}
