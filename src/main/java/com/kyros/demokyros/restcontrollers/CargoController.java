package com.kyros.demokyros.restcontrollers;

import com.kyros.demokyros.dto.CargoDto;
import com.kyros.demokyros.form.CargoForm;
import com.kyros.demokyros.services.CargoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// cargo es inmutable: solo se crea y se lee, nunca se edita ni se borra
@RestController
@RequestMapping("/api/cargos")
@RequiredArgsConstructor
public class CargoController {

    private final CargoService service;

    @GetMapping
    public List<CargoDto> getAll() {
        return service.getAllCargos();
    }

    @GetMapping("/{id}")
    public CargoDto getById(@PathVariable Integer id) {
        return service.getCargoById(id);
    }

    @GetMapping("/estudiante/{idEstudiante}")
    public List<CargoDto> getByEstudiante(@PathVariable Integer idEstudiante) {
        return service.getCargosByEstudiante(idEstudiante);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CargoDto create(@Valid @RequestBody CargoForm form) {
        return service.createCargo(form);
    }
}
