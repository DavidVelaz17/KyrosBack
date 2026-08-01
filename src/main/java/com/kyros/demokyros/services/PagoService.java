package com.kyros.demokyros.services;

import com.kyros.demokyros.dto.CargoDto;
import com.kyros.demokyros.dto.PagoDto;
import com.kyros.demokyros.dto.UsuarioDto;
import com.kyros.demokyros.entity.Pago;
import com.kyros.demokyros.entity.Usuario;
import com.kyros.demokyros.exception.InvalidCredentialsException;
import com.kyros.demokyros.exception.ResourceNotFoundException;
import com.kyros.demokyros.form.PagoForm;
import com.kyros.demokyros.repository.PagoRepository;
import com.kyros.demokyros.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PagoService {

    private final PagoRepository repository;
    private final CargoService cargoService;
    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public List<PagoDto> getAllPagos() {
        return toDtoList(repository.findAll());
    }

    public PagoDto getPagoById(Integer id) {
        Pago pago = findEntity(id);
        return toDto(pago, cargoService.getCargoById(pago.getIdCargo()), resolveUsuario(pago));
    }

    public List<PagoDto> getPagosByCargo(Integer idCargo) {
        cargoService.findEntity(idCargo);
        return toDtoList(repository.findByIdCargo(idCargo));
    }

    public PagoDto createPago(PagoForm form) {
        cargoService.findEntity(form.getIdCargo());
        usuarioService.findEntity(form.getIdUsuario());
        Pago pago = Pago.builder()
                .montoPagadoPago(form.getMontoPagadoPago())
                .fechaPago(form.getFechaPago())
                .metodoPago(form.getMetodoPago())
                .idCargo(form.getIdCargo())
                .idUsuario(form.getIdUsuario())
                .requiereFactura(form.getRequiereFactura())
                .conceptoPago(form.getConceptoPago())
                .notasPago(form.getNotasPago())
                .build();
        return getPagoById(repository.save(pago).getIdPago());
    }

    // Borrar un pago es una operación sensible (rompe la trazabilidad de cobros): además de estar
    // restringida a ADMIN en SecurityConfig, se re-valida la contraseña del usuario autenticado
    // aquí, igual que hace AuthService.login.
    public void deletePago(Integer id, String password) {
        Pago pago = findEntity(id);
        String usuarioActual = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsuario(usuarioActual)
                .orElseThrow(() -> new InvalidCredentialsException("Contraseña incorrecta"));
        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new InvalidCredentialsException("Contraseña incorrecta");
        }
        repository.delete(pago);
    }

    private List<PagoDto> toDtoList(List<Pago> pagos) {
        Map<Integer, CargoDto> cargoDtoMap = pagos.stream()
                .map(Pago::getIdCargo)
                .distinct()
                .collect(Collectors.toMap(id -> id, cargoService::getCargoById));
        Map<Integer, UsuarioDto> usuarioDtoMap = pagos.stream()
                .map(Pago::getIdUsuario)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toMap(id -> id, id -> usuarioService.toDto(usuarioService.findEntity(id))));
        return pagos.stream()
                .map(pago -> toDto(pago, cargoDtoMap.get(pago.getIdCargo()), usuarioDtoMap.get(pago.getIdUsuario())))
                .toList();
    }

    private UsuarioDto resolveUsuario(Pago pago) {
        return pago.getIdUsuario() == null ? null : usuarioService.toDto(usuarioService.findEntity(pago.getIdUsuario()));
    }

    private Pago findEntity(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado: " + id));
    }

    private PagoDto toDto(Pago pago, CargoDto cargoDto, UsuarioDto usuarioDto) {
        return PagoDto.builder()
                .idPago(pago.getIdPago())
                .montoPagadoPago(pago.getMontoPagadoPago())
                .fechaPago(pago.getFechaPago())
                .metodoPago(pago.getMetodoPago())
                .cargo(cargoDto)
                .usuario(usuarioDto)
                .requiereFactura(pago.getRequiereFactura())
                .conceptoPago(pago.getConceptoPago())
                .notasPago(pago.getNotasPago())
                .build();
    }
}
