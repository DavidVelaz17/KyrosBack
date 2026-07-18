package com.kyros.demokyros.services;

import com.kyros.demokyros.dto.EstudianteDestinoDto;
import com.kyros.demokyros.dto.EstudianteDto;
import com.kyros.demokyros.dto.GrupoDto;
import com.kyros.demokyros.entity.Estudiante;
import com.kyros.demokyros.entity.EstudianteAsesoria;
import com.kyros.demokyros.entity.EstudianteBachillerato;
import com.kyros.demokyros.entity.EstudianteCursoVerano;
import com.kyros.demokyros.entity.EstudianteSecundaria;
import com.kyros.demokyros.entity.EstudianteUniversidad;
import com.kyros.demokyros.entity.EstudianteUniversidadCarrera;
import com.kyros.demokyros.enums.EstatusEstudiante;
import com.kyros.demokyros.enums.IngresoA;
import com.kyros.demokyros.exception.ResourceNotFoundException;
import com.kyros.demokyros.form.EstudianteEstatusForm;
import com.kyros.demokyros.form.EstudianteForm;
import com.kyros.demokyros.repository.AsesoriaMateriaRepository;
import com.kyros.demokyros.repository.EstudianteAsesoriaRepository;
import com.kyros.demokyros.repository.EstudianteBachilleratoRepository;
import com.kyros.demokyros.repository.EstudianteCursoVeranoRepository;
import com.kyros.demokyros.repository.EstudianteRepository;
import com.kyros.demokyros.repository.EstudianteSecundariaRepository;
import com.kyros.demokyros.repository.EstudianteUniversidadCarreraRepository;
import com.kyros.demokyros.repository.EstudianteUniversidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EstudianteService {

    private static final Map<String, String> EXTENSIONES_PERMITIDAS = Map.of(
            "image/jpeg", ".jpg",
            "image/png", ".png",
            "image/webp", ".webp"
    );

    @Value("${app.uploads.dir:uploads}")
    private String uploadsDir;

    private final EstudianteRepository repository;
    private final GrupoService grupoService;
    private final UniversidadService universidadService;
    private final BachilleratoService bachilleratoService;
    private final SecundariaService secundariaService;
    private final CursoVeranoService cursoVeranoService;
    private final AsesoriaService asesoriaService;
    private final CarreraService carreraService;
    private final MateriaService materiaService;
    private final AsesoriaMateriaRepository asesoriaMateriaRepository;
    private final EstudianteUniversidadRepository estudianteUniversidadRepository;
    private final EstudianteUniversidadCarreraRepository estudianteUniversidadCarreraRepository;
    private final EstudianteBachilleratoRepository estudianteBachilleratoRepository;
    private final EstudianteSecundariaRepository estudianteSecundariaRepository;
    private final EstudianteCursoVeranoRepository estudianteCursoVeranoRepository;
    private final EstudianteAsesoriaRepository estudianteAsesoriaRepository;

    public List<EstudianteDto> getAllEstudiantes() {
        List<Estudiante> estudiantes = repository.findAll();
        Map<Integer, GrupoDto> grupoDtoMap = estudiantes.stream()
                .map(Estudiante::getIdGrupo)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toMap(idGrupo -> idGrupo, idGrupo -> grupoService.toDto(grupoService.findEntity(idGrupo))));
        return estudiantes.stream()
                .map(estudiante -> toDto(estudiante, grupoDtoMap.get(estudiante.getIdGrupo())))
                .toList();
    }

    public EstudianteDto getEstudianteById(Integer id) {
        Estudiante estudiante = findEntity(id);
        return toDto(estudiante, resolveGrupo(estudiante));
    }

    public List<EstudianteDto> getEstudiantesByGrupo(Integer idGrupo) {
        GrupoDto grupoDto = grupoService.toDto(grupoService.findEntity(idGrupo));
        return repository.findByIdGrupo(idGrupo).stream()
                .map(estudiante -> toDto(estudiante, grupoDto))
                .toList();
    }

    public EstudianteDto createEstudiante(EstudianteForm form) {
        ensureMatriculaDisponible(form.getMatricula(), null);
        if (form.getIdGrupo() != null) {
            grupoService.findEntity(form.getIdGrupo());
        }
        Estudiante estudiante = Estudiante.builder()
                .matricula(form.getMatricula())
                .nombre(form.getNombre())
                .apellidoPaterno(form.getApellidoPaterno())
                .apellidoMaterno(form.getApellidoMaterno())
                .edad(form.getEdad())
                .numeroTelefonico(form.getNumeroTelefonico())
                .escuelaProcedencia(form.getEscuelaProcedencia())
                .gradoEscolar(form.getGradoEscolar())
                .nombreTutor(form.getNombreTutor())
                .telefonoTutor(form.getTelefonoTutor())
                .direccion(form.getDireccion())
                .foto(form.getFoto())
                .notas(form.getNotas())
                .fechaInscripcion(form.getFechaInscripcion())
                .horario(form.getHorario())
                .ingresoA(form.getIngresoA())
                .idGrupo(form.getIdGrupo())
                .estatus(EstatusEstudiante.ACTIVO)
                .build();
        Estudiante saved = repository.save(estudiante);
        return toDto(saved, resolveGrupo(saved));
    }

    public EstudianteDto updateEstudiante(Integer id, EstudianteForm form) {
        Estudiante estudiante = findEntity(id);
        ensureMatriculaDisponible(form.getMatricula(), id);
        if (form.getIdGrupo() != null) {
            grupoService.findEntity(form.getIdGrupo());
        }
        estudiante.setMatricula(form.getMatricula());
        estudiante.setNombre(form.getNombre());
        estudiante.setApellidoPaterno(form.getApellidoPaterno());
        estudiante.setApellidoMaterno(form.getApellidoMaterno());
        estudiante.setEdad(form.getEdad());
        estudiante.setNumeroTelefonico(form.getNumeroTelefonico());
        estudiante.setEscuelaProcedencia(form.getEscuelaProcedencia());
        estudiante.setGradoEscolar(form.getGradoEscolar());
        estudiante.setNombreTutor(form.getNombreTutor());
        estudiante.setTelefonoTutor(form.getTelefonoTutor());
        estudiante.setDireccion(form.getDireccion());
        estudiante.setFoto(form.getFoto());
        estudiante.setNotas(form.getNotas());
        estudiante.setFechaInscripcion(form.getFechaInscripcion());
        estudiante.setHorario(form.getHorario());
        estudiante.setIngresoA(form.getIngresoA());
        estudiante.setIdGrupo(form.getIdGrupo());
        Estudiante saved = repository.save(estudiante);
        return toDto(saved, resolveGrupo(saved));
    }

    public void deleteEstudiante(Integer id) {
        repository.delete(findEntity(id));
    }

    /** El resto del alumno es editable vía updateEstudiante; el estatus (Activo/Baja) se
     *  cambia aparte, típicamente al "dar de baja" en vez de borrar al alumno. */
    public EstudianteDto updateEstatus(Integer id, EstudianteEstatusForm form) {
        Estudiante estudiante = findEntity(id);
        estudiante.setEstatus(form.getEstatus());
        Estudiante saved = repository.save(estudiante);
        return toDto(saved, resolveGrupo(saved));
    }

    public EstudianteDto actualizarFoto(Integer id, MultipartFile file) {
        Estudiante estudiante = findEntity(id);
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("El archivo de la foto es obligatorio");
        }
        String extension = EXTENSIONES_PERMITIDAS.get(file.getContentType());
        if (extension == null) {
            throw new IllegalArgumentException("La foto debe ser una imagen JPEG, PNG o WEBP");
        }

        String filename = UUID.randomUUID() + extension;
        try {
            Path destino = Path.of(uploadsDir, filename);
            Files.createDirectories(destino.getParent());
            file.transferTo(destino);
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo guardar la foto del estudiante", e);
        }

        estudiante.setFoto("/uploads/" + filename);
        Estudiante saved = repository.save(estudiante);
        return toDto(saved, resolveGrupo(saved));
    }

    public List<EstudianteDestinoDto> getDestinos(Integer idEstudiante) {
        Estudiante estudiante = findEntity(idEstudiante);
        return switch (estudiante.getIngresoA()) {
            // Carrera(s)/área vía estudiante_universidad -> estudiante_universidad_carrera -> carrera -> area:
            // la(s) carrera(s) que el alumno realmente eligió para esa universidad, no todas las que ofrece.
            case UNIVERSIDAD -> estudianteUniversidadRepository.findByIdEstudiante(idEstudiante).stream()
                    .map(rel -> EstudianteDestinoDto.builder()
                            .idRelacion(rel.getIdEstudianteUniversidad())
                            .idDestino(rel.getIdUniversidad())
                            .nombreDestino(universidadService.findEntity(rel.getIdUniversidad()).getNombreUniversidad())
                            .tipo(IngresoA.UNIVERSIDAD)
                            .carreras(estudianteUniversidadCarreraRepository
                                    .findByIdEstudianteUniversidad(rel.getIdEstudianteUniversidad()).stream()
                                    .map(euc -> carreraService.getCarreraById(euc.getIdCarrera()))
                                    .toList())
                            .build())
                    .toList();
            case BACHILLERATO -> estudianteBachilleratoRepository.findByIdEstudiante(idEstudiante).stream()
                    .map(rel -> EstudianteDestinoDto.builder()
                            .idRelacion(rel.getIdEstudianteBachillerato())
                            .idDestino(rel.getIdBachillerato())
                            .nombreDestino(bachilleratoService.findEntity(rel.getIdBachillerato()).getNombreBachillerato())
                            .tipo(IngresoA.BACHILLERATO)
                            .build())
                    .toList();
            case SECUNDARIA -> estudianteSecundariaRepository.findByIdEstudiante(idEstudiante).stream()
                    .map(rel -> EstudianteDestinoDto.builder()
                            .idRelacion(rel.getIdEstudianteSecundaria())
                            .idDestino(rel.getIdSecundaria())
                            .nombreDestino(secundariaService.findEntity(rel.getIdSecundaria()).getNombreSecundaria())
                            .tipo(IngresoA.SECUNDARIA)
                            .build())
                    .toList();
            case CURSO_VERANO -> estudianteCursoVeranoRepository.findByIdEstudiante(idEstudiante).stream()
                    .map(rel -> EstudianteDestinoDto.builder()
                            .idRelacion(rel.getIdEstudianteCursoVerano())
                            .idDestino(rel.getIdCursoVerano())
                            .nombreDestino(cursoVeranoService.findEntity(rel.getIdCursoVerano()).getNombreCursoVerano())
                            .tipo(IngresoA.CURSO_VERANO)
                            .build())
                    .toList();
            case ASESORIAS -> estudianteAsesoriaRepository.findByIdEstudiante(idEstudiante).stream()
                    .map(rel -> {
                        var asesoria = asesoriaService.findEntity(rel.getIdAsesoria());
                        return EstudianteDestinoDto.builder()
                                .idRelacion(rel.getIdEstudianteAsesoria())
                                .idDestino(rel.getIdAsesoria())
                                .nombreDestino(describeAsesoria(rel.getIdAsesoria()))
                                .tipo(IngresoA.ASESORIAS)
                                .dia(asesoria.getDiaAsesoria())
                                .hora(asesoria.getHoraAsesoria())
                                .materias(asesoriaMateriaRepository.findByIdAsesoria(rel.getIdAsesoria()).stream()
                                        .map(am -> materiaService.getMateriaById(am.getIdMateria()))
                                        .toList())
                                .build();
                    })
                    .toList();
        };
    }

    public void addDestino(Integer idEstudiante, Integer idDestino, Integer idCarrera) {
        Estudiante estudiante = findEntity(idEstudiante);
        switch (estudiante.getIngresoA()) {
            case UNIVERSIDAD -> {
                universidadService.findEntity(idDestino);
                EstudianteUniversidad rel = estudianteUniversidadRepository.findByIdEstudiante(idEstudiante).stream()
                        .filter(r -> r.getIdUniversidad().equals(idDestino))
                        .findFirst()
                        .orElseGet(() -> estudianteUniversidadRepository.save(EstudianteUniversidad.builder()
                                .idEstudiante(idEstudiante).idUniversidad(idDestino).build()));
                if (idCarrera != null) {
                    carreraService.findEntity(idCarrera);
                    if (estudianteUniversidadCarreraRepository
                            .findByIdEstudianteUniversidadAndIdCarrera(rel.getIdEstudianteUniversidad(), idCarrera)
                            .isEmpty()) {
                        estudianteUniversidadCarreraRepository.save(EstudianteUniversidadCarrera.builder()
                                .idEstudianteUniversidad(rel.getIdEstudianteUniversidad())
                                .idCarrera(idCarrera)
                                .build());
                    }
                }
            }
            case BACHILLERATO -> {
                bachilleratoService.findEntity(idDestino);
                if (estudianteBachilleratoRepository.findByIdEstudiante(idEstudiante).stream()
                        .noneMatch(rel -> rel.getIdBachillerato().equals(idDestino))) {
                    estudianteBachilleratoRepository.save(EstudianteBachillerato.builder()
                            .idEstudiante(idEstudiante).idBachillerato(idDestino).build());
                }
            }
            case SECUNDARIA -> {
                secundariaService.findEntity(idDestino);
                if (estudianteSecundariaRepository.findByIdEstudiante(idEstudiante).stream()
                        .noneMatch(rel -> rel.getIdSecundaria().equals(idDestino))) {
                    estudianteSecundariaRepository.save(EstudianteSecundaria.builder()
                            .idEstudiante(idEstudiante).idSecundaria(idDestino).build());
                }
            }
            case CURSO_VERANO -> {
                cursoVeranoService.findEntity(idDestino);
                if (estudianteCursoVeranoRepository.findByIdEstudiante(idEstudiante).stream()
                        .noneMatch(rel -> rel.getIdCursoVerano().equals(idDestino))) {
                    estudianteCursoVeranoRepository.save(EstudianteCursoVerano.builder()
                            .idEstudiante(idEstudiante).idCursoVerano(idDestino).build());
                }
            }
            case ASESORIAS -> {
                asesoriaService.findEntity(idDestino);
                if (estudianteAsesoriaRepository.findByIdEstudiante(idEstudiante).stream()
                        .noneMatch(rel -> rel.getIdAsesoria().equals(idDestino))) {
                    estudianteAsesoriaRepository.save(EstudianteAsesoria.builder()
                            .idEstudiante(idEstudiante).idAsesoria(idDestino).build());
                }
            }
        }
    }

    public void removeDestino(Integer idEstudiante, Integer idDestino) {
        Estudiante estudiante = findEntity(idEstudiante);
        switch (estudiante.getIngresoA()) {
            case UNIVERSIDAD -> estudianteUniversidadRepository.findByIdEstudiante(idEstudiante).stream()
                    .filter(rel -> rel.getIdUniversidad().equals(idDestino))
                    .findFirst()
                    .ifPresent(estudianteUniversidadRepository::delete);
            case BACHILLERATO -> estudianteBachilleratoRepository.findByIdEstudiante(idEstudiante).stream()
                    .filter(rel -> rel.getIdBachillerato().equals(idDestino))
                    .findFirst()
                    .ifPresent(estudianteBachilleratoRepository::delete);
            case SECUNDARIA -> estudianteSecundariaRepository.findByIdEstudiante(idEstudiante).stream()
                    .filter(rel -> rel.getIdSecundaria().equals(idDestino))
                    .findFirst()
                    .ifPresent(estudianteSecundariaRepository::delete);
            case CURSO_VERANO -> estudianteCursoVeranoRepository.findByIdEstudiante(idEstudiante).stream()
                    .filter(rel -> rel.getIdCursoVerano().equals(idDestino))
                    .findFirst()
                    .ifPresent(estudianteCursoVeranoRepository::delete);
            case ASESORIAS -> estudianteAsesoriaRepository.findByIdEstudiante(idEstudiante).stream()
                    .filter(rel -> rel.getIdAsesoria().equals(idDestino))
                    .findFirst()
                    .ifPresent(estudianteAsesoriaRepository::delete);
        }
    }

    private String describeAsesoria(Integer idAsesoria) {
        var asesoria = asesoriaService.findEntity(idAsesoria);
        return asesoria.getDiaAsesoria() + " " + asesoria.getHoraAsesoria().getDescripcion();
    }

    private GrupoDto resolveGrupo(Estudiante estudiante) {
        return estudiante.getIdGrupo() == null ? null : grupoService.toDto(grupoService.findEntity(estudiante.getIdGrupo()));
    }

    private void ensureMatriculaDisponible(String matricula, Integer idEstudianteActual) {
        repository.findByMatricula(matricula).ifPresent(existing -> {
            if (!existing.getIdEstudiante().equals(idEstudianteActual)) {
                throw new IllegalArgumentException("La matrícula ya está en uso: " + matricula);
            }
        });
    }

    Estudiante findEntity(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado: " + id));
    }

    EstudianteDto toDto(Estudiante estudiante, GrupoDto grupoDto) {
        return EstudianteDto.builder()
                .idEstudiante(estudiante.getIdEstudiante())
                .matricula(estudiante.getMatricula())
                .nombre(estudiante.getNombre())
                .apellidoPaterno(estudiante.getApellidoPaterno())
                .apellidoMaterno(estudiante.getApellidoMaterno())
                .edad(estudiante.getEdad())
                .numeroTelefonico(estudiante.getNumeroTelefonico())
                .escuelaProcedencia(estudiante.getEscuelaProcedencia())
                .gradoEscolar(estudiante.getGradoEscolar())
                .nombreTutor(estudiante.getNombreTutor())
                .telefonoTutor(estudiante.getTelefonoTutor())
                .direccion(estudiante.getDireccion())
                .foto(estudiante.getFoto())
                .notas(estudiante.getNotas())
                .fechaInscripcion(estudiante.getFechaInscripcion())
                .horario(estudiante.getHorario())
                .ingresoA(estudiante.getIngresoA())
                .grupo(grupoDto)
                .estatus(estudiante.getEstatus())
                .build();
    }
}
