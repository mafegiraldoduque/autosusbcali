
package co.edu.usbcali.autosusbcali.service;

import co.edu.usbcali.autosusbcali.domain.Marca;
import co.edu.usbcali.autosusbcali.domain.Modelo;
import co.edu.usbcali.autosusbcali.dto.request.CrearModificarModeloRequest;
import co.edu.usbcali.autosusbcali.dto.response.ObtenerModeloResponse;
import co.edu.usbcali.autosusbcali.mapper.ModeloMapper;
import co.edu.usbcali.autosusbcali.repository.MarcaRepository;
import co.edu.usbcali.autosusbcali.repository.ModeloRepository;
import co.edu.usbcali.autosusbcali.repository.VehiculoRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ModeloService implements IModeloService {

    private final ModeloRepository modeloRepository;
    private final MarcaRepository marcaRepository;
    private final VehiculoRepository vehiculoRepository;

    public ModeloService(
            ModeloRepository modeloRepository,
            MarcaRepository marcaRepository,
            VehiculoRepository vehiculoRepository) {
        this.modeloRepository = modeloRepository;
        this.marcaRepository = marcaRepository;
        this.vehiculoRepository = vehiculoRepository;
    }

    @Override
    public List<ObtenerModeloResponse> obtenerTodos() {
        return ModeloMapper.listaModelosAListaObtenerModeloResponse(
                modeloRepository.findAll()
        );
    }

    @Override
    public List<ObtenerModeloResponse> obtenerModelosPorMarca(Long marcaId) {
        if (marcaId == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El ID de la marca es obligatorio"
            );
        }

        return ModeloMapper.listaModelosAListaObtenerModeloResponse(
                modeloRepository.findByMarcaId(marcaId)
        );
    }

    @Override
    public ObtenerModeloResponse obtenerModeloPorNombre(
            String nombre, Long marcaId) {

        Modelo modelo = modeloRepository.findByNombreAndMarcaId(nombre, marcaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró el modelo"
                ));

        return ModeloMapper.modeloAObtenerModeloResponse(modelo);
    }

    @Override
    public ObtenerModeloResponse obtenerModeloPorId(Long id) {
        Modelo modelo = modeloRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró el modelo"
                ));

        return ModeloMapper.modeloAObtenerModeloResponse(modelo);
    }

    @Override
    public ObtenerModeloResponse crearModelo(
            CrearModificarModeloRequest crearModeloRequest) {

        if (crearModeloRequest == null
                || crearModeloRequest.marcaId() == null
                || crearModeloRequest.nombre() == null
                || crearModeloRequest.nombre().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Datos del modelo inválidos"
            );
        }

        Marca marca = marcaRepository.findById(
                crearModeloRequest.marcaId()
        ).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "No se encontró la marca"
        ));

        if (modeloRepository.existsByNombreAndMarcaId(
                crearModeloRequest.nombre(), marca.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Ya existe un modelo con ese nombre para esta marca"
            );
        }

        Modelo modelo = ModeloMapper.CrearModificarModeloRequestAModelo(
                crearModeloRequest, marca
        );

        Modelo modeloGuardado = modeloRepository.save(modelo);

        return ModeloMapper.modeloAObtenerModeloResponse(modeloGuardado);
    }

    @Override
    public ObtenerModeloResponse modificarModelo(
            Long id,
            CrearModificarModeloRequest modificarModeloRequest) {

        if (id == null
                || modificarModeloRequest == null
                || modificarModeloRequest.marcaId() == null
                || modificarModeloRequest.nombre() == null
                || modificarModeloRequest.nombre().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Datos del modelo inválidos"
            );
        }

        Modelo modelo = modeloRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró el modelo"
                ));

        Marca marca = marcaRepository.findById(
                modificarModeloRequest.marcaId()
        ).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "No se encontró la marca"
        ));

        if ((!modelo.getNombre().equalsIgnoreCase(
                modificarModeloRequest.nombre())
                || modelo.getMarca().getId() != marca.getId())
                && modeloRepository.existsByNombreAndMarcaId(
                modificarModeloRequest.nombre(), marca.getId())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Ya existe otro modelo con ese nombre para esta marca"
            );
        }

        modelo.setNombre(modificarModeloRequest.nombre());
        modelo.setMarca(marca);

        Modelo modeloModificado = modeloRepository.save(modelo);

        return ModeloMapper.modeloAObtenerModeloResponse(modeloModificado);
    }

    @Override
    @Transactional
    public void eliminarModeloFisicamente(Long id) {

        if (id == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El ID del modelo es obligatorio"
            );
        }

        Modelo modelo = modeloRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró el modelo"
                ));

        if (vehiculoRepository.existsByModeloId(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede eliminar el modelo porque tiene vehículos asociados"
            );
        }

        modeloRepository.delete(modelo);
    }
}
