package com.kyros.demokyros.repository;

import com.kyros.demokyros.entity.EstudianteCursoVerano;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstudianteCursoVeranoRepository extends JpaRepository<EstudianteCursoVerano, Integer> {

    List<EstudianteCursoVerano> findByIdEstudiante(Integer idEstudiante);

    List<EstudianteCursoVerano> findByIdCursoVerano(Integer idCursoVerano);
}
