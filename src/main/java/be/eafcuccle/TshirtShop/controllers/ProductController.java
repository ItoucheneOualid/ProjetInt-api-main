package be.eafcuccle.tshirtshop.controllers;

import be.eafcuccle.tshirtshop.models.Product;
import be.eafcuccle.tshirtshop.models.Category;
import be.eafcuccle.tshirtshop.models.Brand;
import be.eafcuccle.tshirtshop.models.Size;
import be.eafcuccle.tshirtshop.dto.ProductDTO;
import be.eafcuccle.tshirtshop.repositories.ProductRepository;
import be.eafcuccle.tshirtshop.repositories.CategoryRepository;
import be.eafcuccle.tshirtshop.repositories.BrandRepository;
import be.eafcuccle.tshirtshop.service.SequenceService;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.*;

/**
 * ProductController is a REST controller that manages CRUD operations for
 * {@link Product} entities.
 * It provides endpoints to create, retrieve, update, delete products, as well
 * as view detailed product information
 * and download product images.
 */
@RestController
@RequestMapping("/api/products")
@Validated
public class ProductController {

    private final ProductRepository productRepository;
    private final SequenceService sequenceService;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    /**
     * Constructs a ProductController with dependencies for the product repository
     * and sequence service.
     *
     * @param productRepository the repository for performing CRUD operations on
     *                          Product entities
     * @param sequenceService   the service for managing sequence IDs after deletion
     */
    public ProductController(ProductRepository productRepository, SequenceService sequenceService,
                             CategoryRepository categoryRepository, BrandRepository brandRepository) {
        this.productRepository = productRepository;
        this.sequenceService = sequenceService;
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
    }

    @PostConstruct
    public void addData() {
        categoryRepository.save(new Category("Hommes"));
        categoryRepository.save(new Category("Femmes"));
        categoryRepository.save(new Category("Enfants"));
        brandRepository.save(new Brand("Nike"));
        brandRepository.save(new Brand("Adidas"));
        brandRepository.save(new Brand("Jack & Jones"));
        productRepository.save(new Product("T-Shirt simple", "Un simple demi manche", 15.95f, 10, "Blanc",
                categoryRepository.findByName("Hommes").get(), brandRepository.findByName("Nike").get(), Size.L));
        productRepository.save(new Product("T-Shirt V", "Un simple demi manche avec col en V", 17.95f, 5, "Blanc",
                categoryRepository.findByName("Hommes").get(), brandRepository.findByName("Nike").get(), Size.M));
        productRepository.save(new Product("T-Shirt Oversize", "Un demi manche large", 19.95f, 23, "Bleu",
                categoryRepository.findByName("Femmes").get(), brandRepository.findByName("Jack & Jones").get(),
                Size.S));
    }

    /**
     * Retrieves detailed information for a specific product by its ID.
     *
     * @param id the unique identifier of the product
     * @return a response containing product details if found, or HTTP status 404
     *         Not Found if not found
     */
    @GetMapping("/{id}/details")
    public ResponseEntity<Map<String, Object>> getProductDetails(@PathVariable Integer id) {
        return productRepository.findDetailedProductById(id)
                .map(product -> {
                    Map<String, Object> productDetails = new HashMap<>();
                    productDetails.put("name", product.getProductName());
                    productDetails.put("brand", product.getBrand().getName());
                    productDetails.put("price", product.getUnitPrice());
                    productDetails.put("category", product.getCategory().getName());
                    productDetails.put("imagePath", product.getImagePath());
                    return ResponseEntity.ok(productDetails);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retrieves all products.
     *
     * @return a response containing the list of all products and HTTP status 200 OK
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productRepository.findAll());
    }

    /**
     * Retrieves a specific product by its ID.
     *
     * @param id the unique identifier of the product
     * @return a response containing the product if found, or HTTP status 404 Not
     *         Found if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer id) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    /**
     * Retrieves the names of all products.
     *
     * @return a response containing the list of all product names and HTTP status 200 OK
     */
    @GetMapping("/names")
    public ResponseEntity<List<String>> getProductNames() {
        List<String> productNames = productRepository.findAll()
                .stream()
                .map(Product::getProductName)
                .toList();
        return ResponseEntity.ok(productNames);
    }
    /**
     * Creates a new product.
     *
     * @param product the Product entity to create
     * @return a response containing the created product and HTTP status 201 Created
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        return new ResponseEntity<>(productRepository.save(product), HttpStatus.CREATED);
    }

    /**
     * Updates an existing product identified by its ID.
     *
     * @param id      the unique identifier of the product to update
     * @param product the new information for the product
     * @return a response containing the updated product if found, or HTTP status
     *         404 Not Found if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer id, @RequestBody Product product) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setProductName(product.getProductName());
                    existingProduct.setProductDescription(product.getProductDescription());
                    existingProduct.setUnitPrice(product.getUnitPrice());
                    existingProduct.setQuantityInStock(product.getQuantityInStock());
                    existingProduct.setColor(product.getColor());
                    existingProduct.setCategory(product.getCategory());
                    existingProduct.setBrand(product.getBrand());
                    existingProduct.setSize(product.getSize());
                    return ResponseEntity.ok(productRepository.save(existingProduct));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes an existing product identified by its ID.
     *
     * @param id the unique identifier of the product to delete
     * @return a response with HTTP status 204 No Content if deletion is successful,
     *         or HTTP status 404 Not Found if the product does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            sequenceService.reassignIdsAfterDeletion("product");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Retrieves the image associated with a specific product by its ID.
     *
     * @param id the unique identifier of the product
     * @return a response containing the image file if found, or HTTP status 404 Not
     *         Found if the image or product is not found
     */
    @GetMapping("/image/{id}")
    public ResponseEntity<Resource> getProductImage(@PathVariable Integer id) {
        return productRepository.findById(id)
                .flatMap(product -> {
                    Path imagePath = Paths.get("src/main/resources/images/tshirts/" + id + ".jpg");
                    try {
                        if (Files.exists(imagePath)) {
                            Resource image = new UrlResource(imagePath.toUri());
                            return Optional.of(ResponseEntity.ok()
                                    .contentType(MediaType.IMAGE_JPEG)
                                    .header(HttpHeaders.CONTENT_DISPOSITION,
                                            "attachment; filename=\"" + image.getFilename() + "\"")
                                    .body(image));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return Optional.empty();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retrieve all products of a given category name
     *
     * @param categoryId the given category name
     * @return the list of the products of the category
     */
    @GetMapping("/catagory/{categoryId}")
    public ResponseEntity<List<ProductDTO>> getProductByCategory(@PathVariable String categoryId) {
        Optional<Category> cat = categoryRepository.findById(categoryId);
        if (!cat.isPresent())
            return ResponseEntity.notFound().build();
        List<Product> products = cat.get().getProducts();
        List<ProductDTO> res = new ArrayList<>();
        for (Product product : products) {
            res.add(new ProductDTO(product));
        }
        return ResponseEntity.ok(res);
    }
}
