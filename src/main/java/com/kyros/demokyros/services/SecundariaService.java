package com.kyros.demokyros.services;

import com.kyros.demokyros.dto.SecundariaDto;
import com.kyros.demokyros.entity.Secundaria;
import com.kyros.demokyros.exception.ResourceNotFoundException;
import com.kyros.demokyros.form.SecundariaForm;
import com.kyros.demokyros.repository.SecundariaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SecundariaService {

    private final SecundariaRepository repository;

    public List<SecundariaDto> getAllSecundarias() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public SecundariaDto getSecundariaById(Integer id) {
        return toDto(findEntity(id));
    }

    public SecundariaDto createSecundaria(SecundariaForm form) {
        Secundaria secundaria = Secundaria.builder().nombreSecundaria(form.getNombreSecundaria()).build();
        return toDto(repository.save(secundaria));
    }

    public SecundariaDto updateSecundaria(Integer id, SecundariaForm form) {
        Secundaria secundaria = findEntity(id);
        secundaria.setNombreSecundaria(form.getNombreSecundaria());
        return toDto(repository.save(secundaria));
    }

    public void deleteSecundaria(Integer id) {
        repository.delete(findEntity(id));
    }

    Secundaria findEntity(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Secundaria no encontrada: " + id));
    }

    SecundariaDto toDto(Secundaria secundaria) {
        return SecundariaDto.builder()
                .idSecundaria(secundaria.getIdSecundaria())
                .nombreSecundaria(secundaria.getNombreSecundaria())
                .build();
    }
}
