package com.kyros.demokyros.repository;

import com.kyros.demokyros.entity.CarreraUniversidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarreraUniversidadRepository extends JpaRepository<CarreraUniversidad, Integer> {

    List<CarreraUniversidad> findByIdCarrera(Integer idCarrera);

    List<CarreraUniversidad> findByIdUniversidad(Integer idUniversidad);
}
