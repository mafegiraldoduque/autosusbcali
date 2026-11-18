
package co.edu.usbcali.autosusbcali.repository;

import co.edu.usbcali.autosusbcali.domain.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModeloRepository extends JpaRepository<Modelo, Long> {

    Optional<Modelo> findByNombreAndMarcaId(String nombre, Long marcaId);

    List<Modelo> findByMarcaId(Long marcaId);

    Boolean existsByNombreAndMarcaId(String nombre, Long marcaId);
}
