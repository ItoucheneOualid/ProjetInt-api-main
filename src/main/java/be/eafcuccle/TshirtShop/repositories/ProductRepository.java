package be.eafcuccle.tshirtshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import be.eafcuccle.tshirtshop.models.Product;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for the {@link Product} entity, providing CRUD operations
 * and additional custom query methods.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    /**
     * Finds all products within a specified price range.
     *
     * @param minPrice the minimum price
     * @param maxPrice the maximum price
     * @return a list of products within the specified price range
     */
    List<Product> findProductsByUnitPriceBetween(float minPrice, float maxPrice);

    /**
     * Finds a product by its ID with detailed information, including associated category and brand.
     *
     * @param id the product ID
     * @return an {@link Optional} containing the product with category and brand, or empty if not found
     */
    @Query("SELECT p FROM Product p JOIN FETCH p.category JOIN FETCH p.brand WHERE p.productId = :id")
    Optional<Product> findDetailedProductById(Integer id);

    /**
     * Finds all products belonging to a specific category name.
     *
     * @param categoryName the name of the category
     * @return a list of products in the specified category
     */
    List<Product> findByCategoryName(String categoryName);

    /**
     * Finds all products belonging to a specific brand name.
     *
     * @param brandName the name of the brand
     * @return a list of products of the specified brand
     */
    List<Product> findByBrandName(String brandName);

    /**
     * Finds all products whose name contains the specified substring, case-insensitive.
     *
     * @param namePart the substring to search for in product names
     * @return a list of products with names containing the specified substring
     */
    List<Product> findByProductNameContainingIgnoreCase(String namePart);

    /**
     * Finds all products with stock quantity greater than the specified amount.
     *
     * @param quantity the minimum stock quantity
     * @return a list of products with stock greater than the specified amount
     */
    List<Product> findByQuantityInStockGreaterThan(int quantity);

    /**
     * Finds all products ordered by unit price in ascending order.
     *
     * @return a list of products sorted by ascending unit price
     */
    List<Product> findAllByOrderByUnitPriceAsc();

}
