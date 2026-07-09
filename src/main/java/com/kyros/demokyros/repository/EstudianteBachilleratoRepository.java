package com.kyros.demokyros.repository;

import com.kyros.demokyros.entity.EstudianteBachillerato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstudianteBachilleratoRepository extends JpaRepository<EstudianteBachillerato, Integer> {

    List<EstudianteBachillerato> findByIdEstudiante(Integer idEstudiante);

    List<EstudianteBachillerato> findByIdBachillerato(Integer idBachillerato);
}
