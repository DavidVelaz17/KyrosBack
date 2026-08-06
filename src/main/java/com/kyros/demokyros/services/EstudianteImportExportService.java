package com.kyros.demokyros.services;

import com.kyros.demokyros.dto.ImportEstudianteErrorDto;
import com.kyros.demokyros.dto.ImportEstudiantesResultDto;
import com.kyros.demokyros.entity.Asesoria;
import com.kyros.demokyros.entity.Bachillerato;
import com.kyros.demokyros.entity.Cargo;
import com.kyros.demokyros.entity.Carrera;
import com.kyros.demokyros.entity.CursoVerano;
import com.kyros.demokyros.entity.Estudiante;
import com.kyros.demokyros.entity.EstudianteAsesoria;
import com.kyros.demokyros.entity.EstudianteBachillerato;
import com.kyros.demokyros.entity.EstudianteCursoVerano;
import com.kyros.demokyros.entity.EstudianteSecundaria;
import com.kyros.demokyros.entity.EstudianteUniversidad;
import com.kyros.demokyros.entity.EstudianteUniversidadCarrera;
import com.kyros.demokyros.entity.Grupo;
import com.kyros.demokyros.entity.Pago;
import com.kyros.demokyros.entity.Secundaria;
import com.kyros.demokyros.entity.Universidad;
import com.kyros.demokyros.entity.Usuario;
import com.kyros.demokyros.enums.Horario;
import com.kyros.demokyros.enums.IngresoA;
import com.kyros.demokyros.form.EstudianteForm;
import com.kyros.demokyros.repository.AsesoriaRepository;
import com.kyros.demokyros.repository.BachilleratoRepository;
import com.kyros.demokyros.repository.CargoRepository;
import com.kyros.demokyros.repository.CarreraRepository;
import com.kyros.demokyros.repository.CursoVeranoRepository;
import com.kyros.demokyros.repository.EstudianteAsesoriaRepository;
import com.kyros.demokyros.repository.EstudianteBachilleratoRepository;
import com.kyros.demokyros.repository.EstudianteCursoVeranoRepository;
import com.kyros.demokyros.repository.EstudianteRepository;
import com.kyros.demokyros.repository.EstudianteSecundariaRepository;
import com.kyros.demokyros.repository.EstudianteUniversidadCarreraRepository;
import com.kyros.demokyros.repository.EstudianteUniversidadRepository;
import com.kyros.demokyros.repository.GrupoRepository;
import com.kyros.demokyros.repository.PagoRepository;
import com.kyros.demokyros.repository.SecundariaRepository;
import com.kyros.demokyros.repository.UniversidadRepository;
import com.kyros.demokyros.repository.UsuarioRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/** Carga y descarga masiva de alumnos vía Excel. Separado de EstudianteService (que se
 *  reutiliza para persistir cada fila válida) para no inflarlo con la lógica de lectura/
 *  escritura de POI, que no tiene nada que ver con las operaciones normales de un alumno. */
@Service
@RequiredArgsConstructor
public class EstudianteImportExportService {

    // Explícito y no la del sistema (TZ del contenedor): el servidor corre en Alemania, y el año
    // de folio de la matrícula debe reflejar la fecha en México, no la de Berlín.
    private static final ZoneId ZONA_MEXICO = ZoneId.of("America/Mexico_City");

    private static final String[] ENCABEZADOS_ALUMNOS = {
            "Nombre(s)", "Apellido paterno", "Apellido materno", "Edad", "Teléfono",
            "Escuela de procedencia", "Grado escolar", "Nombre del tutor", "Número del tutor",
            "Dirección", "Fecha de inscripción", "Horario", "Ingresa a", "Lleva inglés", "Notas"
    };

    private static final Map<IngresoA, String> PREFIJO_MATRICULA = Map.of(
            IngresoA.UNIVERSIDAD, "U",
            IngresoA.BACHILLERATO, "B",
            IngresoA.SECUNDARIA, "S",
            IngresoA.ASESORIAS, "A",
            IngresoA.CURSO_VERANO, "V"
    );

    // La matrícula es NOT NULL + UNIQUE en la base de datos, así que siempre se genera una,
    // incluso si la fila no trae "Ingresa a" (ese campo ya es opcional).
    private static final String PREFIJO_SIN_INGRESO_A = "X";

