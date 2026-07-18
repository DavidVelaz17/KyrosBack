package com.kyros.demokyros.repository;

import com.kyros.demokyros.entity.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CargoRepository extends JpaRepository<Cargo, Integer> {

    List<Cargo> findByIdEstudiante(Integer idEstudiante);

    List<Cargo> findByIdUsuario(Integer idUsuario);
}
