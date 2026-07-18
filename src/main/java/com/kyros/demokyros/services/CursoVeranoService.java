package com.kyros.demokyros.services;

import com.kyros.demokyros.dto.CursoVeranoDto;
import com.kyros.demokyros.entity.CursoVerano;
import com.kyros.demokyros.exception.ResourceNotFoundException;
import com.kyros.demokyros.form.CursoVeranoForm;
import com.kyros.demokyros.repository.CursoVeranoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CursoVeranoService {

    private final CursoVeranoRepository repository;

    public List<CursoVeranoDto> getAllCursosVerano() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public CursoVeranoDto getCursoVeranoById(Integer id) {
        return toDto(findEntity(id));
    }

    public CursoVeranoDto createCursoVerano(CursoVeranoForm form) {
        CursoVerano cursoVerano = CursoVerano.builder().nombreCursoVerano(form.getNombreCursoVerano()).build();
        return toDto(repository.save(cursoVerano));
    }

    public CursoVeranoDto updateCursoVerano(Integer id, CursoVeranoForm form) {
        CursoVerano cursoVerano = findEntity(id);
        cursoVerano.setNombreCursoVerano(form.getNombreCursoVerano());
        return toDto(repository.save(cursoVerano));
    }

    public void deleteCursoVerano(Integer id) {
        repository.delete(findEntity(id));
    }

    CursoVerano findEntity(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso de verano no encontrado: " + id));
    }

    CursoVeranoDto toDto(CursoVerano cursoVerano) {
        return CursoVeranoDto.builder()
                .idCursoVerano(cursoVerano.getIdCursoVerano())
                .nombreCursoVerano(cursoVerano.getNombreCursoVerano())
                .build();
    }
}
