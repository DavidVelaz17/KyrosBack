package com.kyros.demokyros.services;

import com.kyros.demokyros.dto.UniversidadDto;
import com.kyros.demokyros.entity.Universidad;
import com.kyros.demokyros.exception.ResourceNotFoundException;
import com.kyros.demokyros.form.UniversidadForm;
import com.kyros.demokyros.repository.UniversidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UniversidadService {

    private final UniversidadRepository repository;

    public List<UniversidadDto> getAllUniversidades() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public UniversidadDto getUniversidadById(Integer id) {
        return toDto(findEntity(id));
    }

    public UniversidadDto createUniversidad(UniversidadForm form) {
        Universidad universidad = Universidad.builder().nombreUniversidad(form.getNombreUniversidad()).build();
        return toDto(repository.save(universidad));
    }

    public UniversidadDto updateUniversidad(Integer id, UniversidadForm form) {
        Universidad universidad = findEntity(id);
        universidad.setNombreUniversidad(form.getNombreUniversidad());
        return toDto(repository.save(universidad));
    }

    public void deleteUniversidad(Integer id) {
        repository.delete(findEntity(id));
    }

    Universidad findEntity(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Universidad no encontrada: " + id));
    }

    UniversidadDto toDto(Universidad universidad) {
        return UniversidadDto.builder()
                .idUniversidad(universidad.getIdUniversidad())
                .nombreUniversidad(universidad.getNombreUniversidad())
                .build();
    }
}