    // Hasta qué fila (0-indexada, sin contar el encabezado) se aplica el desplegable de las
    // columnas de opciones fijas; suficiente margen para cualquier carga masiva razonable.
    private static final int ULTIMA_FILA_VALIDACION = 2000;

    private final EstudianteService estudianteService;
    private final EstudianteRepository estudianteRepository;
    private final GrupoRepository grupoRepository;
    private final CargoRepository cargoRepository;
    private final PagoRepository pagoRepository;
    private final UsuarioRepository usuarioRepository;
    private final UniversidadRepository universidadRepository;
    private final CarreraRepository carreraRepository;
    private final BachilleratoRepository bachilleratoRepository;
    private final SecundariaRepository secundariaRepository;
    private final CursoVeranoRepository cursoVeranoRepository;
    private final AsesoriaRepository asesoriaRepository;
    private final EstudianteUniversidadRepository estudianteUniversidadRepository;
    private final EstudianteUniversidadCarreraRepository estudianteUniversidadCarreraRepository;
    private final EstudianteBachilleratoRepository estudianteBachilleratoRepository;
    private final EstudianteSecundariaRepository estudianteSecundariaRepository;
    private final EstudianteCursoVeranoRepository estudianteCursoVeranoRepository;
    private final EstudianteAsesoriaRepository estudianteAsesoriaRepository;
    private final Validator validator;

    // ---- Importar ----

