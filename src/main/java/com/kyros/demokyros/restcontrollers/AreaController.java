package com.kyros.demokyros.restcontrollers;

import com.kyros.demokyros.dto.AreaDto;
import com.kyros.demokyros.form.AreaForm;
import com.kyros.demokyros.services.AreaService;
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
@RequestMapping("/api/areas")
@RequiredArgsConstructor
public class AreaController {

    private final AreaService service;

    @GetMapping
    public List<AreaDto> getAll() {
        return service.getAllAreas();
    }

    @GetMapping("/{id}")
    public AreaDto getById(@PathVariable Integer id) {
        return service.getAreaById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AreaDto create(@Valid @RequestBody AreaForm form) {
        return service.createArea(form);
    }

    @PutMapping("/{id}")
    public AreaDto update(@PathVariable Integer id, @Valid @RequestBody AreaForm form) {
        return service.updateArea(id, form);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.deleteArea(id);
    }
}
