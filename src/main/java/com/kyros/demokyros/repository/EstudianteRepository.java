package com.kyros.demokyros.repository;

import com.kyros.demokyros.entity.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EstudianteRepository extends JpaRepository<Estudiante, Integer> {

    Optional<Estudiante> findByMatricula(String matricula);

    boolean existsByMatricula(String matricula);

    List<Estudiante> findByIdGrupo(Integer idGrupo);
}
