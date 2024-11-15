package be.eafcuccle.tshirtshop.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import be.eafcuccle.tshirtshop.models.Brand;
import be.eafcuccle.tshirtshop.repositories.BrandRepository;
import be.eafcuccle.tshirtshop.service.SequenceService;
import java.util.List;
import java.util.Optional;

/**
 * BrandController is a REST controller that handles CRUD operations for {@link Brand} entities.
 * This controller provides endpoints to create, retrieve, update, and delete brands.
 * Methods are mapped to HTTP requests and return appropriate responses based on the operation's result.
 */
@RestController
@RequestMapping("/api/brands")
public class BrandController {

    private final BrandRepository brandRepository;
    private final SequenceService sequenceService;

    /**
     * Constructs a BrandController with dependencies for the brand repository
     * and the sequence service.
     *
     * @param brandRepository the repository for performing CRUD operations on Brand entities
     * @param sequenceService the service to manage ID sequence after deletion
     */
    public BrandController(BrandRepository brandRepository, SequenceService sequenceService) {
        this.brandRepository = brandRepository;
        this.sequenceService = sequenceService;
    }

    /**
     * Creates a new brand in the system.
     *
     * @param brand the Brand entity to create
     * @return a response containing the created brand and HTTP status 201 Created
     */
    @PostMapping
    public ResponseEntity<Brand> createBrand(@RequestBody Brand brand) {
        Brand savedBrand = brandRepository.save(brand);
        return new ResponseEntity<>(savedBrand, HttpStatus.CREATED);
    }

    /**
     * Retrieves all available brands in the system.
     *
     * @return a response containing the list of all brands and HTTP status 200 OK
     */
    @GetMapping
    public ResponseEntity<List<Brand>> getAllBrands() {
        return ResponseEntity.ok(brandRepository.findAll());
    }

    /**
     * Retrieves a specific brand by its ID.
     *
     * @param id the unique identifier of the brand
     * @return a response containing the brand if it exists, or HTTP status 404 Not Found if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Brand> getBrandById(@PathVariable Integer id) {
        Optional<Brand> brand = brandRepository.findById(id);
        return brand.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Updates an existing brand identified by its ID.
     *
     * @param id    the unique identifier of the brand to update
     * @param brand the new information for the brand
     * @return a response containing the updated brand if it exists, or HTTP status 404 Not Found if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<Brand> updateBrand(@PathVariable Integer id, @RequestBody Brand brand) {
        return brandRepository.findById(id).map(existingBrand -> {
            existingBrand.setName(brand.getName());
            Brand updatedBrand = brandRepository.save(existingBrand);
            return ResponseEntity.ok(updatedBrand);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Deletes an existing brand identified by its ID.
     *
     * @param id the unique identifier of the brand to delete
     * @return a response with HTTP status 204 No Content if the deletion is successful, or HTTP status 404 Not Found if the brand does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable Integer id) {
        if (brandRepository.existsById(id)) {
            brandRepository.deleteById(id);
            sequenceService.reassignIdsAfterDeletion("brand");
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
