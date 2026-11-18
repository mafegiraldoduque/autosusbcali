package co.edu.usbcali.autosusbcali.dto.response;

public record ObtenerModeloResponse(
        Long id,
        Long marcaId,
        String marcaNombre,
        String nombre
) {
}
