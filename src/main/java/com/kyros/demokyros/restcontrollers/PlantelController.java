package com.kyros.demokyros.restcontrollers;

import com.kyros.demokyros.dto.PlantelDto;
import com.kyros.demokyros.form.PlantelForm;
import com.kyros.demokyros.services.PlantelService;
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
@RequestMapping("/api/planteles")
@RequiredArgsConstructor
public class PlantelController {

    private final PlantelService service;

    @GetMapping
    public List<PlantelDto> getAll() {
        return service.getAllPlanteles();
    }

    @GetMapping("/{id}")
    public PlantelDto getById(@PathVariable Integer id) {
        return service.getPlantelById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlantelDto create(@Valid @RequestBody PlantelForm form) {
        return service.createPlantel(form);
    }

    @PutMapping("/{id}")
    public PlantelDto update(@PathVariable Integer id, @Valid @RequestBody PlantelForm form) {
        return service.updatePlantel(id, form);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.deletePlantel(id);
    }
}
