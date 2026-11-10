package com.kyros.demokyros.repository;

import com.kyros.demokyros.entity.EgresoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EgresoMovimientoRepository extends JpaRepository<EgresoMovimiento, Integer> {

    List<EgresoMovimiento> findByIdEgresoOrderByIdEgresoMovimientoAsc(Integer idEgreso);

    /** El movimiento más reciente de cada egreso que tenga alguno (el id autoincremental sigue el
     *  orden en que se guardaron), para mostrar quién y cuándo en la tabla sin cargar todo el historial. */
    @Query("SELECT m FROM EgresoMovimiento m WHERE m.idEgresoMovimiento IN "
            + "(SELECT MAX(m2.idEgresoMovimiento) FROM EgresoMovimiento m2 GROUP BY m2.idEgreso)")
    List<EgresoMovimiento> findUltimoDeCadaEgreso();

    Optional<EgresoMovimiento> findFirstByIdEgresoOrderByIdEgresoMovimientoDesc(Integer idEgreso);
}
