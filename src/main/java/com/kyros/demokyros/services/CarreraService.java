package com.kyros.demokyros.services;

import com.kyros.demokyros.dto.AreaDto;
import com.kyros.demokyros.dto.CarreraDto;
import com.kyros.demokyros.dto.UniversidadDto;
import com.kyros.demokyros.entity.Carrera;
import com.kyros.demokyros.entity.CarreraUniversidad;
import com.kyros.demokyros.exception.ResourceNotFoundException;
import com.kyros.demokyros.form.CarreraForm;
import com.kyros.demokyros.repository.CarreraRepository;
import com.kyros.demokyros.repository.CarreraUniversidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarreraService {

    private final CarreraRepository repository;
    private final CarreraUniversidadRepository carreraUniversidadRepository;
    private final AreaService areaService;
    private final UniversidadService universidadService;

    public List<CarreraDto> getAllCarreras() {
        List<Carrera> carreras = repository.findAll();
        Map<Integer, AreaDto> areaDtoMap = carreras.stream()
                .map(Carrera::getIdArea)
                .distinct()
                .collect(Collectors.toMap(idArea -> idArea, id -> areaService.toDto(areaService.findEntity(id))));
        return carreras.stream().map(carrera -> toDto(carrera, areaDtoMap.get(carrera.getIdArea()))).toList();
    }

    public CarreraDto getCarreraById(Integer id) {
        Carrera carrera = findEntity(id);
        return toDto(carrera, areaService.toDto(areaService.findEntity(carrera.getIdArea())));
    }

    public CarreraDto createCarrera(CarreraForm form) {
        areaService.findEntity(form.getIdArea());
        Carrera carrera = Carrera.builder()
                .nombreCarrera(form.getNombreCarrera())
                .idArea(form.getIdArea())
                .build();
        Carrera saved = repository.save(carrera);
        return toDto(saved, areaService.toDto(areaService.findEntity(saved.getIdArea())));
    }

    public CarreraDto updateCarrera(Integer id, CarreraForm form) {
        Carrera carrera = findEntity(id);
        areaService.findEntity(form.getIdArea());
        carrera.setNombreCarrera(form.getNombreCarrera());
        carrera.setIdArea(form.getIdArea());
        Carrera saved = repository.save(carrera);
        return toDto(saved, areaService.toDto(areaService.findEntity(saved.getIdArea())));
    }

    public void deleteCarrera(Integer id) {
        repository.delete(findEntity(id));
    }

    public List<UniversidadDto> getUniversidadesDeCarrera(Integer idCarrera) {
        findEntity(idCarrera);
        return carreraUniversidadRepository.findByIdCarrera(idCarrera).stream()
                .map(cu -> universidadService.toDto(universidadService.findEntity(cu.getIdUniversidad())))
                .toList();
    }

    public List<CarreraDto> getCarrerasDeUniversidad(Integer idUniversidad) {
        universidadService.findEntity(idUniversidad);
        return carreraUniversidadRepository.findByIdUniversidad(idUniversidad).stream()
                .map(cu -> getCarreraById(cu.getIdCarrera()))
                .toList();
    }

    public void vincularUniversidad(Integer idCarrera, Integer idUniversidad) {
        findEntity(idCarrera);
        universidadService.findEntity(idUniversidad);
        boolean yaExiste = carreraUniversidadRepository.findByIdCarrera(idCarrera).stream()
                .anyMatch(cu -> cu.getIdUniversidad().equals(idUniversidad));
        if (!yaExiste) {
            carreraUniversidadRepository.save(CarreraUniversidad.builder()
                    .idCarrera(idCarrera)
                    .idUniversidad(idUniversidad)
                    .build());
        }
    }

    public void desvincularUniversidad(Integer idCarrera, Integer idUniversidad) {
        carreraUniversidadRepository.findByIdCarrera(idCarrera).stream()
                .filter(cu -> cu.getIdUniversidad().equals(idUniversidad))
                .findFirst()
                .ifPresent(carreraUniversidadRepository::delete);
    }

    Carrera findEntity(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Carrera no encontrada: " + id));
    }

    private CarreraDto toDto(Carrera carrera, AreaDto areaDto) {
        return CarreraDto.builder()
                .idCarrera(carrera.getIdCarrera())
                .nombreCarrera(carrera.getNombreCarrera())
                .area(areaDto)
                .build();
    }
}
