package com.kyros.demokyros.restcontrollers;

import com.kyros.demokyros.dto.PagoDto;
import com.kyros.demokyros.form.PagoForm;
import com.kyros.demokyros.requests.PagoDeleteRequest;
import com.kyros.demokyros.services.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// pago normalmente es inmutable (solo se crea y se lee): el borrado es la única excepción,
// restringida a ADMIN (SecurityConfig) y con re-confirmación de contraseña (ver deletePago).
@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService service;

    @GetMapping
    public List<PagoDto> getAll() {
        return service.getAllPagos();
    }

    @GetMapping("/{id}")
    public PagoDto getById(@PathVariable Integer id) {
        return service.getPagoById(id);
    }

    @GetMapping("/cargo/{idCargo}")
    public List<PagoDto> getByCargo(@PathVariable Integer idCargo) {
        return service.getPagosByCargo(idCargo);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PagoDto create(@Valid @RequestBody PagoForm form) {
        return service.createPago(form);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id, @Valid @RequestBody PagoDeleteRequest request) {
        service.deletePago(id, request.getPassword());
    }
}
