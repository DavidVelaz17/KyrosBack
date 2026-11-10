package com.kyros.demokyros.services;

import com.kyros.demokyros.dto.PlantelDto;
import com.kyros.demokyros.entity.Plantel;
import com.kyros.demokyros.exception.ResourceNotFoundException;
import com.kyros.demokyros.form.PlantelForm;
import com.kyros.demokyros.repository.EgresoRepository;
import com.kyros.demokyros.repository.GrupoRepository;
import com.kyros.demokyros.repository.PlantelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlantelService {

    private final PlantelRepository repository;
    private final GrupoRepository grupoRepository;
    private final EgresoRepository egresoRepository;

    public List<PlantelDto> getAllPlanteles() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public PlantelDto getPlantelById(Integer id) {
        return toDto(findEntity(id));
    }

    public PlantelDto createPlantel(PlantelForm form) {
        Plantel plantel = Plantel.builder().nombrePlantel(form.getNombrePlantel().trim()).build();
        return toDto(repository.save(plantel));
    }

    // Los grupos guardan el id, así que renombrar se refleja en todos ellos.
    public PlantelDto updatePlantel(Integer id, PlantelForm form) {
        Plantel plantel = findEntity(id);
        plantel.setNombrePlantel(form.getNombrePlantel().trim());
        return toDto(repository.save(plantel));
    }

    // Las FK fk_grupo_plantel / fk_egreso_plantel (RESTRICT) también lo impedirían, pero con un 409
    // genérico: aquí se revisa antes para decir qué lo usa.
    public void deletePlantel(Integer id) {
        Plantel plantel = findEntity(id);
        long grupos = grupoRepository.countByIdPlantel(id);
        if (grupos > 0) {
            throw new IllegalArgumentException("No se puede eliminar \"" + plantel.getNombrePlantel() + "\": tiene "
                    + grupos + (grupos == 1 ? " grupo asignado" : " grupos asignados") + ". Cámbialos de plantel primero.");
        }
        long egresos = egresoRepository.countByIdPlantel(id);
        if (egresos > 0) {
            throw new IllegalArgumentException("No se puede eliminar \"" + plantel.getNombrePlantel() + "\": tiene "
                    + egresos + (egresos == 1 ? " egreso registrado" : " egresos registrados") + ".");
        }
        repository.delete(plantel);
    }

    Plantel findEntity(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plantel no encontrado: " + id));
    }

    PlantelDto toDto(Plantel plantel) {
        return PlantelDto.builder()
                .idPlantel(plantel.getIdPlantel())
                .nombrePlantel(plantel.getNombrePlantel())
                .build();
    }
}
