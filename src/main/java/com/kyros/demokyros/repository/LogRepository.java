package com.kyros.demokyros.repository;

import com.kyros.demokyros.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogRepository extends JpaRepository<Log, Integer> {

    List<Log> findByIdUsuario(Integer idUsuario);
}
