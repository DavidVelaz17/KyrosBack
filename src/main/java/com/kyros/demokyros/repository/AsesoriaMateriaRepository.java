package com.kyros.demokyros.repository;

import com.kyros.demokyros.entity.AsesoriaMateria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AsesoriaMateriaRepository extends JpaRepository<AsesoriaMateria, Integer> {

    List<AsesoriaMateria> findByIdAsesoria(Integer idAsesoria);

    List<AsesoriaMateria> findByIdMateria(Integer idMateria);
}
