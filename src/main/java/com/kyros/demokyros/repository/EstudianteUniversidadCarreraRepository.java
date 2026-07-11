package com.kyros.demokyros.repository;

import com.kyros.demokyros.entity.EstudianteUniversidadCarrera;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EstudianteUniversidadCarreraRepository extends JpaRepository<EstudianteUniversidadCarrera, Integer> {

    List<EstudianteUniversidadCarrera> findByIdEstudianteUniversidad(Integer idEstudianteUniversidad);

    Optional<EstudianteUniversidadCarrera> findByIdEstudianteUniversidadAndIdCarrera(Integer idEstudianteUniversidad, Integer idCarrera);
}
