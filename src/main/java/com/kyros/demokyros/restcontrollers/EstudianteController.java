package com.kyros.demokyros.restcontrollers;

import com.kyros.demokyros.dto.EstudianteDestinoDto;
import com.kyros.demokyros.dto.EstudianteDto;
import com.kyros.demokyros.dto.ImportEstudiantesResultDto;
import com.kyros.demokyros.form.EstudianteEstatusForm;
import com.kyros.demokyros.form.EstudianteForm;
import com.kyros.demokyros.services.EstudianteImportExportService;
import com.kyros.demokyros.services.EstudianteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/estudiantes")
@RequiredArgsConstructor
public class EstudianteController {

    private static final MediaType XLSX_MEDIA_TYPE =
            MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    private final EstudianteService service;
    private final EstudianteImportExportService importExportService;

    @GetMapping
    public List<EstudianteDto> getAll() {
        return service.getAllEstudiantes();
    }

    @GetMapping("/{id}")
    public EstudianteDto getById(@PathVariable Integer id) {
        return service.getEstudianteById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EstudianteDto create(@Valid @RequestBody EstudianteForm form) {
        return service.createEstudiante(form);
    }

    @PutMapping("/{id}")
    public EstudianteDto update(@PathVariable Integer id, @Valid @RequestBody EstudianteForm form) {
        return service.updateEstudiante(id, form);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.deleteEstudiante(id);
    }

    @PutMapping("/{id}/estatus")
    public EstudianteDto updateEstatus(@PathVariable Integer id, @Valid @RequestBody EstudianteEstatusForm form) {
        return service.updateEstatus(id, form);
    }

    @PostMapping(path = "/{id}/foto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public EstudianteDto actualizarFoto(@PathVariable Integer id, @RequestParam("file") MultipartFile file) {
        return service.actualizarFoto(id, file);
    }

    // idDestino se resuelve contra la tabla puente correcta según el ingresoA del estudiante
    @GetMapping("/{id}/destinos")
    public List<EstudianteDestinoDto> getDestinos(@PathVariable Integer id) {
        return service.getDestinos(id);
    }

    // idCarrera sólo aplica cuando el ingresoA del estudiante es UNIVERSIDAD; se ignora en los demás casos
    @PostMapping("/{id}/destinos/{idDestino}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addDestino(
            @PathVariable Integer id,
            @PathVariable Integer idDestino,
            @RequestParam(required = false) Integer idCarrera
    ) {
        service.addDestino(id, idDestino, idCarrera);
    }

    @DeleteMapping("/{id}/destinos/{idDestino}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeDestino(@PathVariable Integer id, @PathVariable Integer idDestino) {
        service.removeDestino(id, idDestino);
    }

    // Alta masiva desde un Excel; una fila inválida no aborta el lote, se reporta como error.
    @PostMapping(path = "/importar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ImportEstudiantesResultDto importar(@RequestParam("file") MultipartFile file) {
        return importExportService.importarEstudiantes(file);
    }

    @GetMapping("/exportar")
    public ResponseEntity<byte[]> exportar() {
        return archivoExcel(importExportService.exportarEstudiantes(), "alumnos.xlsx");
    }

    @GetMapping("/plantilla")
    public ResponseEntity<byte[]> plantilla() {
        return archivoExcel(importExportService.plantillaEstudiantes(), "plantilla-alumnos.xlsx");
    }

    private ResponseEntity<byte[]> archivoExcel(byte[] contenido, String filename) {
        return ResponseEntity.ok()
                .contentType(XLSX_MEDIA_TYPE)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(filename).build().toString())
                .body(contenido);
    }
}
