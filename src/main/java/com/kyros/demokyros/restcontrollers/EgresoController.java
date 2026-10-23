package com.kyros.demokyros.restcontrollers;

import com.kyros.demokyros.dto.EgresoDto;
import com.kyros.demokyros.form.EgresoForm;
import com.kyros.demokyros.services.EgresoService;
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

@RestController
@RequestMapping("/api/egresos")
@RequiredArgsConstructor
public class EgresoController {

    private final EgresoService service;

    @GetMapping
    public List<EgresoDto> getAll() {
        return service.getAllEgresos();
    }

    @GetMapping("/{id}")
    public EgresoDto getById(@PathVariable Integer id) {
        return service.getEgresoById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EgresoDto create(@Valid @RequestBody EgresoForm form) {
        return service.createEgreso(form);
    }
}
