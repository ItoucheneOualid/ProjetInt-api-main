package be.eafcuccle.tshirtshop.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import be.eafcuccle.tshirtshop.models.Brand;
import be.eafcuccle.tshirtshop.models.Product;
import be.eafcuccle.tshirtshop.repositories.BrandRepository;
import be.eafcuccle.tshirtshop.repositories.ProductRepository;
import org.springframework.data.domain.Sort;
import java.util.List;

/**
 * Service for managing ID reassignment for entities in the T-shirt shop application.
 * It supports reassigning sequential IDs for products and brands after a deletion operation.
 */
@Service
public class SequenceService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;

    /**
     * Constructs a SequenceService with dependencies for product and brand repositories.
     *
     * @param productRepository the repository for performing operations on Product entities
     * @param brandRepository the repository for performing operations on Brand entities
     */
    public SequenceService(ProductRepository productRepository, BrandRepository brandRepository) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
    }

    /**
     * Reassigns sequential IDs to entities of the specified type after a deletion.
     * This method will call either {@link #reassignProductIds()} or {@link #reassignBrandIds()}
     * based on the provided entity name.
     *
     * @param entityName the name of the entity type ("product" or "brand")
     * @throws IllegalArgumentException if the entity name is not recognized
     */
    @Transactional
    public void reassignIdsAfterDeletion(String entityName) {
        if ("product".equals(entityName)) {
            reassignProductIds();
        } else if ("brand".equals(entityName)) {
            reassignBrandIds();
        } else {
            throw new IllegalArgumentException("Entity not recognized: " + entityName);
        }
    }

    /**
     * Reassigns sequential IDs to all products, starting from 1.
     * Products are sorted by their current ID in ascending order before reassignment.
     */
    private void reassignProductIds() {
        List<Product> products = productRepository.findAll(Sort.by(Sort.Direction.ASC, "productId"));
        for (int i = 0; i < products.size(); i++) {
            products.get(i).setProductId(i + 1);
        }
        productRepository.saveAll(products);
    }

    /**
     * Reassigns sequential IDs to all brands, starting from 1.
     * Brands are sorted by their current ID in ascending order before reassignment.
     */
    private void reassignBrandIds() {
        List<Brand> brands = brandRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        for (int i = 0; i < brands.size(); i++) {
            brands.get(i).setId(i + 1);
        }
        brandRepository.saveAll(brands);
    }
}
