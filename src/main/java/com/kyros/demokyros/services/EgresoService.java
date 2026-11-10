package com.kyros.demokyros.services;

import com.kyros.demokyros.dto.EgresoDto;
import com.kyros.demokyros.dto.EgresoMovimientoDto;
import com.kyros.demokyros.dto.PlantelDto;
import com.kyros.demokyros.dto.TipoEgresoDto;
import com.kyros.demokyros.entity.Egreso;
import com.kyros.demokyros.entity.EgresoMovimiento;
import com.kyros.demokyros.entity.Usuario;
import com.kyros.demokyros.enums.EstatusEgreso;
import com.kyros.demokyros.enums.TipoMovimientoEgreso;
import com.kyros.demokyros.exception.InvalidCredentialsException;
import com.kyros.demokyros.exception.ResourceNotFoundException;
import com.kyros.demokyros.form.EgresoForm;
import com.kyros.demokyros.repository.EgresoMovimientoRepository;
import com.kyros.demokyros.repository.EgresoRepository;
import com.kyros.demokyros.repository.PlantelRepository;
import com.kyros.demokyros.repository.TipoEgresoRepository;
import com.kyros.demokyros.repository.UsuarioRepository;
import com.kyros.demokyros.requests.EgresoCorreccionRequest;
import com.kyros.demokyros.requests.EgresoJustificacionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class EgresoService {

    private final EgresoRepository repository;
    private final EgresoMovimientoRepository movimientoRepository;
    private final TipoEgresoRepository tipoEgresoRepository;
    private final TipoEgresoService tipoEgresoService;
    private final UsuarioRepository usuarioRepository;
    private final PlantelRepository plantelRepository;
    private final PlantelService plantelService;
    private final PasswordEncoder passwordEncoder;

    // Devuelve también los ELIMINADO: el front los muestra marcados y es quien los excluye de los
    // totales y del reporte.
    public List<EgresoDto> getAllEgresos() {
        List<Egreso> egresos = repository.findAll();
        Map<Integer, EgresoMovimiento> ultimos = movimientoRepository.findUltimoDeCadaEgreso().stream()
                .collect(Collectors.toMap(EgresoMovimiento::getIdEgreso, Function.identity()));
        // Una consulta por tabla relacionada en vez de una por egreso.
        Map<Integer, TipoEgresoDto> tipos = tiposPorId();
        Map<Integer, PlantelDto> planteles = plantelesPorId();
        Map<Integer, String> nombresUsuario = nombresUsuario(Stream.concat(
                egresos.stream().map(Egreso::getIdUsuario),
                ultimos.values().stream().map(EgresoMovimiento::getIdUsuario)).toList());
        return egresos.stream()
                .map(egreso -> toDto(egreso, tipos, planteles, nombresUsuario, ultimos.get(egreso.getIdEgreso())))
                .toList();
    }

    public EgresoDto getEgresoById(Integer id) {
        Egreso egreso = findEntity(id);
        EgresoMovimiento ultimo = movimientoRepository.findFirstByIdEgresoOrderByIdEgresoMovimientoDesc(id).orElse(null);
        List<Integer> idsUsuario = ultimo == null
                ? List.of(egreso.getIdUsuario())
                : List.of(egreso.getIdUsuario(), ultimo.getIdUsuario());
        return toDto(egreso, tiposPorId(), plantelesPorId(), nombresUsuario(idsUsuario), ultimo);
    }

    /** Historial del egreso, del más antiguo al más reciente. El registro original no es un
     *  movimiento: sus datos vienen en el propio egreso (y, si se corrigió, en el "anterior" de la
     *  primera corrección). */
    public List<EgresoMovimientoDto> getMovimientos(Integer idEgreso) {
        findEntity(idEgreso);
        List<EgresoMovimiento> movimientos = movimientoRepository.findByIdEgresoOrderByIdEgresoMovimientoAsc(idEgreso);
        Map<Integer, TipoEgresoDto> tipos = tiposPorId();
        Map<Integer, PlantelDto> planteles = plantelesPorId();
        Map<Integer, String> nombresUsuario = nombresUsuario(movimientos.stream().map(EgresoMovimiento::getIdUsuario).toList());
        return movimientos.stream().map(movimiento -> toMovimientoDto(movimiento, tipos, planteles, nombresUsuario)).toList();
    }

    // El usuario que registra se toma de la sesión autenticada (el JWT validado por Spring Security),
    // no del cuerpo de la petición, para que nadie pueda registrar un egreso a nombre de otro.
    public EgresoDto createEgreso(EgresoForm form) {
        tipoEgresoService.findEntity(form.getIdTipoEgreso());
        plantelService.findEntity(form.getIdPlantel());
        String usuarioActual = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsuario(usuarioActual)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + usuarioActual));
        Egreso egreso = Egreso.builder()
                .idTipoEgreso(form.getIdTipoEgreso())
                .importeEgreso(form.getImporteEgreso())
                .fechaHoraEgreso(form.getFechaHoraEgreso())
                .observacionesEgreso(normalizarObservaciones(form.getObservacionesEgreso()))
                .idUsuario(usuario.getIdUsuario())
                .idPlantel(form.getIdPlantel())
                .estatusEgreso(EstatusEgreso.SIN_CAMBIOS)
                .fechaRegistroEgreso(LocalDateTime.now())
                .build();
        return getEgresoById(repository.save(egreso).getIdEgreso());
    }

    // Corregir, eliminar y restaurar re-validan la contraseña del usuario autenticado (igual que
    // PagoService.deletePago) y dejan su rastro en egreso_movimiento con quién, cuándo y por qué.
    @Transactional
    public EgresoDto corregirEgreso(Integer id, EgresoCorreccionRequest request) {
        Usuario usuario = validarPasswordUsuarioActual(request.getPassword());
        Egreso egreso = findEntity(id);
        if (egreso.getEstatusEgreso() == EstatusEgreso.ELIMINADO) {
            throw new IllegalArgumentException("No se puede corregir un egreso eliminado; restáuralo primero.");
        }
        tipoEgresoService.findEntity(request.getIdTipoEgreso());
        plantelService.findEntity(request.getIdPlantel());
        String observaciones = normalizarObservaciones(request.getObservacionesEgreso());
        boolean sinCambios = Objects.equals(egreso.getIdTipoEgreso(), request.getIdTipoEgreso())
                && Objects.equals(egreso.getImporteEgreso(), request.getImporteEgreso())
                && Objects.equals(egreso.getFechaHoraEgreso(), request.getFechaHoraEgreso())
                && Objects.equals(egreso.getObservacionesEgreso(), observaciones)
                && Objects.equals(egreso.getIdPlantel(), request.getIdPlantel());
        if (sinCambios) {
            throw new IllegalArgumentException("No hay cambios que guardar.");
        }

        movimientoRepository.save(EgresoMovimiento.builder()
                .idEgreso(id)
                .tipoMovimiento(TipoMovimientoEgreso.CORRECCION)
                .fechaHoraMovimiento(LocalDateTime.now())
                .idUsuario(usuario.getIdUsuario())
                .justificacionMovimiento(request.getJustificacion().trim())
                .idTipoEgresoAnterior(egreso.getIdTipoEgreso())
                .idTipoEgresoNuevo(request.getIdTipoEgreso())
                .importeAnterior(egreso.getImporteEgreso())
                .importeNuevo(request.getImporteEgreso())
                .fechaHoraAnterior(egreso.getFechaHoraEgreso())
                .fechaHoraNueva(request.getFechaHoraEgreso())
                .observacionesAnteriores(egreso.getObservacionesEgreso())
                .observacionesNuevas(observaciones)
                .idPlantelAnterior(egreso.getIdPlantel())
                .idPlantelNuevo(request.getIdPlantel())
                .build());

        egreso.setIdTipoEgreso(request.getIdTipoEgreso());
        egreso.setImporteEgreso(request.getImporteEgreso());
        egreso.setFechaHoraEgreso(request.getFechaHoraEgreso());
        egreso.setObservacionesEgreso(observaciones);
        egreso.setIdPlantel(request.getIdPlantel());
        egreso.setEstatusEgreso(EstatusEgreso.CORREGIDO);
        repository.save(egreso);
        return getEgresoById(id);
    }

    @Transactional
    public EgresoDto eliminarEgreso(Integer id, EgresoJustificacionRequest request) {
        Usuario usuario = validarPasswordUsuarioActual(request.getPassword());
        Egreso egreso = findEntity(id);
        if (egreso.getEstatusEgreso() == EstatusEgreso.ELIMINADO) {
            throw new IllegalArgumentException("El egreso ya está eliminado.");
        }
        registrarMovimiento(egreso, TipoMovimientoEgreso.ELIMINACION, EstatusEgreso.ELIMINADO, usuario, request.getJustificacion());
        return getEgresoById(id);
    }

    @Transactional
    public EgresoDto restaurarEgreso(Integer id, EgresoJustificacionRequest request) {
        Usuario usuario = validarPasswordUsuarioActual(request.getPassword());
        Egreso egreso = findEntity(id);
        if (egreso.getEstatusEgreso() != EstatusEgreso.ELIMINADO) {
            throw new IllegalArgumentException("Solo se puede restaurar un egreso eliminado.");
        }
        registrarMovimiento(egreso, TipoMovimientoEgreso.RESTAURACION, EstatusEgreso.RESTAURADO, usuario, request.getJustificacion());
        return getEgresoById(id);
    }

    private void registrarMovimiento(Egreso egreso, TipoMovimientoEgreso tipo, EstatusEgreso nuevoEstatus,
                                     Usuario usuario, String justificacion) {
        movimientoRepository.save(EgresoMovimiento.builder()
                .idEgreso(egreso.getIdEgreso())
                .tipoMovimiento(tipo)
                .fechaHoraMovimiento(LocalDateTime.now())
                .idUsuario(usuario.getIdUsuario())
                .justificacionMovimiento(justificacion.trim())
                .build());
        egreso.setEstatusEgreso(nuevoEstatus);
        repository.save(egreso);
    }

    private Usuario validarPasswordUsuarioActual(String password) {
        String usuarioActual = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsuario(usuarioActual)
                .orElseThrow(() -> new InvalidCredentialsException("Contraseña incorrecta"));
        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new InvalidCredentialsException("Contraseña incorrecta");
        }
        return usuario;
    }

    private Egreso findEntity(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Egreso no encontrado: " + id));
    }

    private static String normalizarObservaciones(String observaciones) {
        return observaciones == null || observaciones.isBlank() ? null : observaciones.trim();
    }

    private Map<Integer, TipoEgresoDto> tiposPorId() {
        return tipoEgresoRepository.findAll().stream()
                .map(tipoEgresoService::toDto)
                .collect(Collectors.toMap(TipoEgresoDto::getIdTipoEgreso, Function.identity()));
    }

    private Map<Integer, PlantelDto> plantelesPorId() {
        return plantelRepository.findAll().stream()
                .map(plantelService::toDto)
                .collect(Collectors.toMap(PlantelDto::getIdPlantel, Function.identity()));
    }

    private Map<Integer, String> nombresUsuario(Collection<Integer> idsUsuario) {
        return usuarioRepository.findAllById(idsUsuario.stream().distinct().toList()).stream()
                .collect(Collectors.toMap(Usuario::getIdUsuario, Usuario::getNombreUsuario));
    }

    private EgresoDto toDto(Egreso egreso, Map<Integer, TipoEgresoDto> tipos, Map<Integer, PlantelDto> planteles,
                            Map<Integer, String> nombresUsuario, EgresoMovimiento ultimoMovimiento) {
        return EgresoDto.builder()
                .idEgreso(egreso.getIdEgreso())
                .tipoEgreso(tipos.get(egreso.getIdTipoEgreso()))
                .importeEgreso(egreso.getImporteEgreso())
                .fechaHoraEgreso(egreso.getFechaHoraEgreso())
                .observacionesEgreso(egreso.getObservacionesEgreso())
                .idUsuario(egreso.getIdUsuario())
                .nombreUsuario(nombresUsuario.get(egreso.getIdUsuario()))
                .plantel(egreso.getIdPlantel() == null ? null : planteles.get(egreso.getIdPlantel()))
                .estatusEgreso(egreso.getEstatusEgreso())
                .fechaRegistroEgreso(egreso.getFechaRegistroEgreso())
                .ultimoMovimiento(ultimoMovimiento == null ? null : toMovimientoDto(ultimoMovimiento, tipos, planteles, nombresUsuario))
                .build();
    }

    private EgresoMovimientoDto toMovimientoDto(EgresoMovimiento movimiento, Map<Integer, TipoEgresoDto> tipos,
                                                Map<Integer, PlantelDto> planteles, Map<Integer, String> nombresUsuario) {
        return EgresoMovimientoDto.builder()
                .idEgresoMovimiento(movimiento.getIdEgresoMovimiento())
                .idEgreso(movimiento.getIdEgreso())
                .tipoMovimiento(movimiento.getTipoMovimiento())
                .fechaHoraMovimiento(movimiento.getFechaHoraMovimiento())
                .idUsuario(movimiento.getIdUsuario())
                .nombreUsuario(nombresUsuario.get(movimiento.getIdUsuario()))
                .justificacionMovimiento(movimiento.getJustificacionMovimiento())
                .tipoEgresoAnterior(movimiento.getIdTipoEgresoAnterior() == null ? null : tipos.get(movimiento.getIdTipoEgresoAnterior()))
                .tipoEgresoNuevo(movimiento.getIdTipoEgresoNuevo() == null ? null : tipos.get(movimiento.getIdTipoEgresoNuevo()))
                .importeAnterior(movimiento.getImporteAnterior())
                .importeNuevo(movimiento.getImporteNuevo())
                .fechaHoraAnterior(movimiento.getFechaHoraAnterior())
                .fechaHoraNueva(movimiento.getFechaHoraNueva())
                .observacionesAnteriores(movimiento.getObservacionesAnteriores())
                .observacionesNuevas(movimiento.getObservacionesNuevas())
                .plantelAnterior(movimiento.getIdPlantelAnterior() == null ? null : planteles.get(movimiento.getIdPlantelAnterior()))
                .plantelNuevo(movimiento.getIdPlantelNuevo() == null ? null : planteles.get(movimiento.getIdPlantelNuevo()))
                .build();
    }
}
