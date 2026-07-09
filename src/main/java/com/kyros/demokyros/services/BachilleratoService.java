package com.kyros.demokyros.services;

import com.kyros.demokyros.dto.BachilleratoDto;
import com.kyros.demokyros.entity.Bachillerato;
import com.kyros.demokyros.exception.ResourceNotFoundException;
import com.kyros.demokyros.form.BachilleratoForm;
import com.kyros.demokyros.repository.BachilleratoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BachilleratoService {

    private final BachilleratoRepository repository;

    public List<BachilleratoDto> getAllBachilleratos() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public BachilleratoDto getBachilleratoById(Integer id) {
        return toDto(findEntity(id));
    }

    public BachilleratoDto createBachillerato(BachilleratoForm form) {
        Bachillerato bachillerato = Bachillerato.builder().nombreBachillerato(form.getNombreBachillerato()).build();
        return toDto(repository.save(bachillerato));
    }

    public BachilleratoDto updateBachillerato(Integer id, BachilleratoForm form) {
        Bachillerato bachillerato = findEntity(id);
        bachillerato.setNombreBachillerato(form.getNombreBachillerato());
        return toDto(repository.save(bachillerato));
    }

    public void deleteBachillerato(Integer id) {
        repository.delete(findEntity(id));
    }

    Bachillerato findEntity(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bachillerato no encontrado: " + id));
    }

    BachilleratoDto toDto(Bachillerato bachillerato) {
        return BachilleratoDto.builder()
                .idBachillerato(bachillerato.getIdBachillerato())
                .nombreBachillerato(bachillerato.getNombreBachillerato())
                .build();
    }
}
