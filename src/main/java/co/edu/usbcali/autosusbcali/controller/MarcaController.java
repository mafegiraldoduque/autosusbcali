package co.edu.usbcali.autosusbcali.controller;

import co.edu.usbcali.autosusbcali.dto.request.CrearModificarMarcaRequest;
import co.edu.usbcali.autosusbcali.dto.response.ObtenerMarcaResponse;
import co.edu.usbcali.autosusbcali.service.IMarcaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marcas")
public class MarcaController {

    private final IMarcaService marcaService;

    public MarcaController(IMarcaService marcaService) {
        this.marcaService = marcaService;
    }

    @GetMapping
    public ResponseEntity<List<ObtenerMarcaResponse>> obtenerTodos() {
        return ResponseEntity.ok(marcaService.obtenerTodos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<ObtenerMarcaResponse>> obtenerActivos() {
        return ResponseEntity.ok(marcaService.buscarMarcaPorEstado(true));
    }

    @GetMapping("/inactivos")
    public ResponseEntity<List<ObtenerMarcaResponse>> obtenerInactivos() {
        return ResponseEntity.ok(marcaService.buscarMarcaPorEstado(false));
    }

    @GetMapping("/por-nombre/{nombre}")
    public ResponseEntity<ObtenerMarcaResponse> obtenerPorNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(marcaService.obtenerMarcaPorNombre(nombre));
    }

    @GetMapping("/por-id/{id}")
    public ResponseEntity<ObtenerMarcaResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(marcaService.obtenerMarcaPorId(id));
    }

    @PostMapping("/guardar")
    public ResponseEntity<ObtenerMarcaResponse> crearMarca(
            @RequestBody CrearModificarMarcaRequest request) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(marcaService.crearMarca(request));
    }

    @PutMapping("/modificar/{id}")
    public ResponseEntity<ObtenerMarcaResponse> modificarMarca(
            @PathVariable Long id,
            @RequestBody CrearModificarMarcaRequest request) throws Exception {
        return ResponseEntity.ok(marcaService.modificarMarca(id, request));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarMarca(@PathVariable Long id) throws Exception {
        marcaService.eliminarMarca(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/eliminar-fisico/{id}")
    public ResponseEntity<Void> eliminarMarcaFisicamente(@PathVariable Long id) {
        marcaService.eliminarMarcaFisicamente(id);
        return ResponseEntity.noContent().build();
    }
}