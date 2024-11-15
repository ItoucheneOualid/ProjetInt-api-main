package be.eafcuccle.tshirtshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import be.eafcuccle.tshirtshop.models.Category;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for the {@link Category} entity, providing CRUD operations
 * and additional custom query methods.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    /**
     * Retrieves the names of all categories along with the count of products in each category.
     * This method uses a custom query to join categories with products and count products per category.
     *
     * @return a list of Object arrays where each array contains the category name and the product count
     */
    @Query("SELECT c.name, COUNT(p) FROM Category c LEFT JOIN Product p ON p.category.id = c.id GROUP BY c.name")
    List<Object[]> findCategoryNamesWithProductCounts();

    /**
     * Finds a category by its unique name.
     *
     * @param name the name of the category
     * @return an {@link Optional} containing the Category if found, or empty if not found
     */
    Optional<Category> findByName(String name);
}
