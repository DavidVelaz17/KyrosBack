package com.kyros.demokyros.repository;

import com.kyros.demokyros.entity.EstudianteSecundaria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstudianteSecundariaRepository extends JpaRepository<EstudianteSecundaria, Integer> {

    List<EstudianteSecundaria> findByIdEstudiante(Integer idEstudiante);

    List<EstudianteSecundaria> findByIdSecundaria(Integer idSecundaria);
}
