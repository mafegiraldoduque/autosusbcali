package co.edu.usbcali.autosusbcali.service;

import co.edu.usbcali.autosusbcali.domain.Marca;
import co.edu.usbcali.autosusbcali.dto.request.CrearModificarMarcaRequest;
import co.edu.usbcali.autosusbcali.dto.response.ObtenerMarcaResponse;
import co.edu.usbcali.autosusbcali.mapper.MarcaMapper;
import co.edu.usbcali.autosusbcali.repository.MarcaRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MarcaService implements IMarcaService {

    private final MarcaRepository marcaRepository;

    public MarcaService(MarcaRepository marcaRepository) {
        this.marcaRepository = marcaRepository;
    }

    @Override
    public List<ObtenerMarcaResponse> obtenerTodos() {
        return MarcaMapper.listaMarcasAListaObtenerMarcaResponse(
                marcaRepository.findAll()
        );
    }

    @Override
    public List<ObtenerMarcaResponse> buscarMarcaPorEstado(Boolean activo) {
        return MarcaMapper.listaMarcasAListaObtenerMarcaResponse(
                marcaRepository.findByActivo(activo)
        );
    }

    @Override
    public ObtenerMarcaResponse obtenerMarcaPorNombre(String nombre) {
        Marca marca = marcaRepository.findByNombre(nombre)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No se encontró la marca"
                ));

        return MarcaMapper.marcaAObtenerMarcaResponse(marca);
    }

    @Override
    public ObtenerMarcaResponse obtenerMarcaPorId(Long id) {
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No se encontró la marca"
                ));

        return MarcaMapper.marcaAObtenerMarcaResponse(marca);
    }

    @Override
    public ObtenerMarcaResponse crearMarca(CrearModificarMarcaRequest crearMarcaRequest) {
        if (crearMarcaRequest == null || crearMarcaRequest.nombre() == null
                || crearMarcaRequest.nombre().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El nombre de la marca es obligatorio"
            );
        }

        if (marcaRepository.existsByNombre(crearMarcaRequest.nombre())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Ya existe una marca con ese nombre"
            );
        }

        Marca marca = MarcaMapper.CrearModificarMarcaRequestAMarca(crearMarcaRequest);
        Marca marcaGuardada = marcaRepository.save(marca);

        return MarcaMapper.marcaAObtenerMarcaResponse(marcaGuardada);
    }

    @Override
    public ObtenerMarcaResponse modificarMarca(Long id, CrearModificarMarcaRequest modificarMarcaRequest) {
        if (id == null || modificarMarcaRequest == null
                || modificarMarcaRequest.nombre() == null
                || modificarMarcaRequest.nombre().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Datos de la marca inválidos"
            );
        }

        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No se encontró la marca"
                ));

        if (!marca.getNombre().equalsIgnoreCase(modificarMarcaRequest.nombre())
                && marcaRepository.existsByNombre(modificarMarcaRequest.nombre())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Ya existe otra marca con ese nombre"
            );
        }

        marca.setNombre(modificarMarcaRequest.nombre());
        Marca marcaModificada = marcaRepository.save(marca);

        return MarcaMapper.marcaAObtenerMarcaResponse(marcaModificada);
    }

    @Override
    public void eliminarMarca(Long id) {
        if (id == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El ID de la marca es obligatorio"
            );
        }

        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No se encontró la marca"
                ));

        marca.setActivo(false);
        marcaRepository.save(marca);
    }

    @Override
    @Transactional
    public void eliminarMarcaFisicamente(Long id) {
        if (id == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El ID de la marca es obligatorio"
            );
        }

        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No se encontró la marca"
                ));

        marcaRepository.delete(marca);
    }
}