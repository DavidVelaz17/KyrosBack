package com.kyros.demokyros.services;

import com.kyros.demokyros.dto.AsesoriaDto;
import com.kyros.demokyros.dto.MateriaDto;
import com.kyros.demokyros.entity.Asesoria;
import com.kyros.demokyros.entity.AsesoriaMateria;
import com.kyros.demokyros.exception.ResourceNotFoundException;
import com.kyros.demokyros.form.AsesoriaForm;
import com.kyros.demokyros.repository.AsesoriaMateriaRepository;
import com.kyros.demokyros.repository.AsesoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AsesoriaService {

    private final AsesoriaRepository repository;
    private final AsesoriaMateriaRepository asesoriaMateriaRepository;
    private final MateriaService materiaService;

    public List<AsesoriaDto> getAllAsesorias() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public AsesoriaDto getAsesoriaById(Integer id) {
        return toDto(findEntity(id));
    }

    public AsesoriaDto createAsesoria(AsesoriaForm form) {
        Asesoria asesoria = Asesoria.builder()
                .diaAsesoria(form.getDiaAsesoria())
                .horaAsesoria(form.getHoraAsesoria())
                .build();
        return toDto(repository.save(asesoria));
    }

    public AsesoriaDto updateAsesoria(Integer id, AsesoriaForm form) {
        Asesoria asesoria = findEntity(id);
        asesoria.setDiaAsesoria(form.getDiaAsesoria());
        asesoria.setHoraAsesoria(form.getHoraAsesoria());
        return toDto(repository.save(asesoria));
    }

    public void deleteAsesoria(Integer id) {
        repository.delete(findEntity(id));
    }

    public List<MateriaDto> getMateriasDeAsesoria(Integer idAsesoria) {
        findEntity(idAsesoria);
        return asesoriaMateriaRepository.findByIdAsesoria(idAsesoria).stream()
                .map(am -> materiaService.toDto(materiaService.findEntity(am.getIdMateria())))
                .toList();
    }

    public void vincularMateria(Integer idAsesoria, Integer idMateria) {
        findEntity(idAsesoria);
        materiaService.findEntity(idMateria);
        boolean yaExiste = asesoriaMateriaRepository.findByIdAsesoria(idAsesoria).stream()
                .anyMatch(am -> am.getIdMateria().equals(idMateria));
        if (!yaExiste) {
            asesoriaMateriaRepository.save(AsesoriaMateria.builder()
                    .idAsesoria(idAsesoria)
                    .idMateria(idMateria)
                    .build());
        }
    }

    public void desvincularMateria(Integer idAsesoria, Integer idMateria) {
        asesoriaMateriaRepository.findByIdAsesoria(idAsesoria).stream()
                .filter(am -> am.getIdMateria().equals(idMateria))
                .findFirst()
                .ifPresent(asesoriaMateriaRepository::delete);
    }

    Asesoria findEntity(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asesoría no encontrada: " + id));
    }

    AsesoriaDto toDto(Asesoria asesoria) {
        return AsesoriaDto.builder()
                .idAsesoria(asesoria.getIdAsesoria())
                .diaAsesoria(asesoria.getDiaAsesoria())
                .horaAsesoria(asesoria.getHoraAsesoria())
                .build();
    }
}
