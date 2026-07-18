package com.kyros.demokyros.restcontrollers;

import com.kyros.demokyros.dto.LogDto;
import com.kyros.demokyros.services.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class LogController {

    private final LogService service;

    @GetMapping
    public List<LogDto> getAll() {
        return service.getAllLogs();
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<LogDto> getByUsuario(@PathVariable Integer idUsuario) {
        return service.getLogsByUsuario(idUsuario);
    }
}
