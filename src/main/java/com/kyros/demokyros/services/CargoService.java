package com.kyros.demokyros.services;

import com.kyros.demokyros.dto.CargoDto;
import com.kyros.demokyros.dto.EstudianteDto;
import com.kyros.demokyros.dto.UsuarioDto;
import com.kyros.demokyros.entity.Cargo;
import com.kyros.demokyros.entity.Pago;
import com.kyros.demokyros.entity.Usuario;
import com.kyros.demokyros.exception.InvalidCredentialsException;
import com.kyros.demokyros.exception.ResourceNotFoundException;
import com.kyros.demokyros.form.CargoEstatusForm;
import com.kyros.demokyros.form.CargoUpdateForm;
import com.kyros.demokyros.form.CargoForm;
import com.kyros.demokyros.repository.CargoRepository;
import com.kyros.demokyros.repository.PagoRepository;
import com.kyros.demokyros.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CargoService {

    private final CargoRepository repository;
    private final PagoRepository pagoRepository;
    private final EstudianteService estudianteService;
    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public List<CargoDto> getAllCargos() {
        return toDtoList(repository.findAll());
    }

    public CargoDto getCargoById(Integer id) {
        Cargo cargo = findEntity(id);
        return toDto(cargo, estudianteService.getEstudianteById(cargo.getIdEstudiante()),
                usuarioService.toDto(usuarioService.findEntity(cargo.getIdUsuario())));
    }

    public List<CargoDto> getCargosByEstudiante(Integer idEstudiante) {
        estudianteService.findEntity(idEstudiante);
        return toDtoList(repository.findByIdEstudiante(idEstudiante));
    }

    public CargoDto createCargo(CargoForm form) {
        estudianteService.findEntity(form.getIdEstudiante());
        usuarioService.findEntity(form.getIdUsuario());
        Cargo cargo = Cargo.builder()
                .tipoMensualidadCargo(form.getTipoMensualidadCargo())
                .conceptoCargo(form.getConceptoCargo())
                .montoTotalCargo(form.getMontoTotalCargo())
                .fechaVencimientoCargo(form.getFechaVencimientoCargo())
                .estatusCargo(form.getEstatusCargo())
                .idEstudiante(form.getIdEstudiante())
                .idUsuario(form.getIdUsuario())
                .build();
        return getCargoById(repository.save(cargo).getIdCargo());
    }

    // Tipo de mensualidad y monto total se pueden corregir desde "Registrar pago" antes de pagar.
    // El monto no puede quedar por debajo de lo ya pagado. El estatus no se toca aquí: lo ajusta
    // el front con updateEstatus al registrar el pago, como siempre.
    public CargoDto updateCargo(Integer id, CargoUpdateForm form) {
        Cargo cargo = findEntity(id);
        double pagado = pagoRepository.findByIdCargo(id).stream().mapToDouble(Pago::getMontoPagadoPago).sum();
        if (form.getMontoTotalCargo() < pagado) {
            throw new IllegalArgumentException(String.format(
                    "El monto total no puede ser menor a lo ya pagado ($%,.2f).", pagado));
        }
        cargo.setTipoMensualidadCargo(form.getTipoMensualidadCargo());
        cargo.setMontoTotalCargo(form.getMontoTotalCargo());
        return getCargoById(repository.save(cargo).getIdCargo());
    }

    // Concepto y fechas son inmutables; el estatus transiciona (ej. PENDIENTE -> PAGADO) a medida
    // que se registran pagos sobre él.
    public CargoDto updateEstatus(Integer id, CargoEstatusForm form) {
        Cargo cargo = findEntity(id);
        cargo.setEstatusCargo(form.getEstatusCargo());
        return getCargoById(repository.save(cargo).getIdCargo());
    }

    // Borrar un cargo es sensible (afecta la trazabilidad de cobros, igual que borrar un pago):
    // restringido a ADMIN en SecurityConfig, y aquí se revalida la contraseña del usuario
    // autenticado, igual que PagoService.deletePago. Si el cargo tiene pagos asociados, la FK
    // fk_pago_cargo (ON DELETE RESTRICT) rechaza el borrado y GlobalExceptionHandler lo traduce
    // a un 409: hay que borrar esos pagos primero.
    public void deleteCargo(Integer id, String password) {
        Cargo cargo = findEntity(id);
        String usuarioActual = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsuario(usuarioActual)
                .orElseThrow(() -> new InvalidCredentialsException("Contraseña incorrecta"));
        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new InvalidCredentialsException("Contraseña incorrecta");
        }
        repository.delete(cargo);
    }

    private List<CargoDto> toDtoList(List<Cargo> cargos) {
        Map<Integer, EstudianteDto> estudianteDtoMap = cargos.stream()
                .map(Cargo::getIdEstudiante)
                .distinct()
                .collect(Collectors.toMap(id -> id, estudianteService::getEstudianteById));
        Map<Integer, UsuarioDto> usuarioDtoMap = cargos.stream()
                .map(Cargo::getIdUsuario)
                .distinct()
                .collect(Collectors.toMap(id -> id, id -> usuarioService.toDto(usuarioService.findEntity(id))));
        return cargos.stream()
                .map(cargo -> toDto(cargo, estudianteDtoMap.get(cargo.getIdEstudiante()), usuarioDtoMap.get(cargo.getIdUsuario())))
                .toList();
    }

    Cargo findEntity(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cargo no encontrado: " + id));
    }

    CargoDto toDto(Cargo cargo, EstudianteDto estudianteDto, UsuarioDto usuarioDto) {
        return CargoDto.builder()
                .idCargo(cargo.getIdCargo())
                .tipoMensualidadCargo(cargo.getTipoMensualidadCargo())
                .conceptoCargo(cargo.getConceptoCargo())
                .montoTotalCargo(cargo.getMontoTotalCargo())
                .fechaVencimientoCargo(cargo.getFechaVencimientoCargo())
                .estatusCargo(cargo.getEstatusCargo())
                .estudiante(estudianteDto)
                .usuario(usuarioDto)
                .build();
    }
}
