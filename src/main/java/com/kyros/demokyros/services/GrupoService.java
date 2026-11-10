package com.kyros.demokyros.services;

import com.kyros.demokyros.dto.GrupoDto;
import com.kyros.demokyros.entity.Grupo;
import com.kyros.demokyros.entity.Plantel;
import com.kyros.demokyros.exception.ResourceNotFoundException;
import com.kyros.demokyros.form.GrupoForm;
import com.kyros.demokyros.repository.GrupoRepository;
import com.kyros.demokyros.repository.PlantelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GrupoService {

    private final GrupoRepository repository;
    private final PlantelRepository plantelRepository;
    private final PlantelService plantelService;

    public List<GrupoDto> getAllGrupos() {
        // Una consulta de planteles para todos los grupos, en vez de una por grupo.
        Map<Integer, String> nombresPlantel = plantelRepository.findAll().stream()
                .collect(Collectors.toMap(Plantel::getIdPlantel, Plantel::getNombrePlantel));
        return repository.findAll().stream()
                .map(grupo -> toDto(grupo, nombresPlantel.get(grupo.getIdPlantel())))
                .toList();
    }

    public GrupoDto getGrupoById(Integer id) {
        return toDto(findEntity(id));
    }

    public GrupoDto createGrupo(GrupoForm form) {
        plantelService.findEntity(form.getIdPlantel());
        Grupo grupo = Grupo.builder()
                .nombreGrupo(form.getNombreGrupo())
                .fechaInicio(form.getFechaInicio())
                .idPlantel(form.getIdPlantel())
                .horario(form.getHorario())
                .build();
        return toDto(repository.save(grupo));
    }

    public GrupoDto updateGrupo(Integer id, GrupoForm form) {
        Grupo grupo = findEntity(id);
        plantelService.findEntity(form.getIdPlantel());
        grupo.setNombreGrupo(form.getNombreGrupo());
        grupo.setFechaInicio(form.getFechaInicio());
        grupo.setIdPlantel(form.getIdPlantel());
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
        String nombrePlantel = plantelRepository.findById(grupo.getIdPlantel()).map(Plantel::getNombrePlantel).orElse(null);
        return toDto(grupo, nombrePlantel);
    }

    private GrupoDto toDto(Grupo grupo, String nombrePlantel) {
        return GrupoDto.builder()
                .idGrupo(grupo.getIdGrupo())
                .nombreGrupo(grupo.getNombreGrupo())
                .fechaInicio(grupo.getFechaInicio())
                .idPlantel(grupo.getIdPlantel())
                .nombrePlantel(nombrePlantel)
                .horario(grupo.getHorario())
                .build();
    }
}
