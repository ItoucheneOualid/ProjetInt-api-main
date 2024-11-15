package be.eafcuccle.tshirtshop.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import be.eafcuccle.tshirtshop.models.Category;
import be.eafcuccle.tshirtshop.repositories.CategoryRepository;
import be.eafcuccle.tshirtshop.dto.CategoryDTO;
import be.eafcuccle.tshirtshop.service.SequenceService;
import java.util.*;

/**
 * CategoryController is a REST controller that manages CRUD operations for {@link Category} entities.
 * It provides endpoints to create, retrieve, update, delete categories, as well as initialize default categories.
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final SequenceService sequenceService;

    /**
     * Constructs a CategoryController with dependencies for the category repository and sequence service.
     *
     * @param categoryRepository the repository for performing CRUD operations on Category entities
     * @param sequenceService the service for managing sequence IDs after deletion
     */
    public CategoryController(CategoryRepository categoryRepository, SequenceService sequenceService) {
        this.categoryRepository = categoryRepository;
        this.sequenceService = sequenceService;
    }

    /**
     * Creates a new category.
     *
     * @param category the Category entity to create
     * @return a response containing the created category and HTTP status 201 Created
     */
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        return new ResponseEntity<>(categoryRepository.save(category), HttpStatus.CREATED);
    }

    /**
     * Retrieves all available categories.
     *
     * @return a response containing the list of all categories and HTTP status 200 OK
     */
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryRepository.findAll());
    }

    @GetMapping("/dtos")
    public ResponseEntity<List<CategoryDTO>> getCategoryNameId() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOs = new ArrayList<>();
        for(Category category : categories) {
            categoryDTOs.add(new CategoryDTO(category));
        }
        return ResponseEntity.ok(categoryDTOs);
    }
    /**
     * Retrieves a specific category by its ID.
     *
     * @param id the unique identifier of the category
     * @return a response containing the category if found, or HTTP status 404 Not Found if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable String id) {
        return categoryRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retrieves category names along with product counts for each category.
     *
     * @return a response containing a list of category names with corresponding product counts and HTTP status 200 OK
     */
    @GetMapping("/names-with-counts")
    public ResponseEntity<List<Map<String, Object>>> getCategoryNamesWithProductCounts() {
        List<Object[]> categoryData = categoryRepository.findCategoryNamesWithProductCounts();
        List<Map<String, Object>> response = new ArrayList<>();
        categoryData.forEach(data -> {
            Map<String, Object> categoryInfo = new HashMap<>();
            categoryInfo.put("name", data[0]);
            categoryInfo.put("productCount", data[1]);
            response.add(categoryInfo);
        });
        return ResponseEntity.ok(response);
    }

    /**
     * Initializes the default categories if they do not already exist in the database.
     *
     * @return a response with a message indicating that the default categories have been initialized and HTTP status 201 Created
     */
    @PostMapping("/init")
    public ResponseEntity<String> initializeCategories() {
        List<String> defaultCategories = Arrays.asList("Homme", "Femme", "Mixte", "Enfant Fille", "Enfant Garçon");
        defaultCategories.forEach(name ->
                categoryRepository.findByName(name)
                        .orElseGet(() -> categoryRepository.save(new Category(name)))
        );
        return ResponseEntity.status(HttpStatus.CREATED).body("Default categories initialized.");
    }

    /**
     * Updates an existing category identified by its ID.
     *
     * @param id the unique identifier of the category to update
     * @param category the new information for the category
     * @return a response containing the updated category if found, or HTTP status 404 Not Found if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable String id, @RequestBody Category category) {
        return categoryRepository.findById(id)
                .map(existingCategory -> {
                    existingCategory.setName(category.getName());
                    return categoryRepository.save(existingCategory);
                })
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes an existing category identified by its ID.
     *
     * @param id the unique identifier of the category to delete
     * @return a response with HTTP status 204 No Content if deletion is successful, or HTTP status 404 Not Found if the category does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}
