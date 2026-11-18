
package co.edu.usbcali.autosusbcali.mapper;

import co.edu.usbcali.autosusbcali.domain.Modelo;
import co.edu.usbcali.autosusbcali.domain.Marca;
import co.edu.usbcali.autosusbcali.dto.request.CrearModificarModeloRequest;
import co.edu.usbcali.autosusbcali.dto.response.ObtenerModeloResponse;

import java.util.List;

public class ModeloMapper {

    public static ObtenerModeloResponse modeloAObtenerModeloResponse(Modelo modelo) {
        return new ObtenerModeloResponse(
                modelo.getId(),
                modelo.getMarca().getId(),
                modelo.getMarca().getNombre(),
                modelo.getNombre()
        );
    }

    public static List<ObtenerModeloResponse> listaModelosAListaObtenerModeloResponse(List<Modelo> modelos) {
        return modelos.stream()
                .map(ModeloMapper::modeloAObtenerModeloResponse)
                .toList();
    }

    public static Modelo CrearModificarModeloRequestAModelo(
            CrearModificarModeloRequest crearModificarModeloRequest,
            Marca marca) {

        return new Modelo(
                null,
                marca,
                crearModificarModeloRequest.nombre()
        );
    }
}
