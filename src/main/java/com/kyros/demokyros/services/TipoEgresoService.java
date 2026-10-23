package com.kyros.demokyros.services;

import com.kyros.demokyros.dto.TipoEgresoDto;
import com.kyros.demokyros.entity.TipoEgreso;
import com.kyros.demokyros.exception.ResourceNotFoundException;
import com.kyros.demokyros.form.TipoEgresoForm;
import com.kyros.demokyros.repository.TipoEgresoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoEgresoService {

    private final TipoEgresoRepository repository;

    public List<TipoEgresoDto> getAllTiposEgreso() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public TipoEgresoDto getTipoEgresoById(Integer id) {
        return toDto(findEntity(id));
    }

    public TipoEgresoDto createTipoEgreso(TipoEgresoForm form) {
        TipoEgreso tipoEgreso = TipoEgreso.builder().nombreTipoEgreso(form.getNombreTipoEgreso()).build();
        return toDto(repository.save(tipoEgreso));
    }

    public TipoEgresoDto updateTipoEgreso(Integer id, TipoEgresoForm form) {
        TipoEgreso tipoEgreso = findEntity(id);
        tipoEgreso.setNombreTipoEgreso(form.getNombreTipoEgreso());
        return toDto(repository.save(tipoEgreso));
    }

    // Un tipo con egresos registrados no se borra: la FK RESTRICT lo impide y GlobalExceptionHandler
    // responde 409, igual que con los demás catálogos en uso.
    public void deleteTipoEgreso(Integer id) {
        repository.delete(findEntity(id));
        repository.flush();
    }

    TipoEgreso findEntity(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de egreso no encontrado: " + id));
    }

    TipoEgresoDto toDto(TipoEgreso tipoEgreso) {
        return TipoEgresoDto.builder()
                .idTipoEgreso(tipoEgreso.getIdTipoEgreso())
                .nombreTipoEgreso(tipoEgreso.getNombreTipoEgreso())
                .build();
    }
}
