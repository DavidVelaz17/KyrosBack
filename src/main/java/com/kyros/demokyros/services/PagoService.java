package com.kyros.demokyros.services;

import com.kyros.demokyros.dto.CargoDto;
import com.kyros.demokyros.dto.PagoDto;
import com.kyros.demokyros.entity.Pago;
import com.kyros.demokyros.exception.ResourceNotFoundException;
import com.kyros.demokyros.form.PagoForm;
import com.kyros.demokyros.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PagoService {

    private final PagoRepository repository;
    private final CargoService cargoService;

    public List<PagoDto> getAllPagos() {
        return toDtoList(repository.findAll());
    }

    public PagoDto getPagoById(Integer id) {
        Pago pago = findEntity(id);
        return toDto(pago, cargoService.getCargoById(pago.getIdCargo()));
    }

    public List<PagoDto> getPagosByCargo(Integer idCargo) {
        cargoService.findEntity(idCargo);
        return toDtoList(repository.findByIdCargo(idCargo));
    }

    public PagoDto createPago(PagoForm form) {
        cargoService.findEntity(form.getIdCargo());
        Pago pago = Pago.builder()
                .montoPagadoPago(form.getMontoPagadoPago())
                .fechaPago(form.getFechaPago())
                .metodoPago(form.getMetodoPago())
                .idCargo(form.getIdCargo())
                .build();
        return getPagoById(repository.save(pago).getIdPago());
    }

    private List<PagoDto> toDtoList(List<Pago> pagos) {
        Map<Integer, CargoDto> cargoDtoMap = pagos.stream()
                .map(Pago::getIdCargo)
                .distinct()
                .collect(Collectors.toMap(id -> id, cargoService::getCargoById));
        return pagos.stream()
                .map(pago -> toDto(pago, cargoDtoMap.get(pago.getIdCargo())))
                .toList();
    }

    private Pago findEntity(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado: " + id));
    }

    private PagoDto toDto(Pago pago, CargoDto cargoDto) {
        return PagoDto.builder()
                .idPago(pago.getIdPago())
                .montoPagadoPago(pago.getMontoPagadoPago())
                .fechaPago(pago.getFechaPago())
                .metodoPago(pago.getMetodoPago())
                .cargo(cargoDto)
                .build();
    }
}