    public ImportEstudiantesResultDto importarEstudiantes(MultipartFile file, Integer idGrupo) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("El archivo Excel es obligatorio");
        }
        if (idGrupo != null) {
            // Se valida una sola vez, antes de procesar filas: si el grupo no existe, todo el
            // lote es inválido (todas las filas se asignarían al mismo grupo inexistente).
            grupoRepository.findById(idGrupo)
                    .orElseThrow(() -> new IllegalArgumentException("El grupo indicado no existe"));
        }

        List<ImportEstudianteErrorDto> errores = new ArrayList<>();
        int totalFilas = 0;
        int exitosos = 0;
        int year = LocalDate.now(ZONA_MEXICO).getYear();
        List<String> matriculasExistentes = estudianteRepository.findAll().stream()
                .map(Estudiante::getMatricula)
                .toList();
        Map<String, Integer> siguienteSecuenciaPorPrefijo = new HashMap<>();

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null || esFilaVacia(row)) {
                    continue;
                }
                totalFilas++;
                int numeroFila = rowIndex + 1;
                try {
                    EstudianteForm form = leerFila(row, year, matriculasExistentes, siguienteSecuenciaPorPrefijo, idGrupo);
                    var violaciones = validator.validate(form);
                    if (!violaciones.isEmpty()) {
                        String mensaje = violaciones.stream()
                                .map(ConstraintViolation::getMessage)
                                .collect(Collectors.joining("; "));
                        errores.add(ImportEstudianteErrorDto.builder().fila(numeroFila).mensaje(mensaje).build());
                        continue;
                    }
                    estudianteService.createEstudiante(form);
                    exitosos++;
                } catch (Exception e) {
                    errores.add(ImportEstudianteErrorDto.builder().fila(numeroFila).mensaje(e.getMessage()).build());
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo leer el archivo Excel", e);
        }

        return ImportEstudiantesResultDto.builder()
                .totalFilas(totalFilas)
                .exitosos(exitosos)
                .errores(errores)
                .build();
    }

    private EstudianteForm leerFila(
            Row row, int year, List<String> matriculasExistentes, Map<String, Integer> siguienteSecuenciaPorPrefijo,
            Integer idGrupo
    ) {
        String nombre = leerTexto(row, 0);
        String apellidoPaterno = leerTexto(row, 1);
        String apellidoMaterno = leerTexto(row, 2);
        Short edad = leerEntero(row, 3, "Edad");
        Long telefono = leerEnteroLargo(row, 4, "Teléfono");
        String escuelaProcedencia = leerTexto(row, 5);
        String gradoEscolar = leerTexto(row, 6);
        String tutorNombre = leerTexto(row, 7);
        String tutorTelefono = leerTexto(row, 8);
        String direccion = leerTexto(row, 9);
        LocalDate fechaInscripcion = leerFecha(row, 10);
        Horario horario = parseHorario(leerTexto(row, 11));
        IngresoA ingresoA = parseIngresoA(leerTexto(row, 12));
        boolean llevaIngles = parseBooleano(leerTexto(row, 13));
        String notas = leerTexto(row, 14);

        String matricula = generarMatricula(ingresoA, year, matriculasExistentes, siguienteSecuenciaPorPrefijo);

        return EstudianteForm.builder()
                .matricula(matricula)
                .nombre(nombre)
                .apellidoPaterno(apellidoPaterno)
                .apellidoMaterno(apellidoMaterno)
                .edad(edad)
                .numeroTelefonico(telefono)
                .escuelaProcedencia(escuelaProcedencia)
                .gradoEscolar(gradoEscolar)
                .nombreTutor(tutorNombre)
                .telefonoTutor(tutorTelefono)
                .direccion(direccion)
                .notas(notas)
                .fechaInscripcion(fechaInscripcion)
                .horario(horario)
                .ingresoA(ingresoA)
                .llevaIngles(llevaIngles)
                .idGrupo(idGrupo)
                .build();
    }

    /** Genera una matrícula como "U2026003": prefijo por ingresoA + año + folio de 3 dígitos.
     *  Misma lógica que suggestMatricula/nextSequenceForPrefix en el frontend
     *  (lib/utils/matricula.ts), replicada aquí porque la carga masiva se procesa enteramente
     *  en el backend. El folio se calcula una vez por prefijo+año a partir de las matrículas ya
     *  existentes y luego se incrementa en memoria, para que varias filas nuevas del mismo lote
     *  no choquen entre sí. */
    private String generarMatricula(
            IngresoA ingresoA, int year, List<String> matriculasExistentes, Map<String, Integer> siguienteSecuenciaPorPrefijo
    ) {
        String prefijo = ingresoA == null ? PREFIJO_SIN_INGRESO_A : PREFIJO_MATRICULA.get(ingresoA);
        String llave = prefijo + year;
        int secuencia = siguienteSecuenciaPorPrefijo.computeIfAbsent(
                llave, k -> calcularSiguienteSecuencia(matriculasExistentes, prefijo, year)
        );
        siguienteSecuenciaPorPrefijo.put(llave, secuencia + 1);
        return prefijo + year + String.format("%03d", secuencia);
    }

    private int calcularSiguienteSecuencia(List<String> matriculasExistentes, String prefijo, int year) {
        Pattern pattern = Pattern.compile("^" + Pattern.quote(prefijo + year) + "(\\d{3})$");
        return matriculasExistentes.stream()
                .map(pattern::matcher)
                .filter(Matcher::matches)
                .map(m -> Integer.parseInt(m.group(1)))
                .max(Integer::compareTo)
                .map(max -> max + 1)
                .orElse(1);
    }

    private Horario parseHorario(String texto) {
        if (texto == null) {
            return null;
        }
        return switch (normalizar(texto)) {
            case "escolarizado" -> Horario.ESCOLARIZADO;
            case "sabatino" -> Horario.SABATINO;
            case "virtual" -> Horario.VIRTUAL;
            default -> throw new IllegalArgumentException(
                    "Horario no reconocido: '" + texto + "' (usa Escolarizado, Sabatino o Virtual)");
        };
    }

    private IngresoA parseIngresoA(String texto) {
        if (texto == null) {
            return null;
        }
        return switch (normalizar(texto)) {
            case "universidad" -> IngresoA.UNIVERSIDAD;
            case "bachillerato" -> IngresoA.BACHILLERATO;
            case "secundaria" -> IngresoA.SECUNDARIA;
            case "asesorias" -> IngresoA.ASESORIAS;
            case "curso de verano" -> IngresoA.CURSO_VERANO;
            default -> throw new IllegalArgumentException(
                    "Ingresa a no reconocido: '" + texto
                            + "' (usa Universidad, Bachillerato, Secundaria, Asesorías o Curso de verano)");
        };
    }

    private boolean parseBooleano(String texto) {
        if (texto == null) {
            return false;
        }
        return switch (normalizar(texto)) {
            case "si" -> true;
            case "no" -> false;
            default -> throw new IllegalArgumentException(
                    "Lleva inglés no reconocido: '" + texto + "' (usa Sí o No)");
        };
    }

    private String normalizar(String texto) {
        String sinAcentos = Normalizer.normalize(texto.trim().toLowerCase(Locale.ROOT), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return sinAcentos;
    }

    private boolean esFilaVacia(Row row) {
        for (int i = 0; i < ENCABEZADOS_ALUMNOS.length; i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    private String leerTexto(Row row, int indice) {
        Cell cell = row.getCell(indice);
        if (cell == null) {
            return null;
        }
        String valor = switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> {
                double numero = cell.getNumericCellValue();
                yield numero == Math.floor(numero) ? String.valueOf((long) numero) : String.valueOf(numero);
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> null;
        };
        if (valor == null) {
            return null;
        }
        String trimmed = valor.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private Short leerEntero(Row row, int indice, String nombreCampo) {
        String texto = leerTexto(row, indice);
        if (texto == null) {
            return null;
        }
        try {
            return Short.parseShort(texto.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(nombreCampo + " inválido: '" + texto + "'");
        }
    }

    private Long leerEnteroLargo(Row row, int indice, String nombreCampo) {
        String texto = leerTexto(row, indice);
        if (texto == null) {
            return null;
        }
        try {
            return Long.parseLong(texto.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(nombreCampo + " inválido: '" + texto + "'");
        }
    }

    private LocalDate leerFecha(Row row, int indice) {
        Cell cell = row.getCell(indice);
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return null;
        }
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        }
        String texto = leerTexto(row, indice);
        if (texto == null) {
            return null;
        }
        try {
            return LocalDate.parse(texto.trim());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Fecha de inscripción inválida: '" + texto
                            + "' (usa una celda con formato de fecha, o el formato AAAA-MM-DD)");
        }
    }

    // ---- Exportar / plantilla ----

    public byte[] exportarEstudiantes() {
        try (Workbook workbook = new XSSFWorkbook()) {
            CellStyle estiloFecha = crearEstiloFecha(workbook);
            List<Estudiante> estudiantes = estudianteRepository.findAll();
            Map<Integer, Grupo> gruposPorId = grupoRepository.findAll().stream()
                    .collect(Collectors.toMap(Grupo::getIdGrupo, g -> g));
            Map<Integer, String> matriculaPorIdEstudiante = estudiantes.stream()
                    .collect(Collectors.toMap(Estudiante::getIdEstudiante, Estudiante::getMatricula));

            escribirHojaAlumnos(workbook, estudiantes, gruposPorId, true, estiloFecha);
            escribirHojaCargos(workbook, matriculaPorIdEstudiante, estiloFecha);
            escribirHojaPagos(workbook, matriculaPorIdEstudiante, estiloFecha);
            escribirHojaDestinos(workbook, matriculaPorIdEstudiante);

            return aBytes(workbook);
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo generar el archivo Excel", e);
        }
    }

    public byte[] plantillaEstudiantes() {
        try (Workbook workbook = new XSSFWorkbook()) {
            escribirHojaAlumnos(workbook, List.of(), Map.of(), false, crearEstiloFecha(workbook));
            return aBytes(workbook);
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo generar la plantilla", e);
        }
    }

    private CellStyle crearEstiloFecha(Workbook workbook) {
        CellStyle estilo = workbook.createCellStyle();
        estilo.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("yyyy-mm-dd"));
        return estilo;
    }

    private byte[] aBytes(Workbook workbook) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            workbook.write(out);
            return out.toByteArray();
        }
    }

    private void escribirHojaAlumnos(
            Workbook workbook, List<Estudiante> estudiantes, Map<Integer, Grupo> gruposPorId,
            boolean incluirMatriculaYEstatus, CellStyle estiloFecha
    ) {
        Sheet sheet = workbook.createSheet("Alumnos");
        List<String> encabezados = new ArrayList<>();
        if (incluirMatriculaYEstatus) {
            encabezados.add("Matrícula");
        }
        encabezados.addAll(List.of(ENCABEZADOS_ALUMNOS));
        if (incluirMatriculaYEstatus) {
            encabezados.add("Grupo");
            encabezados.add("Estatus");
        }
        escribirEncabezado(sheet, workbook, encabezados.toArray(new String[0]));
        aplicarValidacionesColumnasFijas(sheet, encabezados);

        int rowIndex = 1;
        for (Estudiante estudiante : estudiantes) {
            Row row = sheet.createRow(rowIndex++);
            int col = 0;
            if (incluirMatriculaYEstatus) {
                setCelda(row, col++, estudiante.getMatricula());
            }
            setCelda(row, col++, estudiante.getNombre());
            setCelda(row, col++, estudiante.getApellidoPaterno());
            setCelda(row, col++, estudiante.getApellidoMaterno());
            setCeldaNumero(row, col++, estudiante.getEdad());
            setCeldaNumero(row, col++, estudiante.getNumeroTelefonico());
            setCelda(row, col++, estudiante.getEscuelaProcedencia());
            setCelda(row, col++, estudiante.getGradoEscolar());
            setCelda(row, col++, estudiante.getNombreTutor());
            setCelda(row, col++, estudiante.getTelefonoTutor());
            setCelda(row, col++, estudiante.getDireccion());
            setCeldaFecha(row, col++, estudiante.getFechaInscripcion(), estiloFecha);
            setCelda(row, col++, estudiante.getHorario() != null ? capitalizar(estudiante.getHorario().name()) : null);
            setCelda(row, col++, describirIngresoA(estudiante.getIngresoA()));
            setCelda(row, col++, estudiante.isLlevaIngles() ? "Sí" : "No");
            setCelda(row, col++, estudiante.getNotas());
            if (incluirMatriculaYEstatus) {
                Grupo grupo = estudiante.getIdGrupo() != null ? gruposPorId.get(estudiante.getIdGrupo()) : null;
                setCelda(row, col++, grupo != null ? grupo.getNombreGrupo() : "Sin grupo");
                setCelda(row, col, estudiante.getEstatus() != null ? capitalizar(estudiante.getEstatus().name()) : null);
            }
        }
        anchoColumnasFijo(sheet, encabezados.size());
    }

    private void escribirHojaCargos(Workbook workbook, Map<Integer, String> matriculaPorIdEstudiante, CellStyle estiloFecha) {
        Sheet sheet = workbook.createSheet("Cargos");
        escribirEncabezado(sheet, workbook, "Matrícula", "Concepto", "Tipo de mensualidad", "Monto total",
                "Fecha de vencimiento", "Estatus");

        int rowIndex = 1;
        for (Cargo cargo : cargoRepository.findAll()) {
            String matricula = matriculaPorIdEstudiante.get(cargo.getIdEstudiante());
            if (matricula == null) {
                continue;
            }
            Row row = sheet.createRow(rowIndex++);
            setCelda(row, 0, matricula);
            setCelda(row, 1, cargo.getConceptoCargo());
            setCelda(row, 2, cargo.getTipoMensualidadCargo() != null ? capitalizar(cargo.getTipoMensualidadCargo().name()) : null);
            setCeldaNumero(row, 3, cargo.getMontoTotalCargo());
            setCeldaFecha(row, 4, cargo.getFechaVencimientoCargo(), estiloFecha);
            setCelda(row, 5, cargo.getEstatusCargo() != null ? capitalizar(cargo.getEstatusCargo().name()) : null);
        }
        anchoColumnasFijo(sheet, 6);
    }

    private void escribirHojaPagos(Workbook workbook, Map<Integer, String> matriculaPorIdEstudiante, CellStyle estiloFecha) {
        Sheet sheet = workbook.createSheet("Pagos");
        escribirEncabezado(sheet, workbook, "Matrícula", "Monto", "Fecha", "Método de pago", "Concepto",
                "Requiere factura", "Observaciones", "Registrado por");

        Map<Integer, Cargo> cargosPorId = cargoRepository.findAll().stream()
                .collect(Collectors.toMap(Cargo::getIdCargo, c -> c));
        Map<Integer, String> usuariosPorId = usuarioRepository.findAll().stream()
                .collect(Collectors.toMap(Usuario::getIdUsuario, Usuario::getNombreUsuario));

        int rowIndex = 1;
        for (Pago pago : pagoRepository.findAll()) {
            Cargo cargo = cargosPorId.get(pago.getIdCargo());
            String matricula = cargo != null ? matriculaPorIdEstudiante.get(cargo.getIdEstudiante()) : null;
            if (matricula == null) {
                continue;
            }
            Row row = sheet.createRow(rowIndex++);
            setCelda(row, 0, matricula);
            setCeldaNumero(row, 1, pago.getMontoPagadoPago());
            setCeldaFecha(row, 2, pago.getFechaPago(), estiloFecha);
            setCelda(row, 3, pago.getMetodoPago() != null ? capitalizar(pago.getMetodoPago().name()) : null);
            String concepto = pago.getConceptoPago() != null && !pago.getConceptoPago().isBlank()
                    ? pago.getConceptoPago()
                    : cargo.getConceptoCargo();
            setCelda(row, 4, concepto);
            setCelda(row, 5, Boolean.TRUE.equals(pago.getRequiereFactura()) ? "Sí" : "No");
            setCelda(row, 6, pago.getNotasPago());
            setCelda(row, 7, pago.getIdUsuario() != null ? usuariosPorId.get(pago.getIdUsuario()) : null);
        }
        anchoColumnasFijo(sheet, 8);
    }

    private void escribirHojaDestinos(Workbook workbook, Map<Integer, String> matriculaPorIdEstudiante) {
        Sheet sheet = workbook.createSheet("Destinos");
        escribirEncabezado(sheet, workbook, "Matrícula", "Tipo", "Nombre", "Carrera");
        int[] rowIndex = {1};

        Map<Integer, String> universidades = universidadRepository.findAll().stream()
                .collect(Collectors.toMap(Universidad::getIdUniversidad, Universidad::getNombreUniversidad));
        Map<Integer, String> carreras = carreraRepository.findAll().stream()
                .collect(Collectors.toMap(Carrera::getIdCarrera, Carrera::getNombreCarrera));
        List<EstudianteUniversidad> vinculosUniversidad = estudianteUniversidadRepository.findAll();
        Map<Integer, List<String>> carrerasPorVinculo = estudianteUniversidadCarreraRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        EstudianteUniversidadCarrera::getIdEstudianteUniversidad,
                        Collectors.mapping(euc -> carreras.getOrDefault(euc.getIdCarrera(), "—"), Collectors.toList())
                ));
        for (EstudianteUniversidad vinculo : vinculosUniversidad) {
            String matricula = matriculaPorIdEstudiante.get(vinculo.getIdEstudiante());
            if (matricula == null) {
                continue;
            }
            String nombreUniversidad = universidades.getOrDefault(vinculo.getIdUniversidad(), "—");
            List<String> carrerasDelVinculo = carrerasPorVinculo.getOrDefault(vinculo.getIdEstudianteUniversidad(), List.of());
            if (carrerasDelVinculo.isEmpty()) {
                escribirFilaDestino(sheet, rowIndex, matricula, "Universidad", nombreUniversidad, "");
            } else {
                for (String carrera : carrerasDelVinculo) {
                    escribirFilaDestino(sheet, rowIndex, matricula, "Universidad", nombreUniversidad, carrera);
                }
            }
        }

        Map<Integer, String> bachilleratos = bachilleratoRepository.findAll().stream()
                .collect(Collectors.toMap(Bachillerato::getIdBachillerato, Bachillerato::getNombreBachillerato));
        for (EstudianteBachillerato vinculo : estudianteBachilleratoRepository.findAll()) {
            String matricula = matriculaPorIdEstudiante.get(vinculo.getIdEstudiante());
            if (matricula == null) {
                continue;
            }
            escribirFilaDestino(sheet, rowIndex, matricula, "Bachillerato",
                    bachilleratos.getOrDefault(vinculo.getIdBachillerato(), "—"), "");
        }

        Map<Integer, String> secundarias = secundariaRepository.findAll().stream()
                .collect(Collectors.toMap(Secundaria::getIdSecundaria, Secundaria::getNombreSecundaria));
        for (EstudianteSecundaria vinculo : estudianteSecundariaRepository.findAll()) {
            String matricula = matriculaPorIdEstudiante.get(vinculo.getIdEstudiante());
            if (matricula == null) {
                continue;
            }
            escribirFilaDestino(sheet, rowIndex, matricula, "Secundaria",
                    secundarias.getOrDefault(vinculo.getIdSecundaria(), "—"), "");
        }

        Map<Integer, String> cursosVerano = cursoVeranoRepository.findAll().stream()
                .collect(Collectors.toMap(CursoVerano::getIdCursoVerano, CursoVerano::getNombreCursoVerano));
        for (EstudianteCursoVerano vinculo : estudianteCursoVeranoRepository.findAll()) {
            String matricula = matriculaPorIdEstudiante.get(vinculo.getIdEstudiante());
            if (matricula == null) {
                continue;
            }
            escribirFilaDestino(sheet, rowIndex, matricula, "Curso de verano",
                    cursosVerano.getOrDefault(vinculo.getIdCursoVerano(), "—"), "");
        }

        Map<Integer, Asesoria> asesorias = asesoriaRepository.findAll().stream()
                .collect(Collectors.toMap(Asesoria::getIdAsesoria, a -> a));
        for (EstudianteAsesoria vinculo : estudianteAsesoriaRepository.findAll()) {
            String matricula = matriculaPorIdEstudiante.get(vinculo.getIdEstudiante());
            Asesoria asesoria = asesorias.get(vinculo.getIdAsesoria());
            if (matricula == null || asesoria == null) {
                continue;
            }
            String descripcion = asesoria.getDiaAsesoria() + " " + asesoria.getHoraAsesoria().getDescripcion();
            escribirFilaDestino(sheet, rowIndex, matricula, "Asesorías", descripcion, "");
        }

        anchoColumnasFijo(sheet, 4);
    }

    private void escribirFilaDestino(Sheet sheet, int[] rowIndex, String matricula, String tipo, String nombre, String carrera) {
        Row row = sheet.createRow(rowIndex[0]++);
        setCelda(row, 0, matricula);
        setCelda(row, 1, tipo);
        setCelda(row, 2, nombre);
        setCelda(row, 3, carrera);
    }

    /** Restringe con un desplegable (data validation de Excel) las columnas cuyo valor solo puede
     *  ser uno de un catálogo fijo, para que no se pueda teclear texto libre inválido que luego
     *  falle al importar. Busca la columna por nombre de encabezado en vez de un índice fijo, así
     *  que sigue funcionando aunque cambie el orden de columnas entre plantilla y exportación. */
    private void aplicarValidacionesColumnasFijas(Sheet sheet, List<String> encabezados) {
        aplicarValidacionLista(sheet, encabezados.indexOf("Horario"), "Escolarizado", "Sabatino", "Virtual");
        aplicarValidacionLista(sheet, encabezados.indexOf("Ingresa a"),
                "Universidad", "Bachillerato", "Secundaria", "Asesorías", "Curso de verano");
        aplicarValidacionLista(sheet, encabezados.indexOf("Lleva inglés"), "Sí", "No");
    }

    private void aplicarValidacionLista(Sheet sheet, int columna, String... opciones) {
        if (columna < 0) {
            return;
        }
        DataValidationHelper helper = sheet.getDataValidationHelper();
        DataValidationConstraint restriccion = helper.createExplicitListConstraint(opciones);
        CellRangeAddressList rango = new CellRangeAddressList(1, ULTIMA_FILA_VALIDACION, columna, columna);
        DataValidation validacion = helper.createValidation(restriccion, rango);
        validacion.setSuppressDropDownArrow(true);
        validacion.setShowErrorBox(true);
        validacion.createErrorBox("Valor no válido", "Selecciona una de las opciones de la lista.");
        sheet.addValidationData(validacion);
    }

    private String describirIngresoA(IngresoA ingresoA) {
        if (ingresoA == null) {
            return null;
        }
        return switch (ingresoA) {
            case UNIVERSIDAD -> "Universidad";
            case BACHILLERATO -> "Bachillerato";
            case SECUNDARIA -> "Secundaria";
            case ASESORIAS -> "Asesorías";
            case CURSO_VERANO -> "Curso de verano";
        };
    }

    private String capitalizar(String enumName) {
        String lower = enumName.toLowerCase(Locale.ROOT).replace('_', ' ');
        return lower.substring(0, 1).toUpperCase(Locale.ROOT) + lower.substring(1);
    }

    private void escribirEncabezado(Sheet sheet, Workbook workbook, String... encabezados) {
        CellStyle estilo = workbook.createCellStyle();
        Font negrita = workbook.createFont();
        negrita.setBold(true);
        estilo.setFont(negrita);
        Row header = sheet.createRow(0);
        for (int i = 0; i < encabezados.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(encabezados[i]);
            cell.setCellStyle(estilo);
        }
    }

    private void anchoColumnasFijo(Sheet sheet, int columnas) {
        for (int i = 0; i < columnas; i++) {
            sheet.setColumnWidth(i, 22 * 256);
        }
    }

    private void setCelda(Row row, int indice, String valor) {
        if (valor == null) {
            return;
        }
        row.createCell(indice).setCellValue(valor);
    }

    private void setCeldaNumero(Row row, int indice, Number valor) {
        if (valor == null) {
            return;
        }
        row.createCell(indice).setCellValue(valor.doubleValue());
    }

    private void setCeldaFecha(Row row, int indice, LocalDate valor, CellStyle estiloFecha) {
        if (valor == null) {
            return;
        }
        Cell cell = row.createCell(indice);
        cell.setCellValue(valor);
        cell.setCellStyle(estiloFecha);
    }
}
