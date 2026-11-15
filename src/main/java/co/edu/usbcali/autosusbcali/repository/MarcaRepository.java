
package co.edu.usbcali.autosusbcali.repository;

import co.edu.usbcali.autosusbcali.domain.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Long> {

    Optional<Marca> findByNombre(String nombre);

    List<Marca> findByActivo(Boolean activo);

    Boolean existsByNombre(String nombre);
}
