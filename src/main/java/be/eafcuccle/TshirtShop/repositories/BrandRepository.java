package be.eafcuccle.tshirtshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import be.eafcuccle.tshirtshop.models.Brand;
import java.util.Optional;

/**
 * Repository interface for the {@link Brand} entity, providing CRUD operations
 * and additional custom query methods.
 */
@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {

    /**
     * Finds a brand by its unique name.
     *
     * @param name the name of the brand to be searched
     * @return an {@link Optional} containing the Brand if found, or empty if not found
     */
    Optional<Brand> findByName(String name);
}
