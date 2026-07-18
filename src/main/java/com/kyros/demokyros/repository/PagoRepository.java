package com.kyros.demokyros.repository;

import com.kyros.demokyros.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagoRepository extends JpaRepository<Pago, Integer> {

    List<Pago> findByIdCargo(Integer idCargo);
}
