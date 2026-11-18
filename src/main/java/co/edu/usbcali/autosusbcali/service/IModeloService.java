
package co.edu.usbcali.autosusbcali.service;

import co.edu.usbcali.autosusbcali.dto.request.CrearModificarModeloRequest;
import co.edu.usbcali.autosusbcali.dto.response.ObtenerModeloResponse;

import java.util.List;

public interface IModeloService {

    List<ObtenerModeloResponse> obtenerTodos();

    List<ObtenerModeloResponse> obtenerModelosPorMarca(Long marcaId);

    ObtenerModeloResponse obtenerModeloPorNombre(String nombre, Long marcaId);

    ObtenerModeloResponse obtenerModeloPorId(Long id);

    ObtenerModeloResponse crearModelo(CrearModificarModeloRequest crearModeloRequest) throws Exception;

    ObtenerModeloResponse modificarModelo(Long id, CrearModificarModeloRequest modificarModeloRequest) throws Exception;

    void eliminarModeloFisicamente(Long id);
}
