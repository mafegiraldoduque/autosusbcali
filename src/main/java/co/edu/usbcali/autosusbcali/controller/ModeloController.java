
package co.edu.usbcali.autosusbcali.controller;

import co.edu.usbcali.autosusbcali.dto.request.CrearModificarModeloRequest;
import co.edu.usbcali.autosusbcali.dto.response.ObtenerModeloResponse;
import co.edu.usbcali.autosusbcali.service.IModeloService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modelos")
public class ModeloController {

    private final IModeloService modeloService;

    public ModeloController(IModeloService modeloService) {
        this.modeloService = modeloService;
    }

    @GetMapping
    public ResponseEntity<List<ObtenerModeloResponse>> obtenerTodos() {
        return ResponseEntity.ok(modeloService.obtenerTodos());
    }

    @GetMapping("/por-marca/{marcaId}")
    public ResponseEntity<List<ObtenerModeloResponse>> obtenerModelosPorMarca(
            @PathVariable Long marcaId) {
        return ResponseEntity.ok(modeloService.obtenerModelosPorMarca(marcaId));
    }

    @GetMapping("/por-nombre")
    public ResponseEntity<ObtenerModeloResponse> obtenerPorNombre(
            @RequestParam String nombre,
            @RequestParam Long marcaId) {
        return ResponseEntity.ok(
                modeloService.obtenerModeloPorNombre(nombre, marcaId)
        );
    }

    @GetMapping("/por-id/{id}")
    public ResponseEntity<ObtenerModeloResponse> obtenerPorId(
            @PathVariable Long id) {
        return ResponseEntity.ok(modeloService.obtenerModeloPorId(id));
    }

    @PostMapping("/guardar")
    public ResponseEntity<ObtenerModeloResponse> crearModelo(
            @RequestBody CrearModificarModeloRequest request) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(modeloService.crearModelo(request));
    }

    @PutMapping("/modificar/{id}")
    public ResponseEntity<ObtenerModeloResponse> modificarModelo(
            @PathVariable Long id,
            @RequestBody CrearModificarModeloRequest request) throws Exception {
        return ResponseEntity.ok(
                modeloService.modificarModelo(id, request)
        );
    }

    @DeleteMapping("/eliminar-fisico/{id}")
    public ResponseEntity<Void> eliminarModeloFisicamente(
            @PathVariable Long id) {
        modeloService.eliminarModeloFisicamente(id);
        return ResponseEntity.noContent().build();
    }
}
