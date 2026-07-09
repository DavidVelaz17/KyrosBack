package com.kyros.demokyros.services;

import com.kyros.demokyros.dto.LogDto;
import com.kyros.demokyros.dto.UsuarioDto;
import com.kyros.demokyros.entity.Log;
import com.kyros.demokyros.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository repository;
    private final UsuarioService usuarioService;

    public List<LogDto> getAllLogs() {
        return toDtoList(repository.findAll());
    }

    public List<LogDto> getLogsByUsuario(Integer idUsuario) {
        usuarioService.findEntity(idUsuario);
        return toDtoList(repository.findByIdUsuario(idUsuario));
    }

    private List<LogDto> toDtoList(List<Log> logs) {
        Map<Integer, UsuarioDto> usuarioDtoMap = logs.stream()
                .map(Log::getIdUsuario)
                .distinct()
                .collect(Collectors.toMap(id -> id, id -> usuarioService.toDto(usuarioService.findEntity(id))));
        return logs.stream()
                .map(log -> LogDto.builder()
                        .idLog(log.getIdLog())
                        .timeStamp(log.getTimeStamp())
                        .usuario(usuarioDtoMap.get(log.getIdUsuario()))
                        .build())
                .toList();
    }
}
