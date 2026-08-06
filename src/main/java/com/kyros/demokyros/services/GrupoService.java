package com.kyros.demokyros.services;

import com.kyros.demokyros.dto.GrupoDto;
import com.kyros.demokyros.entity.Grupo;
import com.kyros.demokyros.exception.ResourceNotFoundException;
import com.kyros.demokyros.form.GrupoForm;
import com.kyros.demokyros.repository.GrupoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GrupoService {

    private final GrupoRepository repository;

    public List<GrupoDto> getAllGrupos() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public GrupoDto getGrupoById(Integer id) {
        return toDto(findEntity(id));
    }

    public GrupoDto createGrupo(GrupoForm form) {
        Grupo grupo = Grupo.builder()
                .nombreGrupo(form.getNombreGrupo())
                .fechaInicio(form.getFechaInicio())
                .nombrePlantel(form.getNombrePlantel())
                .horario(form.getHorario())
                .build();
        return toDto(repository.save(grupo));
    }

    public GrupoDto updateGrupo(Integer id, GrupoForm form) {
        Grupo grupo = findEntity(id);
        grupo.setNombreGrupo(form.getNombreGrupo());
        grupo.setFechaInicio(form.getFechaInicio());
        grupo.setNombrePlantel(form.getNombrePlantel());
        grupo.setHorario(form.getHorario());
        return toDto(repository.save(grupo));
    }

    public void deleteGrupo(Integer id) {
        repository.delete(findEntity(id));
    }

    Grupo findEntity(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo no encontrado: " + id));
    }

    GrupoDto toDto(Grupo grupo) {
        return GrupoDto.builder()
                .idGrupo(grupo.getIdGrupo())
                .nombreGrupo(grupo.getNombreGrupo())
                .fechaInicio(grupo.getFechaInicio())
                .nombrePlantel(grupo.getNombrePlantel())
                .horario(grupo.getHorario())
                .build();
    }
}
