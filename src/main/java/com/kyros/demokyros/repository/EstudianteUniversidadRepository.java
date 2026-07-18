package com.kyros.demokyros.repository;

import com.kyros.demokyros.entity.EstudianteUniversidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstudianteUniversidadRepository extends JpaRepository<EstudianteUniversidad, Integer> {

    List<EstudianteUniversidad> findByIdEstudiante(Integer idEstudiante);

    List<EstudianteUniversidad> findByIdUniversidad(Integer idUniversidad);
}
