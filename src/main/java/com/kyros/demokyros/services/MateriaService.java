package com.kyros.demokyros.services;

import com.kyros.demokyros.dto.MateriaDto;
import com.kyros.demokyros.entity.Materia;
import com.kyros.demokyros.exception.ResourceNotFoundException;
import com.kyros.demokyros.form.MateriaForm;
import com.kyros.demokyros.repository.MateriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MateriaService {

    private final MateriaRepository repository;

    public List<MateriaDto> getAllMaterias() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public MateriaDto getMateriaById(Integer id) {
        return toDto(findEntity(id));
    }

    public MateriaDto createMateria(MateriaForm form) {
        Materia materia = Materia.builder().nombreMateria(form.getNombreMateria()).build();
        return toDto(repository.save(materia));
    }

    public MateriaDto updateMateria(Integer id, MateriaForm form) {
        Materia materia = findEntity(id);
        materia.setNombreMateria(form.getNombreMateria());
        return toDto(repository.save(materia));
    }

    public void deleteMateria(Integer id) {
        repository.delete(findEntity(id));
    }

    Materia findEntity(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Materia no encontrada: " + id));
    }

    MateriaDto toDto(Materia materia) {
        return MateriaDto.builder()
                .idMateria(materia.getIdMateria())
                .nombreMateria(materia.getNombreMateria())
                .build();
    }
}
