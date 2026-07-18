package com.kyros.demokyros.repository;

import com.kyros.demokyros.entity.EstudianteAsesoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstudianteAsesoriaRepository extends JpaRepository<EstudianteAsesoria, Integer> {

    List<EstudianteAsesoria> findByIdEstudiante(Integer idEstudiante);

    List<EstudianteAsesoria> findByIdAsesoria(Integer idAsesoria);
}
