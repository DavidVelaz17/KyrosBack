package com.kyros.demokyros.services;

import com.kyros.demokyros.dto.AreaDto;
import com.kyros.demokyros.entity.Area;
import com.kyros.demokyros.exception.ResourceNotFoundException;
import com.kyros.demokyros.form.AreaForm;
import com.kyros.demokyros.repository.AreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AreaService {

    private final AreaRepository repository;

    public List<AreaDto> getAllAreas() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public AreaDto getAreaById(Integer id) {
        return toDto(findEntity(id));
    }

    public AreaDto createArea(AreaForm form) {
        Area area = Area.builder().nombreArea(form.getNombreArea()).build();
        return toDto(repository.save(area));
    }

    public AreaDto updateArea(Integer id, AreaForm form) {
        Area area = findEntity(id);
        area.setNombreArea(form.getNombreArea());
        return toDto(repository.save(area));
    }

    public void deleteArea(Integer id) {
        repository.delete(findEntity(id));
    }

    Area findEntity(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Área no encontrada: " + id));
    }

    AreaDto toDto(Area area) {
        return AreaDto.builder()
                .idArea(area.getIdArea())
                .nombreArea(area.getNombreArea())
                .build();
    }
}
