package com.kyros.demokyros.restcontrollers;

import com.kyros.demokyros.dto.BachilleratoDto;
import com.kyros.demokyros.form.BachilleratoForm;
import com.kyros.demokyros.services.BachilleratoService;
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
@RequestMapping("/api/bachilleratos")
@RequiredArgsConstructor
public class BachilleratoController {

    private final BachilleratoService service;

    @GetMapping
    public List<BachilleratoDto> getAll() {
        return service.getAllBachilleratos();
    }

    @GetMapping("/{id}")
    public BachilleratoDto getById(@PathVariable Integer id) {
        return service.getBachilleratoById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BachilleratoDto create(@Valid @RequestBody BachilleratoForm form) {
        return service.createBachillerato(form);
    }

    @PutMapping("/{id}")
    public BachilleratoDto update(@PathVariable Integer id, @Valid @RequestBody BachilleratoForm form) {
        return service.updateBachillerato(id, form);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.deleteBachillerato(id);
    }
}
