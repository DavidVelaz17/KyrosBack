package com.kyros.demokyros.restcontrollers;

import com.kyros.demokyros.dto.TipoEgresoDto;
import com.kyros.demokyros.form.TipoEgresoForm;
import com.kyros.demokyros.services.TipoEgresoService;
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
@RequestMapping("/api/tipos-egreso")
@RequiredArgsConstructor
public class TipoEgresoController {

    private final TipoEgresoService service;

    @GetMapping
    public List<TipoEgresoDto> getAll() {
        return service.getAllTiposEgreso();
    }

    @GetMapping("/{id}")
    public TipoEgresoDto getById(@PathVariable Integer id) {
        return service.getTipoEgresoById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TipoEgresoDto create(@Valid @RequestBody TipoEgresoForm form) {
        return service.createTipoEgreso(form);
    }

    @PutMapping("/{id}")
    public TipoEgresoDto update(@PathVariable Integer id, @Valid @RequestBody TipoEgresoForm form) {
        return service.updateTipoEgreso(id, form);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.deleteTipoEgreso(id);
    }
}
