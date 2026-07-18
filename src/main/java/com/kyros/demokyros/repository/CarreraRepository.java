package com.kyros.demokyros.repository;

import com.kyros.demokyros.entity.Carrera;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarreraRepository extends JpaRepository<Carrera, Integer> {

    List<Carrera> findByIdArea(Integer idArea);
}
