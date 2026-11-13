package co.edu.usbcali.autosusbcali.service;

import co.edu.usbcali.autosusbcali.domain.Rol;
import co.edu.usbcali.autosusbcali.dto.request.CrearModificarRolRequest;
import co.edu.usbcali.autosusbcali.dto.response.ObtenerRolResponse;
import co.edu.usbcali.autosusbcali.mapper.RolMapper;
import co.edu.usbcali.autosusbcali.repository.RolRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RolService implements IRolService{
    private final RolRepository rolRepository;

    @Override
    public List<ObtenerRolResponse> obtenerTodos() {
        return RolMapper.listaRolesAListaObtenerRolResponse(rolRepository.findAll());
    }

    @Override
    public List<ObtenerRolResponse> buscarRolPorEstado(Boolean activo) {
        List<Rol> rolesPorEstado = rolRepository.findByActivo(activo);
        return RolMapper.listaRolesAListaObtenerRolResponse(rolesPorEstado);
    }

    @Override
    public ObtenerRolResponse obtenerRolPorNombre(String nombre) {
        Rol rolPorNombre = rolRepository.findByNombre(nombre)
                .orElseThrow(() -> new RuntimeException("No se ha encontrado el rol por nombre: "+nombre));
        return RolMapper.rolAObtenerRolResponse(rolPorNombre);
    }

    @Override
    public ObtenerRolResponse obtenerRolPorId(Long id) {
        Rol rolPorId = rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se ha encontrado el rol por id "+id));
        return RolMapper.rolAObtenerRolResponse(rolPorId);
    }

    @Override
    public ObtenerRolResponse crearRol(CrearModificarRolRequest crearModificarRolRequest) throws Exception {
        //Validaciones del objeto #crearRolRequest
        if (crearModificarRolRequest == null) {
            throw new Exception("El objeto CrearModificarRolRequest no puede ser nulo.");
        }
        if (crearModificarRolRequest.nombre() == null
                || crearModificarRolRequest.nombre().isBlank()) {
            throw new Exception("El nombre no puede ser nulo ni estar vacío.");
        }
        if (crearModificarRolRequest.descripcion() == null
                || crearModificarRolRequest.descripcion().isBlank()) {
            throw new Exception("El descripcion no puede ser nulo ni estar vacío.");
        }
        //Buscar si existe un rol con el nombre que se quiere agregar. Opcion 1. Traer el objeto
        /*
        * Optional<Rol> rolPorNombre = rolRepository.findByNombre(CrearModificarRolRequest.nombre());
        if (rolPorNombre.isPresent()) {
            throw new Exception("El rol existe en la base de datos "+CrearModificarRolRequest.nombre());
        }*/

        //Buscar si existe un rol con el nombre que se quiere agregar. Opcion 2. Buscar booleano
        if (rolRepository.existsByNombre(crearModificarRolRequest.nombre())) {
            throw new Exception("Ya existe el rol con el mismo nombre.");
        }

        // Usar mapper para convertir DTO a objeto de Domain
        Rol rol = RolMapper.CrearModificarRolRequestARol(crearModificarRolRequest);

        // Persistir el Rol en base de datos
        rol = rolRepository.save(rol);

        // crear DTO response obtenerRolResponse
        ObtenerRolResponse rolResponse = RolMapper.rolAObtenerRolResponse(rol);
        // Retornar rolResponse
        return rolResponse;
    }

    @Override
    public ObtenerRolResponse modificarRol(Long id, CrearModificarRolRequest crearModificarRolRequest) throws Exception {
        // Comprobar que se haya recibido el identificador del rol
        if (id == null) {
            throw new Exception("El id no puede ser nulo.");
        }

        // Validar que los datos enviados para la modificación estén completos
        if (crearModificarRolRequest == null) {
            throw new Exception("El objeto CrearModificarRolRequest no puede ser nulo.");
        }
        if (crearModificarRolRequest.nombre() == null
                || crearModificarRolRequest.nombre().isBlank()) {
            throw new Exception("El nombre no puede ser nulo ni estar vacío.");
        }
        if (crearModificarRolRequest.descripcion() == null
                || crearModificarRolRequest.descripcion().isBlank()) {
            throw new Exception("La descripcion no puede ser nula ni estar vacía.");
        }

        // Buscar si el rol que se desea modificar está registrado
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new Exception("No existe un rol con el id " + id));

        // Validar que el nuevo nombre no esté asignado a otro rol
        if (!rol.getNombre().equals(crearModificarRolRequest.nombre())
                && rolRepository.existsByNombre(crearModificarRolRequest.nombre())) {
            throw new Exception("Ya existe otro rol con el mismo nombre.");
        }

        // Actualizar la información del rol encontrado
        rol.setNombre(crearModificarRolRequest.nombre());
        rol.setDescripcion(crearModificarRolRequest.descripcion());
        // Guardar los cambios realizados en la base de datos
        rol = rolRepository.save(rol);
        // Retornar el rol con la información actualizada
        return RolMapper.rolAObtenerRolResponse(rol);
    }

    @Override
    public void eliminarRol(Long id) throws Exception {
        // Verificar que se haya ingresado un identificador
        if (id == null) {
            throw new Exception("El id no puede ser nulo.");
        }

        // Buscar el rol que se desea eliminar
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new Exception("No existe un rol con el id " + id));

        // Comprobar que el rol todavía se encuentre activo
        if (!rol.getActivo()) {
            throw new Exception("El rol ya se encuentra inactivo.");
        }
        // Cambiar el estado del rol para dejarlo inactivo
        rol.setActivo(false);
        // Guardar la actualización del estado en la base de datos
        rolRepository.save(rol);
    }
}
