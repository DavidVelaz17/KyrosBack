package com.kyros.demokyros.services;

import com.kyros.demokyros.dto.EgresoDto;
import com.kyros.demokyros.dto.TipoEgresoDto;
import com.kyros.demokyros.entity.Egreso;
import com.kyros.demokyros.entity.Usuario;
import com.kyros.demokyros.exception.ResourceNotFoundException;
import com.kyros.demokyros.form.EgresoForm;
import com.kyros.demokyros.repository.EgresoRepository;
import com.kyros.demokyros.repository.TipoEgresoRepository;
import com.kyros.demokyros.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EgresoService {

    private final EgresoRepository repository;
    private final TipoEgresoRepository tipoEgresoRepository;
    private final TipoEgresoService tipoEgresoService;
    private final UsuarioRepository usuarioRepository;

    public List<EgresoDto> getAllEgresos() {
        List<Egreso> egresos = repository.findAll();
        // Una consulta por tabla relacionada en vez de una por egreso.
        Map<Integer, TipoEgresoDto> tipos = tipoEgresoRepository.findAll().stream()
                .map(tipoEgresoService::toDto)
                .collect(Collectors.toMap(TipoEgresoDto::getIdTipoEgreso, Function.identity()));
        Map<Integer, String> nombresUsuario = usuarioRepository
                .findAllById(egresos.stream().map(Egreso::getIdUsuario).distinct().toList())
                .stream()
                .collect(Collectors.toMap(Usuario::getIdUsuario, Usuario::getNombreUsuario));
        return egresos.stream()
                .map(egreso -> toDto(egreso, tipos.get(egreso.getIdTipoEgreso()), nombresUsuario.get(egreso.getIdUsuario())))
                .toList();
    }

    public EgresoDto getEgresoById(Integer id) {
        Egreso egreso = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Egreso no encontrado: " + id));
        String nombreUsuario = usuarioRepository.findById(egreso.getIdUsuario()).map(Usuario::getNombreUsuario).orElse(null);
        return toDto(egreso, tipoEgresoService.getTipoEgresoById(egreso.getIdTipoEgreso()), nombreUsuario);
    }

    // El usuario que registra se toma de la sesión autenticada (el JWT validado por Spring Security),
    // no del cuerpo de la petición, para que nadie pueda registrar un egreso a nombre de otro.
    public EgresoDto createEgreso(EgresoForm form) {
        tipoEgresoService.findEntity(form.getIdTipoEgreso());
        String usuarioActual = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsuario(usuarioActual)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + usuarioActual));
        String observaciones = form.getObservacionesEgreso() == null || form.getObservacionesEgreso().isBlank()
                ? null
                : form.getObservacionesEgreso().trim();
        Egreso egreso = Egreso.builder()
                .idTipoEgreso(form.getIdTipoEgreso())
                .importeEgreso(form.getImporteEgreso())
                .fechaHoraEgreso(form.getFechaHoraEgreso())
                .observacionesEgreso(observaciones)
                .idUsuario(usuario.getIdUsuario())
                .build();
        return getEgresoById(repository.save(egreso).getIdEgreso());
    }

    private EgresoDto toDto(Egreso egreso, TipoEgresoDto tipoEgreso, String nombreUsuario) {
        return EgresoDto.builder()
                .idEgreso(egreso.getIdEgreso())
                .tipoEgreso(tipoEgreso)
                .importeEgreso(egreso.getImporteEgreso())
                .fechaHoraEgreso(egreso.getFechaHoraEgreso())
                .observacionesEgreso(egreso.getObservacionesEgreso())
                .idUsuario(egreso.getIdUsuario())
                .nombreUsuario(nombreUsuario)
                .build();
    }
}
