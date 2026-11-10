package com.kyros.demokyros.restcontrollers;

import com.kyros.demokyros.dto.EgresoDto;
import com.kyros.demokyros.dto.EgresoMovimientoDto;
import com.kyros.demokyros.form.EgresoForm;
import com.kyros.demokyros.requests.EgresoCorreccionRequest;
import com.kyros.demokyros.requests.EgresoJustificacionRequest;
import com.kyros.demokyros.services.EgresoService;
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

    @GetMapping("/{id}/movimientos")
    public List<EgresoMovimientoDto> getMovimientos(@PathVariable Integer id) {
        return service.getMovimientos(id);
    }

    @PutMapping("/{id}")
    public EgresoDto corregir(@PathVariable Integer id, @Valid @RequestBody EgresoCorreccionRequest request) {
        return service.corregirEgreso(id, request);
    }

    // No borra el registro: lo marca como ELIMINADO (ver EgresoService.eliminarEgreso). Responde el
    // egreso actualizado para que el front lo reemplace en su lista.
    @DeleteMapping("/{id}")
    public EgresoDto eliminar(@PathVariable Integer id, @Valid @RequestBody EgresoJustificacionRequest request) {
        return service.eliminarEgreso(id, request);
    }

    @PostMapping("/{id}/restaurar")
    public EgresoDto restaurar(@PathVariable Integer id, @Valid @RequestBody EgresoJustificacionRequest request) {
        return service.restaurarEgreso(id, request);
    }
}
